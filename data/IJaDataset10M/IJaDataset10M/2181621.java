package us.harward.commons.util;

public class Equals {

    public static boolean allowNull(final Object o1, final Object o2) {
        if (o1 == null && o2 == null) return true; else if (o1 == null || o2 == null) return false; else return o1.equals(o2);
    }

    public static boolean disallowNull(final Object o1, final Object o2) {
        if (o1 == null || o2 == null) return false; else return o1.equals(o2);
    }
}
