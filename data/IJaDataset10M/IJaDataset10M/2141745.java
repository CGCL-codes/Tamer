package fbpwn.ui;

import fbpwn.core.FacebookTask;

/**
 * Represents Facebook GUI used for updating the status of each task 
 */
public interface FacebookGUI {

    /**
     * Updates a given task
     * @param task The task to be updated
     */
    public void updateTaskProgress(FacebookTask task);

    /**
     * Adds a new task to the GUI
     * @param task The task to be added
     */
    public void addTask(FacebookTask task);

    /**
     * Removes a given task from the GUI
     * @param task The task to be removed
     */
    public void removeTask(FacebookTask task);
}
