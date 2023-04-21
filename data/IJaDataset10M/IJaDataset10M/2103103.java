package com.completex.objective.components.persistency.transact;

import com.completex.objective.components.persistency.OdalPersistencyException;

/**
 * @author Gennady Krizhevsky
 */
public interface TransactionManagerCtl extends TransactionManager {

    /**
     * @return new Transaction
     * @throws com.completex.objective.components.persistency.OdalPersistencyException
     * @see TransactionManager#begin()
     */
    Transaction begin(WrappedTransactionFactory wrappedTransactionFactory) throws OdalPersistencyException;
}
