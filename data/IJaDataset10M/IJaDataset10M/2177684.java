package org.apache.commons.collections.primitives.adapters;

import java.util.Collection;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.3 $ $Date: 2008/04/26 11:00:55 $
 * @author Rodney Waldhoff
 */
final class NonSerializableCollectionLongCollection extends AbstractCollectionLongCollection {

    public NonSerializableCollectionLongCollection(final Collection collection) {
        _collection = collection;
    }

    @Override
    protected Collection getCollection() {
        return _collection;
    }

    private Collection _collection = null;
}
