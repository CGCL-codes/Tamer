package petrivis.view;

import prefuse.Visualization;
import prefuse.action.ActionList;

/**
 * Abstract Action List that creates and contains it own actions
 * @author Jorge Munoz
 */
public abstract class ActionListWithActions extends ActionList {

    /**
     * Creates a new run-once ActionList.
     */
    public ActionListWithActions() {
        super();
        initActions();
    }

    /**
     * Creates a new run-once ActionList that processes the given
     * Visualization.
     * @param vis the {@link prefuse.Visualization} to process.
     */
    public ActionListWithActions(Visualization vis) {
        super(vis);
        initActions();
    }

    /**
     * Creates a new ActionList of specified duration and default
     * step time of 20 milliseconds.
     * @param duration the duration of this Activity, in milliseconds
     */
    public ActionListWithActions(long duration) {
        super(duration);
        initActions();
    }

    /**
     * Creates a new ActionList which processes the given Visualization
     * and has the specified duration and a default step time of 20
     * milliseconds.
     * @param vis the {@link prefuse.Visualization} to process.
     * @param duration the duration of this Activity, in milliseconds
     */
    public ActionListWithActions(Visualization vis, long duration) {
        super(vis, duration);
        initActions();
    }

    /**
     * Creates a new ActionList of specified duration and step time.
     * @param duration the duration of this Activity, in milliseconds
     * @param stepTime the time to wait in milliseconds between executions
     *  of the action list
     */
    public ActionListWithActions(long duration, long stepTime) {
        super(duration, stepTime);
        initActions();
    }

    /**
     * Abstract function that must be override to create and add the actions
     * to the ActionList
     */
    protected abstract void initActions();
}
