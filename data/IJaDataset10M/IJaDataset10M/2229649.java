package com.hypermine.ultrasonic.ui.commons;

/**
 * Abstract base class for implementations of tasks than can be undone and
 * redone.
 * 
 * @author wschwitzer
 * @author $Author: wschwitzer $
 * @version $Rev: 152 $
 * @levd.rating GREEN Rev: 152
 */
public abstract class Task {

    /** The id that is assigned to the next task created. */
    private static int nextId;

    /**
	 * The id of this task initialized with the {@link #nextId} which is
	 * incremented.
	 */
    private final int id = nextId++;

    /**
	 * Executes the task. The effects of the execution can be undone by
	 * {@link #revert()}.
	 */
    public abstract void execute();

    /**
	 * Removes the effects of a preceding execution of this task.
	 */
    public abstract void revert();

    /**
	 * Returns a short text that explains the effect of the execution of this
	 * specific task.
	 */
    public abstract String getText();

    /**
	 * Returns the {@link #getText()} and the id.
	 */
    @Override
    public String toString() {
        return getText() + " (#" + id + ")";
    }

    /**
	 * Returns the id of this task which is unique in the running instance of
	 * this application. The sequence of identifiers grows strictly monotonic.
	 */
    public int getId() {
        return id;
    }
}
