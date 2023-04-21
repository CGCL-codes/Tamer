package org.ibex.util;

import java.io.File;
import java.io.UnsupportedEncodingException;

/** provides commands to urlencode and urldecode strings, making sure
 *  to escape sequences which have special meanings in filenames */
public class FileNameEncoder {

    private static final char[] hex = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String encode(String s) {
        StringBuffer sb = new StringBuffer();
        try {
            byte[] b = s.getBytes("UTF-8");
            for (int i = 0; i < b.length; i++) {
                char c = (char) (b[i] & 0xff);
                if (c == File.separatorChar || c < 32 || c > 126 || c == '%' || (i == 0 && c == '.')) sb.append("%" + hex[(b[i] & 0xf0) >> 8] + hex[b[i] & 0xf]); else sb.append(c);
            }
            return sb.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new Error("this should never happen; Java spec mandates UTF-8 support");
        }
    }

    public static String decode(String s) {
        StringBuffer sb = new StringBuffer();
        byte[] b = new byte[s.length() * 2];
        int bytes = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '%') b[bytes++] = (byte) Integer.parseInt(("" + s.charAt(++i) + s.charAt(++i)), 16); else b[bytes++] = (byte) c;
        }
        try {
            return new String(b, 0, bytes, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            throw new Error("this should never happen; Java spec mandates UTF-8 support");
        }
    }
}
