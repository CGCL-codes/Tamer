package org.simbrain.workspace;

/**
 * Listener for coupling related events.
 *
 */
public interface CouplingListener {

    /**
     * Called when a coupling is added.
     *
     * @param coupling the new coupling
     */
    public void couplingAdded(Coupling coupling);

    /**
     * Called when a coupling is removed.
     *
     * @param coupling the coupling that is being removed
     */
    public void couplingRemoved(Coupling coupling);
}
