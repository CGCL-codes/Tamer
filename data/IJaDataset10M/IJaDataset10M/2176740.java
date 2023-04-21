package com.tapina.robe.swi.clib.stdio;

import com.tapina.robe.runtime.Environment;
import com.tapina.robe.swi.clib.Stub;

/**
 * This function writes the string pointed to by a1, followed by a newline, to stdout .
 * The null terminator is not written.
 */
public class PutS extends Stub {

    public void executeStub(Environment environment) {
        System.out.println(environment.getMemoryMap().getString0(environment.getCpu().R[0]));
    }
}
