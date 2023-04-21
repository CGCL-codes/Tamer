package de.mogwai.kias.util;

public class EncodingUtils {

    private static char[] hex = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static final int HIGH_MULTIPLIER = 16;

    private static final int HIGH_AND_VALUE = 15;

    public static String getBase64Encoded(String aValue) {
        StringBuilder theBuilder = new StringBuilder();
        for (int i = 0; i < aValue.length(); i++) {
            char theChar = aValue.charAt(i);
            int theLow = theChar & HIGH_AND_VALUE;
            int theHigh = theChar >> 4;
            theBuilder.append(hex[theLow]);
            theBuilder.append(hex[theHigh]);
        }
        return theBuilder.toString();
    }

    private static int hexToInt(char aChar) {
        for (int i = 0; i < hex.length; i++) {
            if (hex[i] == aChar) return i;
        }
        return -1;
    }

    public static String URLdecode(String aCode) {
        StringBuilder theBuilder = new StringBuilder();
        int i = 0;
        while (i < aCode.length()) {
            char aChar = aCode.charAt(i++);
            switch(aChar) {
                case '%':
                    {
                        int theHigh = hexToInt(Character.toUpperCase(aCode.charAt(i++)));
                        int theLower = hexToInt(Character.toUpperCase(aCode.charAt(i++)));
                        theBuilder.append((char) (theHigh * HIGH_MULTIPLIER + theLower));
                        break;
                    }
                default:
                    {
                        theBuilder.append(aChar);
                        break;
                    }
            }
        }
        return theBuilder.toString();
    }

    public static String decodeBase64(String aValue) {
        StringBuffer theBuffer = new StringBuffer();
        int i = 0;
        while (i < aValue.length()) {
            int theLow = hexToInt(aValue.charAt(i++));
            int theHigh = hexToInt(aValue.charAt(i++));
            theBuffer.append((char) (theHigh * HIGH_MULTIPLIER + theLow));
        }
        return theBuffer.toString();
    }
}
