package com.googlecode.lawu.util.iterators;

import java.util.Iterator;
import com.googlecode.lawu.util.Iterators;

/**
 * Adapter for a {@linkplain Iterator Java iterator}. Because Java's
 * <code>Iterator</code>s do not provide a {@link #reset()}-like operation, the
 * returned iterator will not be resettable.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type over which the iteration takes place
 * @see Iterators#adapt(Iterable)
 * @see Iterators#adapt(Iterator)
 */
public class JIteratorAdapter<T> extends IteratorAdapter<T> {

    private final Iterator<T> iterator;

    /**
	 * Constructs an iterator that adapts the specified Java
	 * <code>Iterator</code>.
	 * 
	 * @param iterator
	 *            the adaptee
	 */
    public JIteratorAdapter(Iterator<T> iterator) {
        if (iterator == null) throw new NullPointerException("Can't adapt null iterator.");
        this.iterator = iterator;
        init();
    }

    /**
	 * Constructs an iterator that adapts the specified <code>Iterable</code>.
	 * 
	 * @param iterable
	 *            the adaptee
	 */
    public JIteratorAdapter(Iterable<T> iterable) {
        this(iterable.iterator());
    }

    @Override
    protected void doAdvance() {
        if (this.iterator.hasNext()) setCurrent(this.iterator.next()); else markAsDone();
    }
}
