package com.volantis.mcs.runtime.configuration.project;

/**
 * Helper class used when de/serializing values to and from XML using JIBX.
 */
public class DeSerializer {

    /**
     * Convert an unlimited non negative integer to a String.
     *
     * @param integer The unlimited non negative integer.
     * @return The string representation.
     */
    public static String unlimitedNonNegativeToString(Integer integer) {
        return integer.intValue() == Integer.MAX_VALUE ? "unlimited" : String.valueOf(integer);
    }

    /**
     * Convert a string to an unlimited non negative Integer.
     *
     * @param string The string.
     * @return The integer representation.
     */
    public static Integer stringToUnlimitedNonNegative(String string) {
        if (string.equals("unlimited")) {
            return new Integer(Integer.MAX_VALUE);
        } else {
            return new Integer(Integer.parseInt(string));
        }
    }
}
