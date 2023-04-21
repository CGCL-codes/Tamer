package com.ibm.wala.util.collections;

import java.util.Iterator;

/**
 * utilities dealing with Iterators
 */
public class IteratorUtil {

    /**
   * @return true iff the Iterator returns some elements which equals() the object o
   */
    public static <T> boolean contains(Iterator<? extends T> it, T o) {
        if (it == null) {
            throw new IllegalArgumentException("null it");
        }
        while (it.hasNext()) {
            if (o.equals(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static final <T> int count(Iterator<T> it) throws IllegalArgumentException {
        if (it == null) {
            throw new IllegalArgumentException("it == null");
        }
        int count = 0;
        while (it.hasNext()) {
            it.next();
            count++;
        }
        return count;
    }
}
