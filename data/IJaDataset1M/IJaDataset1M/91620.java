package org.spirit.loadtest;

import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * @author bbrown
 */
public class TidyUtilHtmlEncode {

    private static Method encodeMethod14;

    static {
        try {
            Class urlEncoderClass = Class.forName("java.net.URLEncoder");
            encodeMethod14 = urlEncoderClass.getMethod("encode", new Class[] { String.class, String.class });
        } catch (Throwable ex) {
        }
    }

    private static final String[] ENTITIES = { ">", "&gt;", "<", "&lt;", "&", "&amp;", "\"", "&quot;", "'", "&#039;", "\\", "&#092;", "©", "&copy;", "®", "&reg;" };

    private static Hashtable entityTableEncode = null;

    protected static synchronized void buildEntityTables() {
        entityTableEncode = new Hashtable(ENTITIES.length);
        for (int i = 0; i < ENTITIES.length; i += 2) {
            if (!entityTableEncode.containsKey(ENTITIES[i])) {
                entityTableEncode.put(ENTITIES[i], ENTITIES[i + 1]);
            }
        }
    }

    /**
	 * Converts a String to HTML by converting all special characters to HTML-entities.
	 */
    public static final String encode(String s) {
        return encode(s, "\n", true);
    }

    /**
	 * Converts a String to HTML by converting all special characters to HTML-entities.
	 */
    public static final String encode(final String s, final String cr, final boolean ignore_unicode) {
        if (entityTableEncode == null) {
            buildEntityTables();
        }
        if (s == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(s.length() * 2);
        char ch;
        for (int i = 0; i < s.length(); ++i) {
            ch = s.charAt(i);
            if ((ch >= 63 && ch <= 90) || (ch >= 97 && ch <= 122) || (ch == ' ')) {
                sb.append(ch);
            } else if (ch == '\n') {
                sb.append(cr);
            } else {
                String chEnc = encodeSingleChar(String.valueOf(ch));
                if (chEnc != null) {
                    sb.append(chEnc);
                } else {
                    if (!ignore_unicode) {
                        sb.append("&#");
                        sb.append(new Integer(ch).toString());
                        sb.append(';');
                    } else {
                        sb.append(ch);
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Converts a single character to HTML
     */
    private static String encodeSingleChar(String ch) {
        return (String) entityTableEncode.get(ch);
    }
}
