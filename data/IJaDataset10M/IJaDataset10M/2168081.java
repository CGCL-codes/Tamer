package com.halware.eclipseutil.util;

/**
 * Static methods for handling potential null values.
 * @author Mike
 */
public class NullUtil {

    /**
	 * Return whether the two objects are equals in a null-safe manner.
	 * <br>Contrary to standard null behaviour, this report two nulls as equal.
	 * @param obj1
	 * @param obj2
	 * @return
	 */
    public static boolean nullSafeEquals(final Object obj1, final Object obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return (obj2 == null);
        }
    }

    private NullUtil() {
        super();
    }
}
