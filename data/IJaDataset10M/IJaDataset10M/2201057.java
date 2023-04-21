package net.sf.lightbound.util;

import java.util.Iterator;

public class IterableImpl<T> implements Iterable<T> {

    private final Iterator<T> iterator;

    public IterableImpl(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public Iterator<T> iterator() {
        return iterator;
    }
}
