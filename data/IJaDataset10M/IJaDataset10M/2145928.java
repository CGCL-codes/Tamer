package org.zee3.proselyte.util;

/**
 * 
 * @author Michael Zazzali
 */
public class StringConverter {

    public static String convert(Object object) {
        if (null != object) return object.toString(); else return "";
    }

    public static String convert(String string) {
        return string;
    }

    public static String convert(Integer integer) {
        return integer.toString();
    }

    public static String convert(int i) {
        return Integer.toString(i);
    }

    public static String convert(double d) {
        return Double.toString(d);
    }
}
