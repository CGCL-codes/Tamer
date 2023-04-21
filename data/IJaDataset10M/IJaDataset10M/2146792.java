package org.zeroexchange.flow;

import java.util.Map;

/**
 * @author black
 *
 */
public interface Flow<D> {

    String FLOWNAME_DEFAULT = "default";

    /**
     * Moves to first step.
     */
    void start(Map<String, ?> actionData);

    /**
     * Moves to the default next step.
     */
    void next();

    /**
     * Moves to the next step.
     */
    void next(String nextStepDiscriminator);

    /**
     * Moves to the next step.
     */
    void next(String nextStepDiscriminator, Map<String, Object> actionData);

    /**
     * Moves to the previous step from the stack.
     */
    Object prev();

    /**
     * Jumps to the one of previous steps.
     */
    void jumpBack(String stepDiscriminator);

    /**
     * Returns manager for the current step. 
     */
    <S> S getStepManager();
}
