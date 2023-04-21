package com.nuts.services;

public class ParsingUtils {

    public static Float safeFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static Double safeDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer safeInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static String emptyNull(String s) {
        if (s != null && s.isEmpty()) return null;
        return s;
    }

    public static String quote(String aText) {
        String QUOTE = "'";
        return QUOTE + aText + QUOTE;
    }
}
