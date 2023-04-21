package com.tapina.robe.swi.clib.roslib;

/**
 * This function calls a SWI , whose number is given in a1. The registers R0-R9 are set to and returned in the values in a2.
 * If an error occurs it is reported by the operating system and your program will halt execution.
 * The error will be thrown as a RuntimeException.
 */
public class SWI extends SWIX {

    int getSWINumber(int[] R) {
        return R[0];
    }
}
