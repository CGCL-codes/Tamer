package visad.util;

import java.util.ListIterator;
import java.util.Vector;

/** A pool of threads (with minimum and maximum limits on the number
 *  of threads) which can be used to execute any Runnable tasks.
 */
public class ThreadPool {

    private static final String DEFAULT_PREFIX = "Minnow";

    private static final int DEFAULT_MIN_THREADS = 5;

    private static final int DEFAULT_MAX_THREADS = 10;

    private int maxQueuedTasks = 3;

    private int minThreads;

    private int maxThreads;

    private Object threadLock = new Object();

    private Object doneLock = new Object();

    private boolean terminateThread = false;

    private Vector threads = new Vector();

    private Vector tasks = new Vector();

    private Vector busy_tasks = new Vector();

    private String prefix;

    private int nextID = 0;

    private class ThreadMinnow extends Thread {

        private ThreadPool parent = null;

        public ThreadMinnow(ThreadPool p) {
            parent = p;
            start();
        }

        public void run() {
            while (true) {
                Runnable r = parent.getTask();
                if (r != null) {
                    try {
                        r.run();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                    parent.releaseTask(r);
                    synchronized (threadLock) {
                        threadLock.notify();
                    }
                    synchronized (doneLock) {
                        doneLock.notifyAll();
                    }
                } else {
                    if (terminateThread) {
                        return;
                    }
                    try {
                        synchronized (threadLock) {
                            threadLock.wait();
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    /** Build a thread pool with the default thread name prefix
   *  and the default minimum and maximum numbers of threads
   */
    public ThreadPool() throws Exception {
        this(DEFAULT_PREFIX, DEFAULT_MIN_THREADS, DEFAULT_MAX_THREADS);
    }

    /** Build a thread pool with the specified thread name prefix, and
   *  the default minimum and maximum numbers of threads
   */
    public ThreadPool(String prefix) throws Exception {
        this(prefix, DEFAULT_MIN_THREADS, DEFAULT_MAX_THREADS);
    }

    /** Build a thread pool with the specified maximum number of
   *  threads, and the default thread name prefix and minimum number
   *  of threads
   */
    public ThreadPool(int max) throws Exception {
        this(DEFAULT_MIN_THREADS, max);
    }

    /** Build a thread pool with the specified minimum and maximum
   *  numbers of threads, and the default thread name prefix
   */
    public ThreadPool(int min, int max) throws Exception {
        this(DEFAULT_PREFIX, min, max);
    }

    /** Build a thread pool with the specified thread name prefix and
   *  minimum and maximum numbers of threads
   */
    public ThreadPool(String prefix, int min, int max) throws Exception {
        minThreads = min;
        maxThreads = max;
        if (minThreads > maxThreads) {
            throw new Exception("Maximum number of threads (" + maxThreads + ") is less than minimum number of threads (" + minThreads + ")");
        }
        this.prefix = prefix;
        for (int i = 0; i < minThreads; i++) {
            ThreadMinnow minnow = new ThreadMinnow(this);
            minnow.setName(prefix + "-" + nextID++);
            threads.addElement(minnow);
        }
    }

    /** return the number of tasks in the queue and that are running 
  * @return number of queued and active tasks
  */
    public int getTaskCount() {
        int count = 0;
        if (tasks != null) count += tasks.size();
        if (busy_tasks != null) count += busy_tasks.size();
        return count;
    }

    public void remove(Runnable r) {
        synchronized (tasks) {
            tasks.removeElement(r);
            busy_tasks.removeElement(r);
        }
    }

    /**
   * Has the thread pool been closed?
   *
   * @return <tt>true</tt> if the pool has been terminated.
   */
    public boolean isTerminated() {
        return terminateThread;
    }

    /** Add a task to the queue; tasks are executed as soon as a thread
   *  is available, in the order in which they are submitted
   */
    public void queue(Runnable r) {
        if (terminateThread) {
            throw new Error("Task queued after threads stopped");
        }
        int numTasks = 0;
        synchronized (tasks) {
            if (!tasks.contains(r)) {
                tasks.addElement(r);
                numTasks = tasks.size();
            } else {
            }
        }
        synchronized (threadLock) {
            if (numTasks > maxQueuedTasks) {
                if (threads != null && threads.size() < maxThreads) {
                    try {
                        Thread t = new ThreadMinnow(this);
                        t.setName(prefix + "-" + nextID++);
                        threads.addElement(t);
                        threadLock.notify();
                    } catch (SecurityException e) {
                    }
                } else {
                    threadLock.notifyAll();
                }
            } else {
                threadLock.notify();
            }
        }
    }

    /** Get the next task on the queue.<BR>
   *  This method is intended only for the use of client threads and
   *  should never be called by external objects.
   */
    Runnable getTask() {
        Runnable thisTask = null;
        synchronized (tasks) {
            int n = tasks.size();
            for (int i = 0; i < n; i++) {
                thisTask = (Runnable) tasks.elementAt(i);
                if (busy_tasks.contains(thisTask)) {
                    thisTask = null;
                } else {
                    tasks.removeElementAt(i);
                    busy_tasks.addElement(thisTask);
                    break;
                }
            }
        }
        return thisTask;
    }

    void releaseTask(Runnable r) {
        synchronized (tasks) {
            busy_tasks.removeElement(r);
        }
    }

    /** wait for currently-running tasks to finish */
    public boolean waitForTasks() {
        int timeout = tasks.size();
        if (Thread.currentThread() instanceof ThreadMinnow) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ie) {
            }
            return false;
        }
        while (tasks.size() > 0) {
            try {
                synchronized (doneLock) {
                    doneLock.wait();
                }
            } catch (InterruptedException e) {
            }
            if (timeout-- == 0) {
                break;
            }
        }
        return (timeout > 0);
    }

    /** increase the maximum number of pooled threads */
    public void setThreadMaximum(int num) throws Exception {
        if (num < maxThreads) {
            throw new Exception("Cannot decrease maximum number of threads");
        }
        maxThreads = num;
    }

    /** Stop all threads as soon as all queued tasks are completed */
    public void stopThreads() {
        if (terminateThread) {
            return;
        }
        terminateThread = true;
        synchronized (threadLock) {
            threadLock.notifyAll();
        }
        Vector oldthreads;
        ListIterator i;
        synchronized (threads) {
            oldthreads = threads;
            threads = null;
            i = oldthreads.listIterator();
        }
        while (i.hasNext()) {
            Thread t = (Thread) i.next();
            while (true) {
                synchronized (oldthreads) {
                    oldthreads.notifyAll();
                }
                try {
                    t.join();
                    break;
                } catch (InterruptedException e) {
                }
            }
            i.remove();
        }
    }
}
