package com.completex.objective.components.persistency.key;

import com.completex.objective.components.persistency.OdalPersistencyException;
import com.completex.objective.components.persistency.Persistency;
import com.completex.objective.components.persistency.transact.Transaction;

/**
 * Stand-alone key generator
 * 
 * @author Gennady Krizhevsky
 */
public interface SimpleKeyGenerator extends KeyGenerator {

    /**
     * Generate next key
     * 
     * @param transaction transaction
     * @param persistency persistency
     * @return next key
     * @throws OdalPersistencyException
     */
    Object getNextKey(Transaction transaction, Persistency persistency) throws OdalPersistencyException;
}
