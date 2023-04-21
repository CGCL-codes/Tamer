package com.completex.objective.components.persistency;

import com.completex.objective.components.persistency.transact.Transaction;

/**
 * @author Gennady Krizhevsky
 */
public interface DelegatePersistentObjectFactory {

    AbstractPersistentObject newPersistentInstance(Transaction transaction, Persistency persistency, QueryFactory queryFactory, PersistentObject initiator) throws OdalPersistencyException;

    void setDelegatePersistentObjects(PersistentObject[] delegatePersistentObjects);

    void setDelegateValues(String[] delegateValues);

    void setDiscriminatorColumnName(String discriminatorColumnName);

    void setTransformer(DiscriminatorTransformer transformer);

    public static interface DiscriminatorTransformer {

        Object transform(PersistentObject persistentObject);
    }
}
