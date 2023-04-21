package com.ibm.wala.util.collections;

import java.util.Iterator;

/**
 * Converts an {@link Iterator} to an {@link Iterable}.
 */
public class Iterator2Iterable<T> implements Iterable<T> {

    private final Iterator<T> iter;

    public static <T> Iterator2Iterable<T> make(Iterator<T> iter) {
        return new Iterator2Iterable<T>(iter);
    }

    public Iterator2Iterable(Iterator<T> iter) {
        this.iter = iter;
    }

    public Iterator<T> iterator() {
        return iter;
    }
}
