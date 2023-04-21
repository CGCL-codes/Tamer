package org.datanucleus.jdo.state;

import org.datanucleus.state.LifeCycleState;

/**
 * Factory for life cycle states
 *
 * @version $Revision: 1.2 $
 **/
public abstract class LifeCycleStateFactory {

    private static LifeCycleState states[];

    static {
        states = new LifeCycleState[LifeCycleState.TOTAL];
        states[LifeCycleState.HOLLOW] = new Hollow();
        states[LifeCycleState.P_CLEAN] = new PersistentClean();
        states[LifeCycleState.P_DIRTY] = new PersistentDirty();
        states[LifeCycleState.P_NEW] = new PersistentNew();
        states[LifeCycleState.P_NEW_DELETED] = new PersistentNewDeleted();
        states[LifeCycleState.P_DELETED] = new PersistentDeleted();
        states[LifeCycleState.P_NONTRANS] = new PersistentNontransactional();
        states[LifeCycleState.T_CLEAN] = new TransientClean();
        states[LifeCycleState.T_DIRTY] = new TransientDirty();
        states[LifeCycleState.P_NONTRANS_DIRTY] = new PersistentNontransactionalDirty();
        states[LifeCycleState.DETACHED_CLEAN] = new DetachedClean();
        states[LifeCycleState.DETACHED_DIRTY] = new DetachedDirty();
        states[LifeCycleState.TRANSIENT] = null;
    }

    /**
     * Returns the LifeCycleState for the state constant.
     * @param stateType the type as integer
     * @return the type as LifeCycleState object
     */
    public static final LifeCycleState getLifeCycleState(int stateType) {
        return states[stateType];
    }
}
