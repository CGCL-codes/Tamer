package org.datanucleus.jdo.connector;

import javax.resource.ResourceException;
import javax.resource.cci.LocalTransaction;

/**
 * Application demarcated local transaction
 * This interface is used for application level local transaction demarcation.
 * One can use the javax.jdo or the javax.resource.cci.LocalTransaction APIs,
 */
public class ApplicationLocalTransaction implements LocalTransaction {

    private final PersistenceManagerImpl pm;

    /**
     * Constructor
     * @param pm the PersistenceManager
     */
    public ApplicationLocalTransaction(PersistenceManagerImpl pm) {
        this.pm = pm;
    }

    public void begin() throws ResourceException {
        pm.currentTransaction().begin();
    }

    public void commit() throws ResourceException {
        pm.currentTransaction().commit();
    }

    public void rollback() throws ResourceException {
        pm.currentTransaction().rollback();
    }
}
