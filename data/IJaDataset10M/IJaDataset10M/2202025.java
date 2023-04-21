package org.datanucleus.jpa.state;

import org.datanucleus.StateManager;
import org.datanucleus.Transaction;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.state.LifeCycleState;

/**
 * Class representing the life cycle state of PersistentDeleted.
 *
 * @version $Revision: 1.6 $
 **/
class PersistentDeleted extends LifeCycleState {

    /** Protected Constructor to prevent external instantiation. */
    protected PersistentDeleted() {
        isPersistent = true;
        isDirty = true;
        isNew = false;
        isDeleted = true;
        isTransactional = true;
        stateType = P_DELETED;
    }

    /**
     * Method to transition to non-transactional.
     * @param sm StateManager.
     * @return new LifeCycle state.
     */
    public LifeCycleState transitionMakeNontransactional(StateManager sm) {
        throw new NucleusUserException(LOCALISER.msg("027007"), sm.getInternalObjectId());
    }

    /**
     * Method to transition to transient.
     * @param sm StateManager.
     * @param useFetchPlan to make transient the fields in the fetch plan
     * @return new LifeCycle state.
     */
    public LifeCycleState transitionMakeTransient(StateManager sm, boolean useFetchPlan, boolean detachAllOnCommit) {
        throw new NucleusUserException(LOCALISER.msg("027008"), sm.getInternalObjectId());
    }

    /**
     * Method to transition to persistent state.
     * @param sm StateManager.
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionMakePersistent(StateManager sm) {
        return changeState(sm, P_CLEAN);
    }

    /**
     * Method to transition to commit state.
     * @param sm StateManager.
     * @param tx the Transaction been committed.
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionCommit(StateManager sm, Transaction tx) {
        sm.clearFields();
        return changeState(sm, TRANSIENT);
    }

    /**
     * Method to transition to rollback state.
     * @param sm StateManager.
     * @param tx The transaction
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionRollback(StateManager sm, Transaction tx) {
        if (tx.getRetainValues()) {
            if (tx.getRestoreValues()) {
                sm.restoreFields();
            }
            return changeState(sm, P_NONTRANS);
        } else {
            sm.clearNonPrimaryKeyFields();
            sm.clearSavedFields();
            return changeState(sm, HOLLOW);
        }
    }

    /**
     * Method to transition to write-field state.
     * @param sm StateManager.
     * @return new LifeCycle state.
     **/
    public LifeCycleState transitionWriteField(StateManager sm) {
        throw new NucleusUserException(LOCALISER.msg("027010"), sm.getInternalObjectId());
    }

    /**
     * Method to return a string version of this object.
     * @return The string "P_DELETED".
     **/
    public String toString() {
        return "P_DELETED";
    }
}
