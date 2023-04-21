package org.epistasis;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Manages a group of threads. The threads in the pool will be started at the
 * same time and interrupted at the same time, and the pool itself will wait
 * until all threads terminate before terminating itself.
 */
public class ThreadPool extends AbstractList implements Runnable {

    /** Indicates whether the pool is running */
    private boolean running = false;

    /** The list of threads in the pool */
    private List threads = Collections.synchronizedList(new ArrayList());

    /**
	 * Add code to the pool with the default priority. This should not be called
	 * while the pool is running.
	 * 
	 * @param r
	 *            Code to run on a thread in the pool
	 */
    public void add(Runnable r) {
        add(r, Thread.NORM_PRIORITY);
    }

    /**
	 * Add code to the pool. This should not be called while the pool is
	 * running.
	 * 
	 * @param r
	 *            Code to run on a thread in the pool
	 * @param priority
	 *            Priority for thread
	 */
    public void add(Runnable r, int priority) {
        if (running) {
            throw new IllegalStateException("ThreadPool is running.");
        }
        Thread t = new Thread(r);
        t.setPriority(priority);
        threads.add(t);
    }

    /**
	 * Clear the threads from the pool. This should not be called while the pool
	 * is running.
	 */
    public void clear() {
        if (running) {
            throw new IllegalStateException("ThreadPool is running.");
        }
        threads.clear();
    }

    /**
	 * Start the thread pool.
	 */
    public void run() {
        running = true;
        for (Iterator i = threads.iterator(); i.hasNext(); ) {
            ((Thread) i.next()).start();
        }
        try {
            for (Iterator i = threads.iterator(); i.hasNext(); ) {
                ((Thread) i.next()).join();
            }
        } catch (InterruptedException ex) {
            interrupt();
            Thread.currentThread().interrupt();
        }
        running = false;
    }

    /**
	 * Interrupt all threads in the pool. This may not stop the threads if they
	 * mask interrupts.
	 */
    public void interrupt() {
        if (!running) {
            return;
        }
        for (Iterator i = threads.iterator(); i.hasNext(); ) {
            ((Thread) i.next()).interrupt();
        }
        running = false;
    }

    public int size() {
        return threads.size();
    }

    public Object get(int index) {
        return threads.get(index);
    }

    public Object remove(int index) {
        return threads.remove(index);
    }
}
