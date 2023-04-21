package ru.adv.test.util.thread;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Java Thread Pool
 * 
 * This is a thread pool that for Java, it is simple to use and gets the job
 * done. This program and all supporting files are distributed under the Limited
 * GNU Public License (LGPL, http://www.gnu.org).
 * 
 * This is the main class for the thread pool. You should create an instance of
 * this class and assign tasks to it.
 * 
 * For more information visit http://www.jeffheaton.com.
 * 
 * @author Jeff Heaton (http://www.jeffheaton.com)
 * @version 1.0
 */
public class ThreadPool {

    /**
	 * The threads in the pool.
	 */
    protected Thread threads[] = null;

    /**
	 * The backlog of assignments, which are waiting for the thread pool.
	 */
    Collection assignments = new ArrayList(3);

    /**
	 * A Done object that is used to track when the thread pool is done, that is
	 * has no more work to perform.
	 */
    protected Done done = new Done();

    private boolean destroyed = false;

    /**
	 * The constructor.
	 * 
	 * @param size
	 *            How many threads in the thread pool.
	 */
    public ThreadPool(int size) {
        threads = new WorkerThread[size];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new WorkerThread(this, i + 1);
            threads[i].start();
        }
    }

    /**
	 * Add a task to the thread pool. Any class which implements the Runnable
	 * interface may be assienged. When this task runs, its run method will be
	 * called.
	 * 
	 * @param r
	 *            An object that implements the Runnable interface
	 */
    public synchronized void assign(Runnable r) {
        done.workerBegin();
        assignments.add(r);
        notify();
    }

    /**
	 * Get a new work assignment.
	 * 
	 * @return A new assignment
	 */
    public synchronized Runnable getAssignment() {
        try {
            while (!assignments.iterator().hasNext()) wait();
            Runnable r = (Runnable) assignments.iterator().next();
            assignments.remove(r);
            return r;
        } catch (InterruptedException e) {
            done.workerEnd();
            return null;
        }
    }

    /**
	 * Called to block the current thread until the thread pool has no more
	 * work.
	 */
    public void complete() {
        done.waitBegin();
        done.waitDone();
    }

    public synchronized void destroy() {
        if (!destroyed) {
            done.reset();
            for (int i = 0; i < threads.length; i++) {
                threads[i].interrupt();
            }
            destroyed = true;
        }
    }

    protected void finalize() {
        destroy();
    }
}
