package edu.drexel.sd0910.ece01.aqmon.manager;

/**
 * Interface to allow a <code>{@link Manager}</code> to control tasks.
 * 
 * @author Kyle O'Connor
 * 
 */
public interface ManagerTaskController {

    /**
	 * Starts all tasks required by the <code>Manager</code>.
	 */
    void startAllTasks();

    /**
	 * Stops all tasks required by the <code>Manager</code>.
	 */
    void stopAllTasks();
}
