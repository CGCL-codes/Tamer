package jvc.util;

public class CheckUtils {

    public static boolean isInteger(String value) {
        return (FormatUtils.formatInt(value) != null);
    }

    public static boolean isDouble(String value) {
        return (FormatUtils.formatDouble(value) != null);
    }

    public static boolean isBlankOrNull(String value) {
        return ((value == null) || (value.trim().length() == 0));
    }

    public static boolean isDate(String value) {
        return (FormatUtils.formatDate(value) != null);
    }

    public static boolean isClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
