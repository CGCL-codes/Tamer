package org.bacula.jbacula;

/**
 * @author phil
 *
 */
public class Utilities {

    static String unquote(String str) {
        String value = str;
        if ((value.length() > 0) && ('"' == value.charAt(0)) && ('"' == value.charAt(value.length() - 1))) {
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }

    static String quote(String str) {
        return "\"" + str + "\"";
    }
}
