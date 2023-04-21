package org.middleheaven.util.collections;

import java.util.Iterator;

/**
 * 
 */
public abstract class IncrementIterator<E> implements Iterator<E> {

    private int count;

    private int current = 0;

    public IncrementIterator(int count) {
        this.count = count;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final boolean hasNext() {
        return current + 1 < count;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final E next() {
        return nextFor(++current);
    }

    /**
	 * @param i
	 * @return
	 */
    protected abstract E nextFor(int current);
}
