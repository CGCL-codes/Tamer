package net.sourceforge.hlm.util.iterator;

import java.util.*;

public class SingleItemIterable<T> implements Iterable<T> {

    public SingleItemIterable(T item) {
        this.item = item;
    }

    public Iterator<T> iterator() {
        return new SingleItemIterator<T>(this.item);
    }

    private T item;
}
