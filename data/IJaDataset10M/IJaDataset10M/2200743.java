package com.ibm.wala.util.math;

/**
 */
public class LongUtil {

    public static long pack(int hi, int lo) {
        return ((long) hi << 32) | lo;
    }
}
