package org.apache.commons.collections.primitives.adapters;

import java.io.Serializable;
import java.util.Collection;
import org.apache.commons.collections.primitives.LongCollection;

/**
 * Adapts an {@link LongCollection LongCollection} to the
 * {@link java.util.Collection Collection} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link LongCollection LongCollection} implementation in the "obvious" way.
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.2 $ $Date: 2008/02/24 14:34:13 $
 * @author Rodney Waldhoff
 */
public final class LongCollectionCollection extends AbstractLongCollectionCollection implements Serializable {

    /**
	 * Create a {@link Collection Collection} wrapping the specified
	 * {@link LongCollection LongCollection}. When the given <i>collection</i>
	 * is <code>null</code>, returns <code>null</code>.
	 * 
	 * @param collection
	 *            the (possibly <code>null</code>)
	 *            {@link LongCollection LongCollection} to wrap
	 * @return a {@link Collection Collection} wrapping the given <i>collection</i>,
	 *         or <code>null</code> when <i>collection</i> is
	 *         <code>null</code>.
	 */
    public static Collection wrap(final LongCollection collection) {
        if (null == collection) {
            return null;
        } else if (collection instanceof Serializable) {
            return new LongCollectionCollection(collection);
        } else {
            return new NonSerializableLongCollectionCollection(collection);
        }
    }

    /**
	 * Creates a {@link Collection Collection} wrapping the specified
	 * {@link LongCollection LongCollection}.
	 * 
	 * @see #wrap
	 */
    public LongCollectionCollection(final LongCollection collection) {
        _collection = collection;
    }

    @Override
    protected LongCollection getLongCollection() {
        return _collection;
    }

    private LongCollection _collection = null;
}
