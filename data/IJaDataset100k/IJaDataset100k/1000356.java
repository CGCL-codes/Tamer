package org.jacp.api.coordinator;

import org.jacp.api.perspective.IPerspective;

/**
 * Notifies perspectives and components included in workbench.
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 * @author Andy Moncsek
 */
public interface IPerspectiveCoordinator<L, A, M> extends ICoordinator<L, A, M> {

    /**
     * Add the perspective to observe.
     * 
     * @param perspective
     */
    void addPerspective(final IPerspective<L, A, M> perspective);

    /**
     * Remove the perspective; e.g. when perspective is deactivated
     * 
     * @param perspective
     */
    void removePerspective(final IPerspective<L, A, M> perspective);
}
