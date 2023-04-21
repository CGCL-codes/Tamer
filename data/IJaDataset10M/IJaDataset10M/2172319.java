package org.apache.commons.collections.primitives.adapters;

import java.io.Serializable;
import java.util.Collection;
import org.apache.commons.collections.primitives.ShortCollection;

/**
 * Adapts a {@link java.lang.Number Number}-valued
 * {@link java.util.Collection Collection} to the
 * {@link ShortCollection ShortCollection} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link Collection Collection} implementation in the "obvious" way.
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.3 $ $Date: 2008/04/26 11:00:55 $
 * @author Rodney Waldhoff
 */
public final class CollectionShortCollection extends AbstractCollectionShortCollection implements Serializable {

    /**
	 * Create an {@link ShortCollection ShortCollection} wrapping the specified
	 * {@link Collection Collection}. When the given <i>collection</i> is
	 * <code>null</code>, returns <code>null</code>.
	 * 
	 * @param collection
	 *            the (possibly <code>null</code>) {@link Collection} to wrap
	 * @return an {@link ShortCollection ShortCollection} wrapping the given
	 *         <i>collection</i>, or <code>null</code> when <i>collection</i>
	 *         is <code>null</code>.
	 */
    public static ShortCollection wrap(final Collection collection) {
        if (null == collection) {
            return null;
        } else if (collection instanceof Serializable) {
            return new CollectionShortCollection(collection);
        } else {
            return new NonSerializableCollectionShortCollection(collection);
        }
    }

    /**
	 * Creates an {@link ShortCollection ShortCollection} wrapping the specified
	 * {@link Collection Collection}.
	 * 
	 * @see #wrap
	 */
    public CollectionShortCollection(final Collection collection) {
        _collection = collection;
    }

    @Override
    protected Collection getCollection() {
        return _collection;
    }

    private Collection _collection = null;
}
