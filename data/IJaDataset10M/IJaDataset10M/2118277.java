package org.datanucleus.jpa;

import javax.jdo.JDOException;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import org.datanucleus.ObjectManager;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.transaction.NucleusTransactionException;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.Localiser;

/**
 * EntityTransaction implementation for JPA for ResourceLocal transaction.
 * Utilises the underlying ObjectManager and its real transaction, providing a JPA layer on top.
 * 
 * @version $Revision: 1.1 $
 */
public class EntityTransactionImpl implements EntityTransaction {

    /** Localisation utility for output messages */
    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.Localisation", NucleusJPAHelper.class.getClassLoader());

    /** ObjectManager managing the persistence and providing the underlying transaction. */
    ObjectManager om;

    /**
     * Constructor.
     * @param om The ObjectManager providing the transaction.
     */
    public EntityTransactionImpl(ObjectManager om) {
        this.om = om;
    }

    /**
     * Indicate whether a transaction is in progress.
     * @throws PersistenceException if an unexpected error condition is encountered.
     */
    public boolean isActive() {
        return om.getTransaction().isActive();
    }

    /**
     * Start a resource transaction.
     * @throws IllegalStateException if the transaction is active
     */
    public void begin() {
        try {
            om.getTransaction().begin();
        } catch (NucleusException jpe) {
            throw NucleusJPAHelper.getJPAExceptionForNucleusException(jpe);
        } catch (JDOException jdoe) {
            throw NucleusJPAHelper.getJPAExceptionForJDOException(jdoe);
        }
    }

    /**
     * Commit the current transaction, writing any unflushed changes to the database.
     * @throws IllegalStateException if isActive() is false.
     * @throws RollbackException if the commit fails.
     */
    public void commit() {
        assertActive();
        if (om.getTransaction().getRollbackOnly()) {
            if (NucleusLogger.TRANSACTION.isDebugEnabled()) {
                NucleusLogger.TRANSACTION.debug(LOCALISER.msg("015020"));
            }
            throw new RollbackException(LOCALISER.msg("015020"));
        }
        try {
            om.getTransaction().commit();
        } catch (NucleusTransactionException jpte) {
            PersistenceException pe = NucleusJPAHelper.getJPAExceptionForNucleusException((NucleusException) jpte.getCause());
            throw new RollbackException(LOCALISER.msg("015007"), pe);
        } catch (NucleusException jpe) {
            throw NucleusJPAHelper.getJPAExceptionForNucleusException(jpe);
        } catch (JDOException jdoe) {
            throw NucleusJPAHelper.getJPAExceptionForJDOException(jdoe);
        }
    }

    /**
     * Roll back the current transaction.
     * @throws IllegalStateException if isActive() is false.
     * @throws PersistenceException if an unexpected error condition is encountered.
     */
    public void rollback() {
        assertActive();
        try {
            om.getTransaction().rollback();
        } catch (NucleusException jpe) {
            throw NucleusJPAHelper.getJPAExceptionForNucleusException(jpe);
        } catch (JDOException jdoe) {
            throw NucleusJPAHelper.getJPAExceptionForJDOException(jdoe);
        }
    }

    /**
     * Determine whether the current transaction has been marked for rollback.
     * @throws IllegalStateException if isActive() is false.
     */
    public boolean getRollbackOnly() {
        assertActive();
        return om.getTransaction().getRollbackOnly();
    }

    /**
     * Mark the current transaction so that the only possible outcome of the transaction is for the transaction to be rolled back.
     * @throws IllegalStateException Thrown if the transaction is not active
     */
    public void setRollbackOnly() {
        assertActive();
        om.getTransaction().setRollbackOnly();
    }

    /**
     * Convenience accessor for setting a transaction option.
     * @param option option name
     * @param value The value
     */
    public void setOption(String option, int value) {
        om.getTransaction().setOption(option, value);
    }

    /**
     * Convenience accessor for setting a transaction option.
     * @param option option name
     * @param value The value
     */
    public void setOption(String option, boolean value) {
        om.getTransaction().setOption(option, value);
    }

    /**
     * Convenience accessor for setting a transaction option.
     * @param option option name
     * @param value The value
     */
    public void setOption(String option, String value) {
        om.getTransaction().setOption(option, value);
    }

    /**
     * Convenience method to throw an exception if the transaction is not active.
     * @throws IllegalStateException Thrown if the transaction is not active.
     */
    protected void assertActive() {
        if (!om.getTransaction().isActive()) {
            throw new IllegalStateException(LOCALISER.msg("015040"));
        }
    }
}
