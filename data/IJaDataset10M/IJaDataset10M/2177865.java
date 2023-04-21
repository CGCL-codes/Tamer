package net.sf.alc.cfpj.iterators;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * @author Alain
 * 
 */
class EmptyListIterator<T> implements ListIterator<T> {

    @SuppressWarnings({ "rawtypes" })
    private static final EmptyListIterator<?> INSTANCE = new EmptyListIterator();

    @SuppressWarnings("unchecked")
    static <T> EmptyListIterator<T> instance() {
        return (EmptyListIterator<T>) INSTANCE;
    }

    private EmptyListIterator() {
    }

    @Override
    public void add(T e) {
        throw new UnsupportedOperationException("add operation not supported on a EmptyListIterator.");
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException("EmptyListIterator has no next element.");
    }

    @Override
    public int nextIndex() {
        return 0;
    }

    @Override
    public T previous() {
        throw new NoSuchElementException("EmptyListIterator has no previous element.");
    }

    @Override
    public int previousIndex() {
        return -1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove operation not supported on a EmptyListIterator.");
    }

    @Override
    public void set(T e) {
        throw new UnsupportedOperationException("set operation not supported on a EmptyListIterator.");
    }
}
