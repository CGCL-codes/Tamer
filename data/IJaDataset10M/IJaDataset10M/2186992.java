package com.sleepycat.je.tree;

/**
 * Miscellaneous Tree utilities.
 */
public class TreeUtils {

    private static final String SPACES = "                                " + "                                " + "                                " + "                                ";

    /**
     * For tree dumper.
     */
    public static String indent(int nSpaces) {
        return SPACES.substring(0, nSpaces);
    }
}
