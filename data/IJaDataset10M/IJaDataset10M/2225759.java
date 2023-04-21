package com.netime.commons.standard.lang;

public final class ArrayUtils {

    public static final int[] convertStringArraytoIntArray(String[] sarray) throws Exception {
        if (sarray != null) {
            int intarray[] = new int[sarray.length];
            for (int i = 0; i < sarray.length; i++) {
                intarray[i] = Integer.parseInt(sarray[i]);
            }
            return intarray;
        }
        return null;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }
}
