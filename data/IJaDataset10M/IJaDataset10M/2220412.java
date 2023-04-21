package com.antlersoft.odb;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Convert an iterator over ObjectRef's to an Enumeration over objects (Enumeration for legacy
 * reasons)
 * @author Michael MacDonald
 *
 */
public class FromRefIteratorEnumeration<E> implements Enumeration<E> {

    private Iterator<ObjectRef<E>> _base;

    public FromRefIteratorEnumeration(Iterator<ObjectRef<E>> it) {
        _base = it;
    }

    public boolean hasMoreElements() {
        return _base.hasNext();
    }

    public E nextElement() {
        return _base.next().getReferenced();
    }
}
