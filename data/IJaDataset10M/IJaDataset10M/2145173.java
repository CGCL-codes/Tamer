package org.jnav.util;

import java.util.Vector;

/**
 *
 * @author Matthias Lohr <mail@matthias-lohr.net>
 */
public abstract class StringUtils {

    private static final String urlAllowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String[] explode(String glue, String str) {
        Vector parts = new Vector();
        int aPos = 0;
        int foundPos;
        int glueLen = glue.length();
        while ((foundPos = str.indexOf(str, aPos)) != -1) {
            parts.addElement(str.substring(aPos, foundPos));
            aPos = foundPos + glueLen;
        }
        int partsSize = parts.size();
        String[] result = new String[partsSize];
        for (int i = 0; i < partsSize; i++) {
            result[i] = (String) parts.elementAt(i);
        }
        return result;
    }

    public static String implode(String glue, String[] strs) {
        StringBuffer buffer = new StringBuffer("");
        int count = strs.length;
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                buffer.append(glue);
            }
            buffer.append(strs[i]);
        }
        return buffer.toString();
    }

    public static String implode(String glue, Vector strs) {
        StringBuffer buffer = new StringBuffer("");
        int count = strs.size();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                buffer.append(glue);
            }
            buffer.append(strs.elementAt(i));
        }
        return buffer.toString();
    }

    public static String rawurlencode(String url) {
        char[] allowedChars = StringUtils.urlAllowed.toCharArray();
        StringBuffer buffer = new StringBuffer();
        int urlLen = url.length();
        char aChar;
        for (int i = 0; i < urlLen; i++) {
            aChar = url.charAt(i);
            if (ArrayUtils.inArray(aChar, allowedChars)) {
                buffer.append(aChar);
            } else {
                buffer.append("%" + Integer.toHexString(aChar));
            }
        }
        return buffer.toString();
    }

    public static String urlencode(String url) {
        char[] allowedChars = StringUtils.urlAllowed.toCharArray();
        StringBuffer buffer = new StringBuffer();
        int urlLen = url.length();
        char aChar;
        for (int i = 0; i < urlLen; i++) {
            aChar = url.charAt(i);
            if (aChar == ' ') {
                buffer.append('+');
            } else {
                if (ArrayUtils.inArray(aChar, allowedChars)) {
                    buffer.append(aChar);
                } else {
                    buffer.append("%" + Integer.toHexString(aChar));
                }
            }
        }
        return buffer.toString();
    }
}
