package org.guiceyfruit.support;

/** @version $Revision: 1.1 $ */
public class Comparators {

    /** A helper method for comparing objects for equality while handling nulls */
    public static boolean equal(Object a, Object b) {
        if (a == b) {
            return true;
        }
        return a != null && b != null && a.equals(b);
    }
}
