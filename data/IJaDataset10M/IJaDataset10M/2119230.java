package org.middleheaven.util.collections;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * 
 */
public class EnumerationIterator<T> implements Iterator<T> {

    private Enumeration<T> enumeration;

    /**
	 * Constructor.
	 * @param enumeration
	 */
    public EnumerationIterator(Enumeration<T> enumeration) {
        this.enumeration = enumeration;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public T next() {
        return enumeration.nextElement();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
