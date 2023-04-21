package org.jxpfw.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator that simulates iterating over an empty collection.
 * <br>I created this class since some classes ended up in returning multiple
 * iterators over empty collections. This class should conserve some memory
 * since the same empty Iterator is always returned (Singleton).
 * @param <T> The type for this iterator.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 1.8 $
 */
public final class EmptyCollectionIterator<T> implements Iterator<T[]> {

    /**
     * The singleton instance.
     */
    private static final Iterator<?> emptyCollectionIterator = new EmptyCollectionIterator<Object[]>();

    /**
     * Private constructor prevents creation of an instance outside this class.
     */
    private EmptyCollectionIterator() {
    }

    /**
     * Gets the Iterator (singleton) that iterates over an empty collection.
     * @return  Empty collection iterator.
     */
    public static Iterator<?> getInstance() {
        return emptyCollectionIterator;
    }

    /**
     * Determines whether there is a next element but there never is!
     * @return False since we are iterating over an empty collection.
     */
    public boolean hasNext() {
        return false;
    }

    /**
     * Throws a NoSuchElementException since there is no next element in an
     * empty collection.
     * @return Nothing, the exception is always thrown.
     * @throws NoSuchElementException Always.
     */
    public T[] next() {
        throw new NoSuchElementException("Don't call next on empty collection");
    }

    /**
     * Throws an UnsupportedOperationException since you can't remove an element
     * from an empty collection.
     * @throws UnsupportedOperationException Always.
     */
    public void remove() {
        throw new UnsupportedOperationException("Removing an object from an empty collection is not supported!");
    }
}
