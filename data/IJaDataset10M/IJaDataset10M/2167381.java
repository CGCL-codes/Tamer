package org.genxdm.bridgekit.axes;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.genxdm.Model;
import org.genxdm.exceptions.PreCondition;

final class IteratorChildAxisElements<N> implements Iterator<N> {

    private N m_pending;

    private final Model<N> m_navigator;

    public IteratorChildAxisElements(final N origin, final Model<N> navigator) {
        m_navigator = PreCondition.assertArgumentNotNull(navigator);
        m_pending = getNextElement(navigator.getFirstChild(origin), navigator);
    }

    public boolean hasNext() {
        return (null != m_pending);
    }

    public N next() throws NoSuchElementException {
        if (m_pending != null) {
            final N last = m_pending;
            m_pending = getNextElement(m_navigator.getNextSibling(m_pending), m_navigator);
            return last;
        } else {
            throw new NoSuchElementException();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private static <N> N getNextElement(final N origin, final Model<N> navigator) {
        N candidate = origin;
        while (null != candidate) {
            if (navigator.isElement(candidate)) {
                return candidate;
            } else {
                candidate = navigator.getNextSibling(candidate);
            }
        }
        return null;
    }
}
