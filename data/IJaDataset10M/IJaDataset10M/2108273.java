package com.amazon.carbonado.qe;

import com.amazon.carbonado.MalformedTypeException;
import com.amazon.carbonado.Repository;
import com.amazon.carbonado.RepositoryException;
import com.amazon.carbonado.Storable;
import com.amazon.carbonado.SupportException;

/**
 * Provides internal access to a {@link Repository}, necessary for query
 * execution.
 *
 * @author Brian S O'Neill
 */
public interface RepositoryAccess {

    Repository getRootRepository();

    /**
     * Returns a StorageAccess instance for the given user defined Storable
     * class or interface.
     *
     * @return specific type of StorageAccess instance
     * @throws IllegalArgumentException if specified type is null
     * @throws MalformedTypeException if specified type is not suitable
     */
    <S extends Storable> StorageAccess<S> storageAccessFor(Class<S> type) throws SupportException, RepositoryException;
}
