package org.lobobrowser.util;

/**
 * @author J. H. S.
 */
public class Diagnostics {

    /**
     * 
     */
    private Diagnostics() {
        super();
    }

    public static void Assert(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
