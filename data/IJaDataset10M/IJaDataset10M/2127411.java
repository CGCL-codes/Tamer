package jopt.js.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.variable.Activity;

/**
 * Action to remove a value from an activity's potential start times
 */
public class RemoveActivityStartTimeAction implements SearchAction {

    private Activity activity;

    private int val;

    /**
     * Creates a remove activity start time action
     * @param activity activity from which we are removing the start value
     * @param val value that we are removing as a possible start time for given activity
     */
    public RemoveActivityStartTimeAction(Activity activity, int val) {
        this.activity = activity;
        this.val = val;
    }

    /**
     * Sets the value that will removed from the start times of the associated activity
     * @param val the value
     */
    public void setVal(int val) {
        this.val = val;
    }

    /**
     * Removes the given value from the start times of the given activity
     * @return null as there is no further action necessary
     * @throws PropagationFailureException
     */
    public SearchAction performAction() throws PropagationFailureException {
        activity.removeStartTime(val);
        return null;
    }

    public String toString() {
        return "remove-start-time(" + activity + ", " + val + ")";
    }
}
