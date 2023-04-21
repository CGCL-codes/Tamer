package net.ko.utils;

import java.sql.Date;

public class KString {

    public static String capitalizeFirstLetter(String value) {
        if (value == null) {
            return null;
        }
        if (value.length() == 0) {
            return value;
        }
        StringBuilder result = new StringBuilder(value);
        result.replace(0, 1, result.substring(0, 1).toUpperCase());
        return result.toString();
    }

    public static boolean isBoolean(String value) {
        return isBooleanTrue(value) || isBooleanFalse(value);
    }

    public static boolean isBooleanTrue(String value) {
        boolean result = false;
        if (value != null) {
            result = value.equalsIgnoreCase("1") || value.equalsIgnoreCase("true");
        }
        return result;
    }

    public static boolean isBooleanFalse(String value) {
        boolean result = false;
        if (value != null) {
            result = value.equalsIgnoreCase("0") || value.equalsIgnoreCase("false");
        }
        return result;
    }

    public static Object convert(String str) {
        Object result = str;
        try {
            result = Integer.parseInt(str);
        } catch (NumberFormatException e1) {
            try {
                result = Float.parseFloat(str);
            } catch (NumberFormatException e2) {
                try {
                    result = Date.valueOf(str);
                } catch (Exception e3) {
                }
            }
        }
        return result;
    }

    public static String addSlashes(String s) {
        if (s != null) {
            s = s.replaceAll("\\\\", "\\\\\\\\");
            s = s.replaceAll("\\n", "\\\\n");
            s = s.replaceAll("\\r", "\\\\r");
            s = s.replaceAll("\\00", "\\\\0");
            s = s.replaceAll("'", "\\\\'");
        }
        return s;
    }

    public static String htmlSpecialChars(String s) {
        if (s != null) {
            s = s.replaceAll("&", "&amp;");
            s = s.replaceAll("'", "&apos;");
            s = s.replaceAll("\"", "&quot;");
            s = s.replaceAll("<", "&lt;");
            s = s.replaceAll(">", "&gt;");
            s = s.replaceAll("é", "&eacute;");
        }
        return s;
    }

    public static String jsQuote(String s) {
        if (s != null) {
            s = s.replaceAll("'", "\'");
            s = s.replaceAll("\"", "\\\"");
        }
        return s;
    }

    public static Integer[] toArrayOfInt(String value, String separator) {
        String[] values = value.split(separator);
        Integer[] result = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                result[i] = Integer.valueOf(values[i]);
            } catch (Exception e) {
                result = null;
            }
        }
        return result;
    }

    public static String cleanStringArray(String value, String separator) {
        String result = value.trim();
        result = result.replaceAll("^" + separator + "|" + separator + "$", "");
        result = result.replaceAll(separator + separator, separator);
        return result;
    }

    public static String cleanJSONValue(String value) {
        return value.replaceAll("^\\s*\'|^\\s*\"|\"\\s*$|\'\\s*$", "");
    }

    public static String cleanJSONString(String value) {
        return value.replaceAll("^\\{|\\}$", "");
    }

    public static String getBefore(String input, String value) {
        String result = input;
        int pos = input.indexOf(value);
        if (pos != -1) result = input.substring(0, pos);
        return result;
    }

    public static String getAfter(String input, String value) {
        String result = input;
        int pos = input.indexOf(value);
        if (pos != -1) result = input.substring(pos + 1);
        return result;
    }
}
