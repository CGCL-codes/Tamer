package org.softee.util;

public class Preconditions {

    private Preconditions() {
    }

    /**
     * An assertion method that makes null validation more fluent
     * @param <E> The type of elements
     * @param obj an Object
     * @return {@code obj}
     * @throws NullPointerException if {@code obj} is null
     */
    public static <E> E notNull(E obj) {
        return notNull(obj, null);
    }

    /**
     * An assertion method that makes null validation more fluent
     * @param <E> The type of elements
     * @param obj an Object
     * @param msg a message that is reported in the exception
     * @return {@code obj}
     * @throws NullPointerException if {@code obj} is null
     */
    public static <E> E notNull(E obj, String msg) {
        if (obj == null) {
            throw (msg == null) ? new NullPointerException() : new NullPointerException(msg);
        }
        return obj;
    }

    public static void assertTrue(boolean b) {
        assertTrue(b, null);
    }

    public static void assertTrue(boolean b, String msg) {
        if (!b) {
            throw (msg == null) ? new IllegalArgumentException() : new IllegalArgumentException(msg);
        }
    }

    public static String notEmpty(String s) {
        return notEmpty(s, null);
    }

    public static String notEmpty(String s, String msg) {
        notNull(s, msg);
        if (s.isEmpty()) {
            throw (msg == null) ? new NullPointerException("empty") : new NullPointerException(msg);
        }
        return s;
    }
}
