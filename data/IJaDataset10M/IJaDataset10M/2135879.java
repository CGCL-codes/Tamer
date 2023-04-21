package net.fortytwo.ripple.control;

import net.fortytwo.ripple.RippleException;
import org.apache.log4j.Logger;
import java.util.LinkedList;

public abstract class Task {

    private static final Logger LOGGER = Logger.getLogger(Task.class);

    private LinkedList<Task> children = null;

    private boolean finished = true, stopped = false;

    protected abstract void executeProtected() throws RippleException;

    protected abstract void stopProtected();

    public void execute() throws RippleException {
        if (!stopped) {
            executeProtected();
        }
        synchronized (this) {
            finished = true;
            notify();
        }
    }

    /**
	 * Note: it is possible to stop a task which has already finished executing
	 * (the effect is to stop any children which may still be executing).
	 */
    public synchronized void stop() {
        if (!stopped) {
            stopped = true;
            stopProtected();
            if (null != children) {
                for (Task child : children) {
                    child.stop();
                }
            }
        }
    }

    /**
	 * Note: should not be called outside of Scheduler.
	 */
    public void begin() {
        finished = false;
        stopped = false;
        if (null != children) {
            children.clear();
        }
    }

    /**
     * Adds a child task.
	 * Note: should not be called outside of Scheduler.
     * @param child  the task to add
     */
    public synchronized void addChild(final Task child) {
        if (finished) {
            LOGGER.error("attempted to add a child to a finished task");
        } else {
            if (null == children) {
                children = new LinkedList<Task>();
            }
            children.add(child);
        }
    }

    /**
     * Blocks until this task has finished.
     * @throws RippleException  if things go astray
     */
    public void waitUntilFinished() throws RippleException {
        synchronized (this) {
            if (!finished) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RippleException("interrupted while waiting for task to finish");
                }
            }
        }
    }
}
