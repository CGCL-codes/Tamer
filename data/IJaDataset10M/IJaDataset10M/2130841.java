package org.apache.commons.collections.primitives;

import java.util.NoSuchElementException;

/**
 * An iterator over <code>float</code> values.
 * 
 * @see org.apache.commons.collections.primitives.adapters.FloatIteratorIterator
 * @see org.apache.commons.collections.primitives.adapters.IteratorFloatIterator
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 1.2.2.2 $ $Date: 2008/04/26 11:00:55 $
 * 
 * @author Rodney Waldhoff
 */
public interface FloatIterator {

    /**
	 * Returns <code>true</code> iff I have more elements. (In other words,
	 * returns <code>true</code> iff a subsequent call to {@link #next next}
	 * will return an element rather than throwing an exception.)
	 * 
	 * @return <code>true</code> iff I have more elements
	 */
    boolean hasNext();

    /**
	 * Returns the next element in me.
	 * 
	 * @return the next element in me
	 * @throws NoSuchElementException
	 *             if there is no next element
	 */
    float next();

    /**
	 * Removes from my underlying collection the last element
	 * {@link #next returned} by me (optional operation).
	 * 
	 * @throws UnsupportedOperationException
	 *             if this operation is not supported
	 * @throws IllegalStateException
	 *             if {@link #next} has not yet been called, or {@link #remove}
	 *             has already been called since the last call to {@link #next}.
	 */
    void remove();
}
