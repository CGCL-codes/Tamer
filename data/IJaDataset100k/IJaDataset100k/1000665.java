package org.jcvi.common.core.util.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@code EmptyIterator} is a NullObject implementation
 * of {@link Iterator}; an EmptyIterator will never 
 * have any elements to iterate over.
 * @author dkatzel
 *
 *
 */
public final class EmptyIterator<E> implements Iterator<E> {

    /**
     * Singleton instance of Empty iterator that can be shared 
     * by all.
     */
    @SuppressWarnings("rawtypes")
    private static final EmptyIterator INSTANCE = new EmptyIterator();

    /**
     * Creates an {@link EmptyIterator} of Type E.
     * @param <E> the type of element to be iterated over.
     * @return an instance of EmptyIterator.
     */
    @SuppressWarnings("unchecked")
    public static <E> EmptyIterator<E> createEmptyIterator() {
        return INSTANCE;
    }

    /**
     * Private constructor so no one can subclass.
     */
    private EmptyIterator() {
    }

    /**
     * Never has a next.
     * @return {@code false}
     */
    @Override
    public boolean hasNext() {
        return false;
    }

    /**
     * Will always throw an NoSuchElementException.
     * @throws NoSuchElementException because there will never be a next.
     */
    @Override
    public E next() {
        throw new NoSuchElementException("no elements in empty iterator");
    }

    /**
     * Does nothing.
     */
    @Override
    public void remove() {
    }
}
