package pl.prv.consept.gestionnaire.dialogs;

public interface Task {

    /**
	 * Starts a task
	 */
    public void go();

    /** 
	 * Returns length of task
	 */
    public int getLengthOfTask();

    /**
	 * Called from ProgressBarDemo to find out how much has been done.
	 */
    public int getCurrent();

    /**
	 * Stops the task.
	 */
    public void stop();

    /**
	 * Cleans
	 */
    public void cleanUp();

    /**
	 * Called from ProgressBar to find out if the task has completed.
	 */
    public boolean isDone();

    /**
	 * Called from ProgressBar to find out if the task has been canceled.
	 */
    public boolean isCanceled();

    /**
	 * Returns the most recent status message, or null
	 * if there is no current status message.
	 */
    public String getMessage();

    /**
	 * Returns the exception that occured if any
	 *
	 * @return exception
	 */
    public Exception getException();
}
