package com;

/**
 * This is a simple class to handle array copies
 * (because java is irritating and doesn't use pointer math)
 */
public class datacopy {

    /**
	 * This is the function to copy arrays starting at an offset
	 * until one of the input arrays ends.
	 *
	 * @param from the array to copy from
	 * @param to the destination array
	 * @param offset the address of the first element of from[] to copy
	 */
    public static void copy_int_array(int[] from, int[] to, int offset) {
        for (int i = offset; (i < from.length) && ((i - offset) < to.length); i++) to[i - offset] = from[i];
    }

    /**
	 * This is the function to copy arrays starting at an offset
	 * until one of the input arrays ends.
	 *
	 * @param from the array to copy from
	 * @param to the destination array
	 * @param offset the address of the first element of from[] to copy
	 */
    public static void copy_byte_array(byte[] from, byte[] to, int offset) {
        for (int i = offset; (i < from.length) && ((i - offset) < to.length); i++) to[i - offset] = from[i];
    }
}
