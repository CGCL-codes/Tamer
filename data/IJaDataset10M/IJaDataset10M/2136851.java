package de.ah7.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Andreas Huber <dev@ah7.de>
 */
public class FilterIterator<E> implements Iterator<E> {

    private Iterator<E> iterator;

    private FilterIteratorCallback callback;

    private E next;

    public FilterIterator(Iterator<E> iterator, FilterIteratorCallback callback) {
        this.iterator = iterator;
        this.callback = callback;
        filteredNext();
    }

    /**
     * 
     * Removes from the underlying collection the last element returned by the
     * iterator (optional operation).  This method can be called only once per
     * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
     * the underlying collection is modified while the iteration is in
     * progress in any way other than by calling this method.
     * 
     * @exception UnsupportedOperationException if the <tt>remove</tt>
     * 		  operation is not supported by this Iterator.
     * @exception IllegalStateException if the <tt>next</tt> method has not
     * 		  yet been called, or the <tt>remove</tt> method has already
     * 		  been called after the last call to the <tt>next</tt>
     * 		  method.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the next element in the iteration.  Calling this method
     * repeatedly until the {@link #hasNext()} method returns false will
     * return each element in the underlying collection exactly once.
     * 
     * @return the next element in the iteration.
     * @exception NoSuchElementException iteration has no more elements.
     */
    public E next() {
        E result = this.next;
        filteredNext();
        return result;
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     * 
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext() {
        return this.next != null;
    }

    private void filteredNext() {
        this.next = null;
        if (this.iterator.hasNext()) {
            this.next = this.iterator.next();
        }
        while ((this.next != null) && (this.callback.filter(this.next))) {
            this.next = null;
            if (this.iterator.hasNext()) {
                this.next = this.iterator.next();
            }
        }
    }
}
