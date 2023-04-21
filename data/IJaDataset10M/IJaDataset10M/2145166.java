package com.volantis.mcs.utilities;

/**
 * A set of utility methods for doing whitespace handling.
 */
public class WhitespaceUtilities {

    /**
     * Get the index of the first non white space character in the buffer.
     */
    public static int getFirstNonWhitespaceIndex(StringBuffer buffer) {
        int length = buffer.length();
        for (int i = 0; i < length; i += 1) {
            char c = buffer.charAt(i);
            if (!Character.isWhitespace(c)) {
                return i;
            }
        }
        return length;
    }

    /**
     * Get the index of the first non white space character in the section
     * of the string defined by the offset and length.
     */
    public static int getFirstNonWhitespaceIndex(String string, int off, int len) {
        int end = off + len;
        for (int i = off; i < end; i += 1) {
            char c = string.charAt(i);
            if (!Character.isWhitespace(c)) {
                return i;
            }
        }
        return end;
    }

    /**
     * Get the index of the first white space character in the section
     * of the string defined by the offset and length.
     */
    public static int getFirstWhitespaceIndex(String string, int off, int len) {
        int end = off + len;
        for (int i = off; i < end; i += 1) {
            char c = string.charAt(i);
            if (Character.isWhitespace(c)) {
                return i;
            }
        }
        return end;
    }

    /**
     * Get the index of the first non white space character in the section
     * of the character array defined by the offset and length.
     */
    public static int getFirstNonWhitespaceIndex(char[] cbuf, int off, int len) {
        int end = off + len;
        for (int i = off; i < end; i += 1) {
            char c = cbuf[i];
            if (!Character.isWhitespace(c)) {
                return i;
            }
        }
        return end;
    }

    /**
     * Get the index of the first white space character in the section
     * of the character array defined by the offset and length.
     */
    public static int getFirstWhitespaceIndex(char[] cbuf, int off, int len) {
        int end = off + len;
        for (int i = off; i < end; i += 1) {
            char c = cbuf[i];
            if (Character.isWhitespace(c)) {
                return i;
            }
        }
        return end;
    }

    /**
     * Get the index of the last non white space character in the section
     * of the character array defined by the offset and length.
     *
     * @param cbuf the character array whose last non-whitespace index to find
     * @param off  the offset from the start of cbuf to stop searching for non-
     *             whitespace characters.
     * @param len  the number of characters after off to begin the search for
     *             non-whitespace characters.
     */
    public static int getLastNonWhitespaceIndex(char[] cbuf, int off, int len) {
        int end = off + len - 1;
        for (int i = end; i < cbuf.length && i >= off; i--) {
            char c = cbuf[i];
            if (!Character.isWhitespace(c)) {
                return i;
            }
        }
        return off;
    }

    /**
     * Get the index of the first non white space character in a
     * ReusableStringBuffer.
     */
    public static int getFirstNonWhitespaceIndex(ReusableStringBuffer rsb) {
        return getFirstNonWhitespaceIndex(rsb.getChars(), 0, rsb.length());
    }

    /**
     * Get the index of the first non white space character in a
     * ReusableStringBuffer within the specified range.
     */
    public static int getFirstNonWhitespaceIndex(ReusableStringBuffer rsb, int off, int len) {
        int length = rsb.length();
        if (off > length) {
            return len;
        }
        if (off + len > length) {
            len = length - off;
        }
        return getFirstNonWhitespaceIndex(rsb.getChars(), off, len);
    }

    /**
     * Get the index of the last non white space character in a
     * ReusableStringBuffer.
     */
    public static int getLastNonWhitespaceIndex(ReusableStringBuffer rsb) {
        return getLastNonWhitespaceIndex(rsb.getChars(), 0, rsb.length());
    }

    /**
     * Checks whether the buffer is only whitespace
     *
     * @return True if the buffer is all whitespace and false otherwise.
     */
    public static boolean isWhitespace(StringBuffer buffer) {
        int length = buffer.length();
        for (int i = 0; i < length; i += 1) {
            char c = buffer.charAt(i);
            if (!Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the string is only whitespace
     *
     * @return True if the string is all whitespace and false otherwise.
     */
    public static boolean isWhitespace(String string, int off, int len) {
        int end = off + len;
        for (int i = off; i < end; i += 1) {
            char c = string.charAt(i);
            if (!Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the character array is only whitespace
     *
     * @return True if the character array is all whitespace and false otherwise.
     */
    public static boolean isWhitespace(char[] cbuf, int off, int len) {
        int end = off + len;
        for (int i = off; i < end; i += 1) {
            char c = cbuf[i];
            if (!Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the character array is only whitespace
     *
     * @return True if the character array is all whitespace and false otherwise.
     */
    public static boolean isWhitespace(ReusableStringBuffer rsb) {
        return isWhitespace(rsb.getChars(), 0, rsb.length());
    }
}
