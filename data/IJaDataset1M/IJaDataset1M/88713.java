package net.persister.util;

/**
 * @author Park, chanwook
 *
 */
public class StringUtils {

    public static String castStringWithTrim(Object object) {
        String stringValue = (String) object;
        return stringValue.trim();
    }

    public static boolean castBoolean(String trueOfFalse) {
        return Boolean.parseBoolean(trueOfFalse);
    }

    public static String toUpperCase(String stream, int length) {
        String wantPart = stream.substring(0, length).toUpperCase();
        return wantPart + stream.substring(length);
    }

    public static boolean isEmpty(String string) {
        return string == null || string.equals("") ? true : false;
    }
}
