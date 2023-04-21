package org.genxdm.processor.w3c.xs.validation.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An {@link Iterator} for walking up the stack of {@link ValidationItem}s.
 */
final class ValidationItemIterator implements Iterator<ValidationItem> {

    private ValidationItem m_pendingItem;

    public ValidationItemIterator(final ValidationItem pendingItem) {
        m_pendingItem = PreCondition.assertArgumentNotNull(pendingItem);
    }

    public boolean hasNext() {
        return (null != m_pendingItem);
    }

    public ValidationItem next() throws NoSuchElementException {
        if (null != m_pendingItem) {
            final ValidationItem nextItem = m_pendingItem;
            m_pendingItem = m_pendingItem.getParentItem();
            return nextItem;
        } else {
            throw new NoSuchElementException();
        }
    }

    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
