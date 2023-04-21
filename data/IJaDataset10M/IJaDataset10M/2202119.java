package com.googlecode.lawu.util.iterators;

import java.util.NoSuchElementException;
import com.googlecode.lawu.dp.Iterator;
import com.googlecode.lawu.util.Iterators;

/**
 * An iterator that continuously loops over the elements of a traversal.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type over which the iteration takes place
 * @see Iterators#loop(Iterator)
 */
public class ContinuousIterator<T> extends AbstractUniversalIterator<T> {

    private final Iterator<? extends T> iterator;

    /**
	 * Constructs an iterator that continuously loops over the elements of the
	 * given traversal. If you need to loop an unresettable iterator, make sure
	 * you {@linkplain Iterators#copy(Iterator) copy} it first or you'll
	 * eventually have problems.
	 * 
	 * @param iterator
	 *            the traversal to loop
	 */
    public ContinuousIterator(Iterator<? extends T> iterator) {
        if (iterator == null) throw new NullPointerException("Can't loop over null iterator.");
        this.iterator = iterator;
        reset();
    }

    /**
	 * Advances this iterator to the next element in the traversal. Behavior is
	 * undefined if the iterator {@linkplain #isDone() is done}.
	 */
    @Override
    public void advance() {
        this.iterator.advance();
    }

    /**
	 * Retrieves the current element in the traversal represented by this
	 * iterator.
	 * 
	 * @return the current element in the traversal
	 */
    @Override
    public T current() {
        return this.iterator.current();
    }

    /**
	 * Checks whether this iterator has exhausted all the elements of its
	 * underlying traversal.
	 * 
	 * @return whether all elements have been exhausted
	 */
    @Override
    public boolean isDone() {
        if (this.iterator.isDone()) reset();
        return false;
    }

    /**
	 * Moves this iterator to the beginning of the traversal. Some iterators
	 * cannot be reset once the iteration started. For example, an iterator over
	 * the lines of an input stream probably can't reread old lines. But all
	 * iterators should allow exception-less calls to this method before any
	 * calls to {@link #advance()}.
	 * 
	 * @throws IllegalStateException
	 *             if resetting this iterator is impossible
	 */
    @Override
    public void reset() {
        this.iterator.reset();
        if (this.iterator.isDone()) throw new NoSuchElementException("Can't loop over empty iterator.");
    }
}
