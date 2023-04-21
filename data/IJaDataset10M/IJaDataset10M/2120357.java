package com.funambol.syncclient.common;

import com.funambol.syncclient.util.StaticDataHelper;

/**
 * Utility class that groups string manipulation functions.
 *
 */
public class StringTools {

    /**
     * <p>Escapes the characters in a <code>String</code> using XML entities.</p>
     *
     *
     * <p>Supports only the four basic XML entities (gt, lt, quot, amp).
     * Does not support DTDs or external entities.</p>
     *
     * @param str  the <code>String</code> to escape, may be null
     * @return a new escaped <code>String</code>, <code>null</code> if null string input
     * @see #unescapeXml(java.lang.String)
     **/
    public static String escapeXml(String str) {
        if (str == null) {
            return null;
        }
        return Entities.XML.escape(str);
    }

    /**
     * <p>Unescapes a string containing XML entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes.</p>
     *
     * <p>Supports only the four basic XML entities (gt, lt, quot, amp).
     * Does not support DTDs or external entities.</p>
     *
     * @param str  the <code>String</code> to unescape, may be null
     * @return a new unescaped <code>String</code>, <code>null</code> if null string input
     * @see #escapeXml(String)
     **/
    public static String unescapeXml(String str) {
        if (str == null) {
            return null;
        }
        return Entities.XML.unescape(str);
    }

    public static String QPdecode(String qp) {
        int i;
        StringBuffer ret = new StringBuffer();
        for (i = 0; i < qp.length(); i++) {
            if (qp.charAt(i) == '=') {
                if (qp.length() - i > 2) {
                    String code = qp.substring(i + 1, i + 3).trim();
                    if (code.equals("\r\n")) {
                        i += 2;
                        continue;
                    } else {
                        try {
                            char c = (char) (Byte.parseByte(code.toLowerCase(), 16));
                            ret.append(c);
                            i += 2;
                            continue;
                        } catch (NumberFormatException nfe) {
                            StaticDataHelper.log("QPDecode: Invalid sequence =" + code);
                        }
                    }
                }
            }
            ret.append(qp.charAt(i));
        }
        return ret.toString();
    }

    public static String dump(String[] array) {
        StringBuffer buffer = new StringBuffer("Content of array " + array.toString() + ":\n");
        for (int i = 0; i < array.length; i++) {
            buffer.append(array[i] + "\n");
        }
        return buffer.toString();
    }
}
