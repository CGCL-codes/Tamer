package org.lindenb.util.iterator;

import java.util.NoSuchElementException;

public class ArrayIterator<T> extends AbstractIterator<T> {

    private T array[];

    private int index = 0;

    public ArrayIterator(T array[]) {
        this.array = array;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < array.length;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        return this.array[index++];
    }
}
