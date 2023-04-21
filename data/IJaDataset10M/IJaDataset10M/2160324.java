package org.enerj.apache.commons.collections.iterators;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.enerj.apache.commons.collections.Predicate;

/** 
 * Decorates another {@link ListIterator} using a predicate to filter elements.
 * <p>
 * This iterator decorates the underlying iterator, only allowing through
 * those elements that match the specified {@link Predicate Predicate}.
 *
 * @since Commons Collections 2.0
 * @version $Revision: 348018 $ $Date: 2005-11-21 23:34:37 +0000 (Mon, 21 Nov 2005) $
 * 
 * @author Rodney Waldhoff
 */
public class FilterListIterator implements ListIterator {

    /** The iterator being used */
    private ListIterator iterator;

    /** The predicate being used */
    private Predicate predicate;

    /** 
     * The value of the next (matching) object, when 
     * {@link #nextObjectSet} is true. 
     */
    private Object nextObject;

    /** 
     * Whether or not the {@link #nextObject} has been set
     * (possibly to <code>null</code>). 
     */
    private boolean nextObjectSet = false;

    /** 
     * The value of the previous (matching) object, when 
     * {@link #previousObjectSet} is true. 
     */
    private Object previousObject;

    /** 
     * Whether or not the {@link #previousObject} has been set
     * (possibly to <code>null</code>). 
     */
    private boolean previousObjectSet = false;

    /** 
     * The index of the element that would be returned by {@link #next}.
     */
    private int nextIndex = 0;

    /**
     * Constructs a new <code>FilterListIterator</code> that will not function
     * until {@link #setListIterator(ListIterator) setListIterator}
     * and {@link #setPredicate(Predicate) setPredicate} are invoked.
     */
    public FilterListIterator() {
        super();
    }

    /**
     * Constructs a new <code>FilterListIterator</code> that will not 
     * function until {@link #setPredicate(Predicate) setPredicate} is invoked.
     *
     * @param iterator  the iterator to use
     */
    public FilterListIterator(ListIterator iterator) {
        super();
        this.iterator = iterator;
    }

    /**
     * Constructs a new <code>FilterListIterator</code>.
     *
     * @param iterator  the iterator to use
     * @param predicate  the predicate to use
     */
    public FilterListIterator(ListIterator iterator, Predicate predicate) {
        super();
        this.iterator = iterator;
        this.predicate = predicate;
    }

    /**
     * Constructs a new <code>FilterListIterator</code> that will not function
     * until {@link #setListIterator(ListIterator) setListIterator} is invoked.
     *
     * @param predicate  the predicate to use.
     */
    public FilterListIterator(Predicate predicate) {
        super();
        this.predicate = predicate;
    }

    /** Not supported. */
    public void add(Object o) {
        throw new UnsupportedOperationException("FilterListIterator.add(Object) is not supported.");
    }

    public boolean hasNext() {
        if (nextObjectSet) {
            return true;
        } else {
            return setNextObject();
        }
    }

    public boolean hasPrevious() {
        if (previousObjectSet) {
            return true;
        } else {
            return setPreviousObject();
        }
    }

    public Object next() {
        if (!nextObjectSet) {
            if (!setNextObject()) {
                throw new NoSuchElementException();
            }
        }
        nextIndex++;
        Object temp = nextObject;
        clearNextObject();
        return temp;
    }

    public int nextIndex() {
        return nextIndex;
    }

    public Object previous() {
        if (!previousObjectSet) {
            if (!setPreviousObject()) {
                throw new NoSuchElementException();
            }
        }
        nextIndex--;
        Object temp = previousObject;
        clearPreviousObject();
        return temp;
    }

    public int previousIndex() {
        return (nextIndex - 1);
    }

    /** Not supported. */
    public void remove() {
        throw new UnsupportedOperationException("FilterListIterator.remove() is not supported.");
    }

    /** Not supported. */
    public void set(Object o) {
        throw new UnsupportedOperationException("FilterListIterator.set(Object) is not supported.");
    }

    /** 
     * Gets the iterator this iterator is using.
     * 
     * @return the iterator.
     */
    public ListIterator getListIterator() {
        return iterator;
    }

    /** 
     * Sets the iterator for this iterator to use.
     * If iteration has started, this effectively resets the iterator.
     * 
     * @param iterator  the iterator to use
     */
    public void setListIterator(ListIterator iterator) {
        this.iterator = iterator;
    }

    /** 
     * Gets the predicate this iterator is using.
     * 
     * @return the predicate.
     */
    public Predicate getPredicate() {
        return predicate;
    }

    /** 
     * Sets the predicate this the iterator to use.
     * 
     * @param predicate  the transformer to use
     */
    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    private void clearNextObject() {
        nextObject = null;
        nextObjectSet = false;
    }

    private boolean setNextObject() {
        if (previousObjectSet) {
            clearPreviousObject();
            if (!setNextObject()) {
                return false;
            } else {
                clearNextObject();
            }
        }
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (predicate.evaluate(object)) {
                nextObject = object;
                nextObjectSet = true;
                return true;
            }
        }
        return false;
    }

    private void clearPreviousObject() {
        previousObject = null;
        previousObjectSet = false;
    }

    private boolean setPreviousObject() {
        if (nextObjectSet) {
            clearNextObject();
            if (!setPreviousObject()) {
                return false;
            } else {
                clearPreviousObject();
            }
        }
        while (iterator.hasPrevious()) {
            Object object = iterator.previous();
            if (predicate.evaluate(object)) {
                previousObject = object;
                previousObjectSet = true;
                return true;
            }
        }
        return false;
    }
}
