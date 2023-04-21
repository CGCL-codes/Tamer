package org.apache.ws.jaxme.junit;

public class PrintParse {

    /** Converts a boolean value into 0 (false) or 1 (true).
     */
    public static String printBoolean(boolean pValue) {
        return pValue ? "1" : "0";
    }

    /** Converts the values 0 (false) or 1 (true) into a
     * boolean value.
     */
    public static boolean parseBoolean(String pValue) {
        if ("1".equals(pValue)) {
            return true;
        } else if ("0".equals(pValue)) {
            return false;
        } else {
            throw new IllegalArgumentException("Illegal argument: " + pValue);
        }
    }
}
