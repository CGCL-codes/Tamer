package com.completex.objective.components.persistency.key.impl;

import com.completex.objective.components.persistency.ColumnType;
import com.completex.objective.components.persistency.OdalPersistencyException;
import com.completex.objective.components.persistency.Persistency;
import com.completex.objective.components.persistency.PersistentEntry;
import com.completex.objective.components.persistency.transact.Transaction;

/**
 * Epoch Last Updated Generator - populate numeric field with epoch value: 
 * System.currentTimeMillis() / 1000L 
 * 
 * @author Gennady Krizhevsky
 */
public class EpochLastUpdatedGenerator extends CreatedDateGenerator {

    /**
     * @see CreatedDateGenerator#updateValue(com.completex.objective.components.persistency.transact.Transaction, com.completex.objective.components.persistency.Persistency, com.completex.objective.components.persistency.PersistentEntry, boolean)   
     */
    public void updateValue(Transaction transaction, Persistency persistency, PersistentEntry persistentEntry, boolean complexDirty) throws OdalPersistencyException {
        if (!ColumnType.isNumeric(persistentEntry.getColumn().getType())) {
            throw new OdalPersistencyException("Unsupported field type [" + persistentEntry.getColumn().getType() + "]");
        }
        if (persistentEntry.getRecord().isDirty()) {
            date(persistentEntry);
        }
    }

    /**
     * @see CreatedDateGenerator#date(com.completex.objective.components.persistency.PersistentEntry) 
     */
    protected void date(PersistentEntry persistentEntry) {
        persistentEntry.setValue(new Long(System.currentTimeMillis() / 1000L));
    }
}
