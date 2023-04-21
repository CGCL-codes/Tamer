package com.pcmsolutions.system;

/**
 *
 * @author  pmeehan
 */
public class IllegalStateTransitionException extends Exception {

    private String existingState;

    private String newState;

    public IllegalStateTransitionException(String existingState, String newState, String msg) {
        super(msg);
        this.existingState = existingState;
        this.newState = newState;
    }

    public String getExisitingState() {
        return existingState;
    }

    public String getNewState() {
        return newState;
    }
}
