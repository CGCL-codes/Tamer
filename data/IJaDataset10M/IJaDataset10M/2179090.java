package org.archive.util;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;

/**
 * Universal sublist implementation.  Instances of this class are 
 * appropriate to return from {@link List#subList(int, int)} 
 * implementations.
 * 
 * <p>This implementation is efficient if the super list is random-access.
 * LinkedList-style super lists should subclass this and provide a custom
 * iterator.
 * 
 * @author pjack
 * @param <E>  the element type of the list
 */
public class SubList<E> extends AbstractList<E> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The list that created this SubList.
     */
    private final List<E> delegate;

    /**
     * The starting index of the SubList, inclusive.
     */
    private int start;

    /**
     * The ending index of the SubList, exclusive.
     */
    private int end;

    /**
     * Constructor.
     * 
     * @param delegate  the list that create this SubList
     * @param start   the starting index of the sublist, inclusive
     * @param end  the ending index of the sublist, exclusive
     * @throws IndexOutOfBoundsException   if start or end are outside the
     *   bounds of the list
     * @throws IllegalArgumentException  if end is less than start
     */
    public SubList(List<E> delegate, int start, int end) {
        if ((start < 0) || (start > delegate.size())) {
            throw new IndexOutOfBoundsException();
        }
        if ((end < 0) || (end > delegate.size())) {
            throw new IndexOutOfBoundsException();
        }
        if (end < start) {
            throw new IllegalArgumentException();
        }
        this.delegate = delegate;
        this.start = start;
        this.end = end;
    }

    /**
     * Ensures that the given index is strictly within the bounds of this
     * SubList.  
     * 
     * @param index  the index to check
     * @throws  IndexOutOfBoundsException  if the index is out of bounds
     */
    private void ensureInside(int index) {
        if ((index < 0) || (index >= end - start)) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Ensures that the given index is either within bounds or on the border
     * of this SubList.  In other words, this method allows the given index
     * to be equal to {@link #size()}.
     * 
     * @param index  the index to check
     * @throws  IndexOutOfBoundsException  if the index is out of bounds
     */
    private void ensureBorder(int index) {
        if ((index < 0) || (index > end - start)) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public E get(int index) {
        ensureInside(index);
        return delegate.get(start + index);
    }

    @Override
    public int size() {
        return end - start;
    }

    @Override
    public E set(int index, E value) {
        ensureInside(index);
        return delegate.set(start + index, value);
    }

    @Override
    public void add(int index, E value) {
        ensureBorder(index);
        delegate.add(start + index, value);
        end++;
    }

    @Override
    public E remove(int index) {
        ensureInside(index);
        return delegate.remove(start + index);
    }
}
