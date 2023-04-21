package org.horen.task;

/**
 * <p>A task visitor is a class that handles the visiting of a task during a task traverse
 * operation.</p>
 * 
 * @see TaskTraverser
 * @author Steffen
 */
public interface ITaskVisitor {

    /**
	 * <p>Visits the given task and executes a visitor depending operation for that task.
	 * E.g. a visitor for statistics may increments a counter variable.</p>
	 * 
	 * @param task the task to visit
	 * @return <code>true</code> to continue traversing tasks or <code>false</code> to
	 * abort traverse operation.
	 */
    public boolean visit(Task task);
}
