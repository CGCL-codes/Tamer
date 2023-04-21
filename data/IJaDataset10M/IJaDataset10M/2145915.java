package org.synthful.automata;

/**
 * @author Blessed Geek
 */
public abstract class Verification implements VerificationInterface {

    public abstract boolean verify();

    /**
     * Sets the else state.
     */
    public void setElseState() {
    }

    /**
     * Sets the error state.
     * 
     * @param state
     *            the ErrorState
     */
    public void setErrorState(State state) {
        ErrorState = state;
    }

    /**
     * Gets the ErrorState.
     * 
     * @return the ErrorState as State
     */
    public State getErrorState() {
        return ErrorState;
    }

    /**
     * Sets the action.
     * 
     * @param action
     *            the Action
     */
    public void setAction(ActionInterface action) {
        Action = action;
    }

    /**
     * Invoke action.
     * 
     * @return true, if Invoke action successful
     */
    public boolean invokeAction() {
        if (Action != null) {
            Action.invoke();
            return true;
        }
        return false;
    }

    /**
     * Sets the else action.
     * 
     * @param action
     *            the ElseAction
     */
    public void setElseAction(ActionInterface action) {
        ElseAction = action;
    }

    /**
     * Invoke else action.
     * 
     * @return true, if Invoke else action successful
     */
    public boolean invokeElseAction() {
        if (ElseAction != null) {
            ElseAction.invoke();
            return true;
        }
        return false;
    }

    /** Variable Action. */
    public ActionInterface Action;

    /** Variable ElseAction. */
    public ActionInterface ElseAction;

    /** Variable ErrorState. */
    public State ErrorState;
}
