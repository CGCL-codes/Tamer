package org.apache.commons.collections.primitives.adapters;

import java.io.Serializable;
import java.util.Collection;
import org.apache.commons.collections.primitives.FloatCollection;

/**
 * Adapts an {@link FloatCollection FloatCollection} to the
 * {@link java.util.Collection Collection} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link FloatCollection FloatCollection} implementation in the "obvious" way.
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.2 $ $Date: 2008/02/24 14:34:14 $
 * @author Rodney Waldhoff
 */
public final class FloatCollectionCollection extends AbstractFloatCollectionCollection implements Serializable {

    /**
	 * Create a {@link Collection Collection} wrapping the specified
	 * {@link FloatCollection FloatCollection}. When the given <i>collection</i>
	 * is <code>null</code>, returns <code>null</code>.
	 * 
	 * @param collection
	 *            the (possibly <code>null</code>)
	 *            {@link FloatCollection FloatCollection} to wrap
	 * @return a {@link Collection Collection} wrapping the given <i>collection</i>,
	 *         or <code>null</code> when <i>collection</i> is
	 *         <code>null</code>.
	 */
    public static Collection wrap(final FloatCollection collection) {
        if (null == collection) {
            return null;
        } else if (collection instanceof Serializable) {
            return new FloatCollectionCollection(collection);
        } else {
            return new NonSerializableFloatCollectionCollection(collection);
        }
    }

    /**
	 * Creates a {@link Collection Collection} wrapping the specified
	 * {@link FloatCollection FloatCollection}.
	 * 
	 * @see #wrap
	 */
    public FloatCollectionCollection(final FloatCollection collection) {
        _collection = collection;
    }

    @Override
    protected FloatCollection getFloatCollection() {
        return _collection;
    }

    private FloatCollection _collection = null;
}
