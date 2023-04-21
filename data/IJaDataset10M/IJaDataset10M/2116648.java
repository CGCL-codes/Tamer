package org.lindenb.util;

/**
 * A walker is an kind of {@link java.util.iterator} but with no 'hasNext' method
 * The method 'next' returns an object or null if there is no more object available
 * After the last object was returned or when the walker is disposed, 'close' should be called.
 * @param <T> the object returned
 */
public interface Walker<T> {

    /** 
 * return the next element or <code>null</code> if there is no more element
 * @return the next element or null if there is no more element
 */
    public T next();

    /**
 * dispose this Walker
 */
    public void close();
}
