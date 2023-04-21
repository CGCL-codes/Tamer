package org.mgkFramework.util;

public class MgkStringUtil {

    public static boolean isBlank(String val) {
        if (val == null) {
            return true;
        }
        return val.isEmpty();
    }

    public static String NVL(String val) {
        return NVL(val, "NULL");
    }

    public static String NVL(String val, String defVal) {
        if (val == null) {
            return defVal;
        }
        return val;
    }
}
