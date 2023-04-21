package org.apache.commons.collections.primitives.adapters;

import java.util.Iterator;
import org.apache.commons.collections.primitives.ShortIterator;

/**
 * Adapts an {@link ShortIterator ShortIterator} to the
 * {@link java.util.Iterator Iterator} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link ShortIterator ShortIterator} implementation in the "obvious" way.
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.3 $ $Date: 2008/04/26 11:00:55 $
 * @author Rodney Waldhoff
 */
public class ShortIteratorIterator implements Iterator {

    /**
	 * Create an {@link Iterator Iterator} wrapping the specified
	 * {@link ShortIterator ShortIterator}. When the given <i>iterator</i> is
	 * <code>null</code>, returns <code>null</code>.
	 * 
	 * @param iterator
	 *            the (possibly <code>null</code>)
	 *            {@link ShortIterator ShortIterator} to wrap
	 * @return an {@link Iterator Iterator} wrapping the given <i>iterator</i>,
	 *         or <code>null</code> when <i>iterator</i> is <code>null</code>.
	 */
    public static Iterator wrap(final ShortIterator iterator) {
        return null == iterator ? null : new ShortIteratorIterator(iterator);
    }

    /**
	 * Creates an {@link Iterator Iterator} wrapping the specified
	 * {@link ShortIterator ShortIterator}.
	 * 
	 * @see #wrap
	 */
    public ShortIteratorIterator(final ShortIterator iterator) {
        _iterator = iterator;
    }

    public boolean hasNext() {
        return _iterator.hasNext();
    }

    public Object next() {
        return new Short(_iterator.next());
    }

    public void remove() {
        _iterator.remove();
    }

    private ShortIterator _iterator = null;
}
