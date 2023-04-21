package org.squin.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is a generic implementation of an empty iterator, that is, an
 * iterator which return no elements.
 *
 * @author Olaf Hartig (hartig@informatik.hu-berlin.de)
 */
public class EmptyIterator<T> implements Iterator<T> {

    public boolean hasNext() {
        return false;
    }

    public T next() {
        throw new NoSuchElementException();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
