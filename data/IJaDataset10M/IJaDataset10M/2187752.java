package org.opensourcephysics.numerics;

/**
 * ODEEventSolver is an interface for a Solver for ODE which accepts
* and deals with StateEvents
 *
 * @author       Francisco Esquembre (March 2004)
 */
public interface ODEEventSolver extends ODESolver {

    /**
   *  Adds a StateEvent to the list of events
   * @param event The event to be added
   */
    public void addEvent(StateEvent event);

    /**
   *  Removes a StateEvent from the list of events
   * @param event The event to be removed
   */
    public void removeEvent(StateEvent event);
}
