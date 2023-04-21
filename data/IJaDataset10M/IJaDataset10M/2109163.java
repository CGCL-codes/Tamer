package com.amazon.carbonado.repo.indexed;

import com.amazon.carbonado.RepositoryException;
import com.amazon.carbonado.Storable;
import com.amazon.carbonado.capability.Capability;

/**
 * Capability for gaining low-level access to index data, which can be used for
 * manual inspection and repair.
 *
 * @author Brian S O'Neill
 */
public interface IndexEntryAccessCapability extends Capability {

    /**
     * Returns index entry accessors for the known indexes of the given
     * storable type. The array might be empty, but it is never null. The array
     * is a copy, and so it may be safely modified.
     */
    <S extends Storable> IndexEntryAccessor<S>[] getIndexEntryAccessors(Class<S> storableType) throws RepositoryException;
}
