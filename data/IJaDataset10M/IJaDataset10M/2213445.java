package uk.org.ogsadai.util.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that iterates through all of the objects produced by a collection
 * of child iterators.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class CollectiveIterator implements Iterator<Object> {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Iterates through the child iterators. */
    private final Iterator<Iterator<Object>> mChildIterators;

    /** Current child iterator or null if there are no more. */
    private Iterator<Object> mCurrentIterator;

    /**
     * Creates a new collective iterator.
     * 
     * @param childIterators
     *     Collection of iterators to use.
     */
    public CollectiveIterator(final Collection<Iterator<Object>> childIterators) {
        mChildIterators = childIterators.iterator();
        nextChild();
    }

    @Override
    public boolean hasNext() {
        while (mCurrentIterator != null && !mCurrentIterator.hasNext()) {
            nextChild();
        }
        return mCurrentIterator != null;
    }

    @Override
    public Object next() {
        Object block = null;
        while (block == null && mCurrentIterator != null) {
            if (mCurrentIterator.hasNext()) {
                block = mCurrentIterator.next();
            } else {
                nextChild();
            }
        }
        if (mCurrentIterator == null) {
            throw new NoSuchElementException("The iterator has no more elements.");
        }
        return block;
    }

    @Override
    public void remove() {
        if (mCurrentIterator != null) {
            mCurrentIterator.remove();
        }
    }

    /**
     * Move to the next child iterator. mCurrentIterator is set to null if there
     * are no more iterators.
     */
    private void nextChild() {
        if (mChildIterators.hasNext()) {
            mCurrentIterator = (Iterator<Object>) mChildIterators.next();
        } else {
            mCurrentIterator = null;
        }
    }
}
