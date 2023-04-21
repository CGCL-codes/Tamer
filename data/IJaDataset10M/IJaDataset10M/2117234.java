package com.itmusings.stm.model;

import com.itmusings.stm.State;

/**
 * This is an optional but strongly recommended interface for the context object to implement. Implementation 
 * is required for allowing some features such as actions with custom properties etc.
 * <p>If the user does not need access to these special features, he does not need to
 * implement this interface. 
 * @author Raja Shankar Kolluru
 *
 */
public interface FlowContext {

    public abstract void setCurrentState(State currentState);

    public abstract State getCurrentState();
}
