package org.dbgen.support;

/**
 * Common tasks operated on a String object.
 *
 */
public class StringHelper {

    /**
   * Escape single quote from a string.
   * @return java.lang.String
   * @param s java.lang.String
   */
    public static String escape(String s) {
        if (s == null) {
            return "";
        }
        int len = s.length();
        StringBuffer buf = new StringBuffer(len * 2);
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == '\'') {
                buf.append("''");
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
   * Check if a character is an alpahbet.
   * @return True if the character is an alphabet.
   * @param c The character to check.
   */
    public static boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
   * Check if a string is blank (either spaces, newlines, or tabs.)
   * @return True if the string is blank.
   * @param s The string to check.
   */
    public static final boolean isBlank(String s) {
        int len = s.length();
        if (len == 0) return true;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c != ' ' && c != '\n' && c != '\t') return false;
        }
        return true;
    }

    /**
   * Check if a character is a number.
   * @return True if the character is a number.
   * @param c The character to check.
   */
    public static boolean isNumeric(char c) {
        return (c >= '0' && c <= '9');
    }
}
