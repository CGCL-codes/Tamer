package org.joda.primitives.listiterator;

import org.joda.primitives.iterator.IntIterator;

/**
 * Defines a list iterator over primitive <code>int</code> values.
 * 
 * @author Stephen Colebourne
 * @author Jason Tiscione
 * @version CODE GENERATED
 * @since 1.0
 */
public interface IntListIterator extends IntIterator, PrimitiveListIterator<Integer> {

    /**
     * Gets the previous value from the iterator.
     *
     * @return the previous available value
     * @throws NoSuchElementException if there are no more values available
     */
    int previousInt();

    /**
     * Adds the specified value to the list underlying the iterator at the
     * current iteration index (optional operation).
     *
     * @param value  the value to add
     * @throws IllegalStateException if the iterator cannot be added to at present
     * @throws UnsupportedOperationException if not supported by this collection
     */
    void add(int value);

    /**
     * Sets the last retrieved value from the iterator (optional operation).
     *
     * @param value  the value to set
     * @throws IllegalStateException if the iterator cannot be set to at present
     * @throws UnsupportedOperationException if not supported by this collection
     */
    void set(int value);
}
