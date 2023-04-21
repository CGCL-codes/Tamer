package com.continuent.tungsten.commons.patterns.fsm;

/**
 * Denotes a class used to determine whether the conditions for a workflow
 * transition have been met.
 * 
 * @author <a href="mailto:robert.hodges@continuent.com">Robert Hodges</a>
 * @version 1.0
 */
public interface Guard {

    /**
     * Returns true if the message is accepted and we should take the transition
     * associated with this guard.
     * 
     * @param message A message that should be processed by this guard.
     * @param entity The entity whose state is being managed
     * @param state The current entity state
     * @return true if the message is accepted
     */
    public boolean accept(Event message, Entity entity, State state);
}
