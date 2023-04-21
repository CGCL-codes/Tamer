package net.sf.cryptoluggage.util;

/**
 * Get hexadecimal representation of data.
 *
 * @author Miguel Hernández <mhernandez314@gmail.com>
 */
public class HexRepresentation {

    private static char[] lowerCaseConversionTable = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static char[] upperCaseConversionTable = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Gets the hexadecimal representation of an array of bytes.
     * 
     * @param data data to be transformed
     * @param lowerCase if true, output will be in lower case
     * @return the hexadecimal representation of an array of bytes.
     */
    public static String get(byte[] data, boolean lowerCase) {
        StringBuilder seqBuilder = new StringBuilder();
        char[] charTable;
        if (lowerCase) {
            charTable = lowerCaseConversionTable;
        } else {
            charTable = upperCaseConversionTable;
        }
        for (int i = 0; i < data.length; i++) {
            seqBuilder.append(charTable[(data[i] & 0xf0) >>> 4]);
            seqBuilder.append(charTable[data[i] & 0x0f]);
        }
        return seqBuilder.toString();
    }
}
