package de.enough.polish.util;

import java.util.Vector;

/**
 * <p>Processes tasks asynchronously</p>
 *
 * <p>Copyright Enough Software 2007 - 2009</p>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class TaskThread extends Thread {

    private static TaskThread INSTANCE;

    private final Vector queue;

    private boolean stopRequested;

    private TaskThread() {
        this.queue = new Vector();
    }

    /**
	 * Retrieves the running instance of this thread.
	 * 
	 * @return the running instance (singleton pattern)
	 */
    public static TaskThread getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskThread();
            INSTANCE.start();
        }
        return INSTANCE;
    }

    /**
	 * Adds a task that should be executed in a background thread.
	 * 
	 * @param task the task
	 */
    public void addTask(Task task) {
        this.queue.addElement(task);
        synchronized (this.queue) {
            this.queue.notify();
        }
    }

    public void run() {
        while (!this.stopRequested) {
            while (this.queue.size() != 0) {
                Task task = (Task) this.queue.elementAt(0);
                this.queue.removeElementAt(0);
                try {
                    task.execute();
                } catch (Exception e) {
                    de.enough.polish.util.Debug.debug("error", "de.enough.polish.util.TaskThread", 81, "Unable to execute task " + task, e);
                }
            }
            synchronized (this.queue) {
                try {
                    this.queue.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
