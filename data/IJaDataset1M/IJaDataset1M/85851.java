package org.simplerrd.impl.util;

public class StringHelper {

    private StringHelper() {
    }

    /**
	 * Converts the String to a space padded array.
	 * The string will be truncated if it exceeds the specified length.
	 * @param value
	 * @param len
	 * @return
	 */
    public static char[] toCharArray(String value, int len) {
        return toCharArray(new char[len], value);
    }

    public static char[] toCharArray(char dest[], String value) {
        int offset = 0;
        int len = dest.length;
        char result[] = dest;
        if (value != null) {
            char a[] = value.toCharArray();
            offset = a.length;
            System.arraycopy(a, 0, result, 0, Math.min(len, offset));
        }
        for (; offset < result.length; offset++) {
            result[offset] = ' ';
        }
        return result;
    }

    public static String trimEnd(String value) {
        if (value != null) {
            int lastChar = value.length();
            while (lastChar > 0 && value.charAt(lastChar - 1) == ' ') {
                lastChar--;
            }
            value = value.substring(0, lastChar);
        }
        return value;
    }

    /**
	 * 
	 * @param ch
	 * @return
	 */
    public static String fromCharArray(char ch[]) {
        return trimEnd(new String(ch));
    }
}
