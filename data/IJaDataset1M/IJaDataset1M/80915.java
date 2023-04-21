package org.sinaxe.xupdate;

public class XUpdateUtil {

    static String trimValue(String value) {
        if (value == null) return null;
        String result = value.trim();
        int length = result.length();
        if (length > 1) {
            if ((result.charAt(0) == '"' && result.charAt(length - 1) == '"') || (result.charAt(0) == '\'' && result.charAt(length - 1) == '\'')) result = result.substring(1, length - 1);
        }
        return result;
    }
}
