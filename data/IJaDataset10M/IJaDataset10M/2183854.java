package com.definity.toolkit.util;

public class StringUtils {

    private static String normal = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";

    private static String plain = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";

    private static char[] table;

    static {
        table = new char[256];
        for (int i = 0; i < table.length; ++i) {
            table[i] = (char) i;
        }
        for (int i = 0; i < normal.length(); ++i) {
            table[normal.charAt(i)] = plain.charAt(i);
        }
    }

    public static String toUrl(String s) {
        if (s == null) return null;
        return replaceSpecialCharacers(s).replaceAll("\\W", "-").toLowerCase();
    }

    public static String toEnum(String s) {
        if (s == null) return null;
        return replaceSpecialCharacers(s).replaceAll("\\W", "_").toUpperCase();
    }

    public static String replaceSpecialCharacers(final String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            if (ch < 256) {
                sb.append(table[ch]);
            } else if (ch == '_') {
                sb.append(ch);
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
