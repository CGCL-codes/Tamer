package org.genxdm.processor.w3c.xs.validation.impl;

import java.util.Iterator;

final class UnaryIterator<E> implements Iterator<E> {

    private E m_thing;

    public UnaryIterator(final E thing) {
        m_thing = thing;
    }

    public boolean hasNext() {
        return (null != m_thing);
    }

    public E next() {
        final E pending = m_thing;
        m_thing = null;
        return pending;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
