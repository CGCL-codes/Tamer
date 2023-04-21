package org.hl7.util;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A helper implementation of an empty iterator; can be used with any
 * component type.  No longer a singleton, since couldn't make static fields
 * work nicely with generics.
 * 
 * @author Skirmantas Kligys (skirmis@hotmail.com)
 */
public class NullIterator<T> implements ListIterator<T> {

    /**
   * Default constructor.
   */
    public NullIterator() {
    }

    public boolean hasNext() {
        return false;
    }

    public T next() {
        throw new NoSuchElementException();
    }

    public boolean hasPrevious() {
        return false;
    }

    public T previous() {
        throw new NoSuchElementException();
    }

    public int nextIndex() {
        return 0;
    }

    public int previousIndex() {
        return -1;
    }

    public void remove() {
        throw new IllegalStateException();
    }

    public void set(T value) {
        throw new IllegalStateException();
    }

    public void add(T value) {
        throw new UnsupportedOperationException();
    }
}
