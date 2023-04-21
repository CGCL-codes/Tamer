package com.neotis.util;

/**
 * Helper utility for string handling.
 *
 * @author Stephan J. Schmidt
 * @version $Id: StringUtil.java 49 2002-06-28 11:47:56Z leo $
 */
public class StringUtil {

    public static String plural(int i, String s1, String s2) {
        StringBuffer buffer = new StringBuffer();
        return plural(buffer, i, s1, s2).toString();
    }

    public static String plural(int i, String s) {
        StringBuffer buffer = new StringBuffer();
        return plural(buffer, i, s).toString();
    }

    public static StringBuffer plural(StringBuffer buffer, int i, String s1, String s2) {
        buffer.append(i);
        buffer.append(" ");
        if (i > 1 || i == 0) {
            buffer.append(s1);
        } else {
            buffer.append(s2);
        }
        return buffer;
    }

    public static StringBuffer plural(StringBuffer buffer, int i, String s) {
        buffer.append(i);
        buffer.append(" ");
        buffer.append(s);
        if (i > 1 || i == 0) {
            buffer.append("s");
        }
        return buffer;
    }
}
