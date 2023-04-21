package com.genia.toolbox.persistence.basis.criteria;

import java.util.Iterator;
import com.genia.toolbox.basics.bean.IterableIterator;
import com.genia.toolbox.persistence.criteria.CriteriaResult;

/**
 * A {@link IterableIterator} that can converte {@link CriteriaResult} to their
 * main value.
 * 
 * @param <MAINTYPE>
 *          the main type of the underlying {@link CriteriaResult} iterator.
 */
public class SimpleCriteriaResultIterable<MAINTYPE> implements IterableIterator<MAINTYPE> {

    /**
   * the underlaying iterator.
   */
    private final Iterator<CriteriaResult<MAINTYPE>> baseIterator;

    /**
   * constructor.
   * 
   * @param baseIterator
   *          the underlaying iterator
   */
    public SimpleCriteriaResultIterable(Iterator<CriteriaResult<MAINTYPE>> baseIterator) {
        this.baseIterator = baseIterator;
    }

    /**
   * Returns an iterator over a set of elements of type T.
   * 
   * @return an Iterator.
   * @see java.lang.Iterable#iterator()
   */
    public Iterator<MAINTYPE> iterator() {
        return this;
    }

    /**
   * Returns <tt>true</tt> if the iteration has more elements. (In other
   * words, returns <tt>true</tt> if <tt>next</tt> would return an element
   * rather than throwing an exception.)
   * 
   * @return <tt>true</tt> if the iterator has more elements.
   * @see java.util.Iterator#hasNext()
   */
    public boolean hasNext() {
        return baseIterator.hasNext();
    }

    /**
   * Returns the next element in the iteration. Calling this method repeatedly
   * until the {@link #hasNext()} method returns false will return each element
   * in the underlying collection exactly once.
   * 
   * @return the next element in the iteration.
   * @see java.util.Iterator#next()
   */
    public MAINTYPE next() {
        return baseIterator.next().get();
    }

    /**
   * Removes from the underlying collection the last element returned by the
   * iterator (optional operation). This method can be called only once per call
   * to <tt>next</tt>. The behavior of an iterator is unspecified if the
   * underlying collection is modified while the iteration is in progress in any
   * way other than by calling this method.
   * 
   * @see java.util.Iterator#remove()
   */
    public void remove() {
        baseIterator.remove();
    }
}
