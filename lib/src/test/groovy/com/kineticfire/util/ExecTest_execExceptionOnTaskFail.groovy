/*
 * (c) Copyright 2023 KineticFire. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kineticfire.util


import com.kineticfire.util.Exec


import java.util.Map
import java.util.HashMap
import static java.util.concurrent.TimeUnit.MINUTES
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Files

import spock.lang.Specification
import spock.lang.Timeout
import spock.lang.TempDir




/**
 * Unit tests for 'Exec.exceptionOnTaskFail(...)'.
 *
 */
@Timeout( value = 1, unit = MINUTES )
class ExecTest_execExceptionOnTaskFail extends Specification {

    @TempDir
    Path tempDir


    // ********************************************************
    // execExceptionOnTaskFail
    //      - task, x, x, x
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task without CL arguments returns correct result"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'whoami' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null );

        then: "result contains correct username"
        String usernameExpected = System.properties[ 'user.name' ]
        result.equals( usernameExpected );
    }


    /* todo- convert

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task with one CL argument returns exitValue of 0"( ) {

        given: "command with arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'id', '-un' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for invalid task returns non-zero exitValue"( ) {

        given: "command to produce error"
        List<String> task = Arrays.asList( 'ls', '-j' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '2'"
        resultMap.exitValue.equals( '2' )

        and: "map key 'out' is empty string"
        resultMap.out.equals( '' )

        and: "map key 'err' contains 'invalid option'"
        resultMap.err.contains( 'invalid option' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for empty task"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( '' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "thrown exception"
        thrown IOException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for task with null CL argument"( ) {

        given: "command without arguments to execute to get the current username"
        List<String> task = Arrays.asList( 'ls', null )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "thrown exception"
        thrown NullPointerException
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config, x, x
    //           (empty)
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'config' is empty"( ) {

        given: "command to execute to get the current username, with empty config"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config, x, x
    //           - trim
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns trimmed output when trim=true"( ) {

        given: "command to execute to get the current username, and trim set to true"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'true' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out', with output trimmed"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns untrimmed output when trim=false"( ) {

        given: "command to execute to get the current username, and trim set to false"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'false' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out', with output untrimmed"
        String usernameExpected = System.properties[ 'user.name' ] + '\n';
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws exception for invalid trim value"( ) {

        given: "command to execute to get the current username, and invalid trim setting"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'trim', 'illegal-value' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "thrown exception"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config, x, x
    //           - directory
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns trimmed output when trim=true"( ) {

        given: "command to execute to get the current username, and trim set to true"
        List<String> task = Arrays.asList( 'pwd' )
        Map<String,String> cfg = new HashMap<String,String>( )
        cfg.put( 'directory', tempDir.toString( ) )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )
        //?
        String oldWorkingDir = Exec.exec( task, null, null, null ).out
        Map<String,String> resultMap = Exec.exec( task, cfg, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct new working directory in map key 'out'"
        tempDir.toString( ).equals( resultMap.out )

        and: "previously had a different working directory than the new working directory"
        System.getProperty( 'user.dir' ).equals( oldWorkingDir )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config, x, x
    //           - redirectOutFilePath
    //           - redirectOutType
    // ********************************************************


    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for valid task when redirecting output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) for invalid task when redirecting output to file"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-j' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '1'"
        resultMap.exitValue.equals( '1' )

        and: "output file has no contents"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( '' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' contains error message"
        resultMap.err.contains( 'invalid option' )
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) overwrites existing file content when output redirected to file with option 'overwrite'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'overwrite' )

        String originalContent = "original content"

        Files.write( Path.of( outFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) appends to existing file when output redirected to file with option 'append'"( ) {

        given: "valid command to run and existing file"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'append' )

        String originalContent = "original content"

        Files.write( Path.of( outFilePath ), originalContent.getBytes( ) )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "output file has contents 'user'"
        Files.readString( Path.of( outFilePath ) ).trim( ).equals( originalContent + 'user' )

        and: "map key 'out' is not present"
        resultMap.containsKey( 'out' ) == false

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws error when given redirectOutFilePath but no redirectOutType"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws error when given redirectOutType but no redirectOutFilePath"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) throws error when given illegal redirectOutType value"( ) {

        given: "valid command to run"
        List<String> task = Arrays.asList( 'id', '-un' )
        Map<String,String> cfg = new HashMap<String,String>( )
        String outFilePath = tempDir.toString( ) + File.separator + 'test.txt'
        cfg.put( 'redirectOutFilePath', outFilePath )
        cfg.put( 'redirectOutType', 'illegal-value' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "exception thrown"
        thrown IllegalArgumentException
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, config, x, x
    //           - redirectErrToOut
    //           - redirectErrFilePath
    //           - redirectErrType
    // ********************************************************

    // test that can't do these things... throw exception?


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, x, addEnv x
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'addEnv' is empty"( ) {

        given: "command to execute to get the current username, with empty addEnv"
        List<String> task = Arrays.asList( 'whoami' )
        Map<String,String> addEnv = new HashMap<String,String>( )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with one key-value pair in 'addEnv'"( ) {

        given: "command to execute to get all env vars"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET', 'HOWDY' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the env var in map key 'out'"
        resultMap.out.contains( 'GREET=HOWDY' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with more than one key-value pair in 'addEnv'"( ) {

        given: "command to execute to get all env vars"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET1', 'HI' )
        addEnv.put( 'GREET2', 'HEY' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the env var in map key 'out'"
        resultMap.out.contains( 'GREET1=HI' )
        resultMap.out.contains( 'GREET2=HEY' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }


    // ********************************************************
    // execExceptionOnTaskFail
    //      - x, x, x, removeEnv
    // ********************************************************

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly when 'removeEnv' is empty"( ) {

        given: "command to execute to get the current username, with empty removeEnv"
        List<String> task = Arrays.asList( 'whoami' )
        List<String> removeEnv = new ArrayList<String>( )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns the correct username from executing the command in map key 'out'"
        String usernameExpected = System.properties[ 'user.name' ]
        usernameExpected.equals( resultMap.out )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with one env var in 'removeEnv' to remove"( ) {

        given: "command to execute to get all env vars, and a List of env vars to remove"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET', 'HOWDY' )
        List<String> removeEnv = new ArrayList<String>( )
        removeEnv.add( 'GREET' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns in 'out' the environment variables without the 'GREET' env var"
        !resultMap.out.contains( 'GREET=' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }

    def "execExceptionOnTaskFail(List<String> task, Map<String,String> config, Map<String,String> addEnv, List<String> removeEnv) returns correctly with more than one env var in 'removeEnv' to remove"( ) {

        given: "command to execute to get all env vars, and a List of env vars to remove"
        List<String> task = Arrays.asList( 'printenv' )
        Map<String,String> addEnv = new HashMap<String,String>( )
        addEnv.put( 'GREET1', 'HI' )
        addEnv.put( 'GREET2', 'HEY' )
        List<String> removeEnv = new ArrayList<String>( )
        removeEnv.add( 'GREET1' )
        removeEnv.add( 'GREET2' )

        when: "execute the command"
        String result = Exec.execExceptionOnTaskFail( task, null, null, null )

        then: "map key 'exitValue' is '0'"
        resultMap.exitValue.equals( '0' )

        and: "returns in 'out' the environment variables without the 'GREET1' and 'GREET2' env vars"
        !resultMap.out.contains( 'GREET1=' )
        !resultMap.out.contains( 'GREET2=' )

        and: "map key 'err' is not present"
        resultMap.containsKey( 'err' ) == false
    }
    */

}