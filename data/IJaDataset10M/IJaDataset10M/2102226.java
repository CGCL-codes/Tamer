package be.kuleuven.cs.mop.domain.model.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Focus Work class
 * Defines the algorithm for executing the Focus Work Use Case
 * 
 * @Note: Template Method Pattern
 */
public abstract class FocusWorkAlgorithm {

    private final UserImpl user;

    public FocusWorkAlgorithm(final UserImpl user) {
        if (user == null) throw new NullPointerException("User == NULL");
        this.user = user;
    }

    /**
	 * The template algorithm method
	 * Executes the algorithm
	 * @return a <tt>List</tt> of <tt>Task</tt>s
	 */
    public List<TaskImpl> execute() {
        final List<TaskImpl> tasks = getTasks();
        sort(tasks);
        filter(tasks);
        return tasks;
    }

    /**
	 * Collects all applicable tasks for this algorithm
	 * All tasks the user owns or is invited on are returned
	 * @return a list of tasks
	 */
    protected List<TaskImpl> getTasks() {
        final List<TaskImpl> tasks = new ArrayList<TaskImpl>();
        tasks.addAll(getUser().getTasks());
        tasks.addAll(getUser().getInvitedTasks());
        return tasks;
    }

    /**
	 * Returns the User for this algorithm
	 * @return a User
	 */
    protected UserImpl getUser() {
        return user;
    }

    /**
	 * Sorts the specified list of tasks
	 * @Note: Primitive operation, to be implemented by subclasses
	 */
    protected abstract void sort(List<TaskImpl> tasks);

    /**
	 * Filters the specified sorted list of tasks
	 * @Note: Primitive operation, to be implemented by subclasses
	 */
    protected abstract void filter(List<TaskImpl> tasks);
}
