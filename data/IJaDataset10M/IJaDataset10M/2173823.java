package org.datanucleus.api.jdo.state;

import org.datanucleus.Transaction;
import org.datanucleus.state.LifeCycleState;
import org.datanucleus.state.StateManager;

/**
 * This class represents TransientClean state specific state transitions as requested by StateManager. 
 * This state is the result of a call to makeTransactional on a Transient instance, or commit or rollback 
 * of a TransientDirty instance.
 */
class TransientClean extends LifeCycleState {

    TransientClean() {
        isPersistent = false;
        isTransactional = true;
        isDirty = false;
        isNew = false;
        isDeleted = false;
        stateType = T_CLEAN;
    }

    /**
     * @param sm The StateManager 
     * @param useFetchPlan to make transient the fields in the fetch plan
     * @return new LifeCycle state.
     * @see LifeCycleState#transitionMakeTransient(StateManager sm)
     */
    public LifeCycleState transitionMakeTransient(StateManager sm, boolean useFetchPlan, boolean detachAllOnCommit) {
        return this;
    }

    /**
     * @param sm The StateManager 
     * @see LifeCycleState#transitionMakeNontransactional(StateManager sm)
     */
    public LifeCycleState transitionMakeNontransactional(StateManager sm) {
        try {
            return changeTransientState(sm, TRANSIENT);
        } finally {
            sm.disconnect();
        }
    }

    /**
     * @param sm The StateManager 
     * @see LifeCycleState#transitionMakePersistent(StateManager sm)
     */
    public LifeCycleState transitionMakePersistent(StateManager sm) {
        sm.registerTransactional();
        return changeState(sm, P_NEW);
    }

    /**
     * @param sm The StateManager
     * @param isLoaded if the field was previously loaded.
     * @see LifeCycleState#transitionReadField(StateManager sm, boolean isLoaded)
     */
    public LifeCycleState transitionReadField(StateManager sm, boolean isLoaded) {
        return this;
    }

    /**
     * @param sm The StateManager
     * @see LifeCycleState#transitionWriteField(StateManager sm)
     */
    public LifeCycleState transitionWriteField(StateManager sm) {
        Transaction tx = sm.getObjectManager().getTransaction();
        if (tx.isActive()) {
            sm.saveFields();
            return changeTransientState(sm, T_DIRTY);
        } else {
            return this;
        }
    }

    /**
     * Method to transition to commit state.
     * This is a no-op.
     * @param sm StateManager.
     * @param tx the Transaction been committed.
     * @return new LifeCycle state.
     */
    public LifeCycleState transitionCommit(StateManager sm, Transaction tx) {
        return this;
    }

    /**
     * @param sm The StateManager
     * @param tx The Transaction
     * @see LifeCycleState#transitionRollback(StateManager sm,Transaction tx)
     */
    public LifeCycleState transitionRollback(StateManager sm, Transaction tx) {
        return this;
    }

    /**
     * Method to return a string version of this object.
     * @return The string "T_CLEAN".
     */
    public String toString() {
        return "T_CLEAN";
    }
}
