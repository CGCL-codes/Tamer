package org.tigr.antware.shared.concurrent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.tigr.antware.shared.util.Logger;

/**
 * The class <code>ThreadPool</code> description here.
 */
public class ThreadPool extends Object {

    /**
     * The variable <code>logger</code> holds the instance of the logger for this class.
     */
    private static Logger logger = new Logger(ThreadPool.class);

    /**
     * The maximum pool size; used if not otherwise specified.  Default
     * value is essentially infinite (Integer.MAX_VALUE)
     **/
    public static final int DEFAULT_MAX_POOL_SIZE = 50;

    /**
     * The minimum pool size; used if not otherwise specified.  Default
     * value is 1.
     **/
    public static final int DEFAULT_MIN_POOL_SIZE = 5;

    /**
     * The maximum time to keep worker threads alive waiting for new
     * tasks; used if not otherwise specified. Default value is one
     * minute (60000 milliseconds).
     **/
    public static final long DEFAULT_KEEPALIVETIME = 60 * 1000;

    /** The maximum number of threads allowed in pool. **/
    protected int maxPoolSize = DEFAULT_MAX_POOL_SIZE;

    /** The minimum number of threads to maintain in pool. **/
    protected int minPoolSize = DEFAULT_MIN_POOL_SIZE;

    /** The maximum number of threads that can be idle */
    protected int maxActiveIdlePoolSize = DEFAULT_MIN_POOL_SIZE;

    /**  Current pool size.  **/
    protected int poolSize = 0;

    /** The maximum time for an idle thread to wait for new task. **/
    protected long keepAliveTime = DEFAULT_KEEPALIVETIME;

    private ThreadPoolWorker worker = null;

    private String name = null;

    private Object threadLock = new Object();

    /**
     * The set of active threads, declared as a map from workers to
     * their threads.  This is needed by the interruptAll method.  It
     * may also be useful in subclasses that need to perform other
     * thread management chores.
     **/
    protected Map<Integer, ThreadPoolWorker> idleWorkerPool;

    protected Map<Integer, ThreadPoolWorker> activeWorkerPool;

    protected ThreadPool(String name) {
        this(name, DEFAULT_MIN_POOL_SIZE, DEFAULT_MAX_POOL_SIZE);
    }

    protected ThreadPool(String name, int maxPoolSize) {
        this(name, DEFAULT_MIN_POOL_SIZE, maxPoolSize);
    }

    protected ThreadPool(String name, int minPoolSize, int maxPoolSize) {
        if (logger.isFinerEnabled()) {
            logger.finer("Creating thread pool " + name + " min: " + minPoolSize + " max: " + maxPoolSize);
        }
        this.name = name;
        this.maxPoolSize = maxPoolSize;
        this.minPoolSize = minPoolSize;
        this.idleWorkerPool = new HashMap<Integer, ThreadPoolWorker>();
        this.activeWorkerPool = new HashMap<Integer, ThreadPoolWorker>();
        createThreads(minPoolSize);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the maximum number of threads to simultaneously execute
     * New un-queued requests will be handled according to the current
     * blocking policy once this limit is exceeded.
     **/
    public synchronized int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * Set the maximum number of threads to use. Decreasing the pool
     * size will not immediately kill existing threads, but they may
     * later die when idle.
     * @exception IllegalArgumentException if less or equal to zero.
     * (It is not considered an error to set the maximum to be less than than
     * the minimum. However, in this case there are no guarantees
     * about behavior.)
     **/
    public synchronized void setMaxPoolSize(int newMax) {
        if (newMax <= 0) throw new IllegalArgumentException();
        maxPoolSize = newMax;
    }

    /**
     * Return the minimum number of threads to simultaneously execute.
     * (Default value is 1).  If fewer than the minimum number are
     * running upon reception of a new request, a new thread is started
     * to handle this request.
     **/
    public synchronized int getMinPoolSize() {
        return minPoolSize;
    }

    /**
     * Set the minimum number of threads to use.
     * @exception IllegalArgumentException if less than zero. (It is not
     * considered an error to set the minimum to be greater than the
     * maximum. However, in this case there are no guarantees about
     * behavior.)
     **/
    public synchronized void setMinPoolSize(int newMin) {
        if (newMin < 0) throw new IllegalArgumentException();
        minPoolSize = newMin;
    }

    /**
     * Return the current number of active threads in the pool.  This
     * number is just a snapshot, and may change immediately upon
     * returning
     **/
    public synchronized int getPoolSize() {
        return activeWorkerPool.size() + idleWorkerPool.size();
    }

    public synchronized int getActivePoolSize() {
        return activeWorkerPool.size();
    }

    public synchronized int getIdlePoolSize() {
        return idleWorkerPool.size();
    }

    /**
     * Return the number of milliseconds to keep threads alive waiting
     * for new commands. A negative value means to wait forever. A zero
     * value means not to wait at all.
     **/
    public synchronized long getKeepAliveTime() {
        return keepAliveTime;
    }

    /**
     * Set the number of milliseconds to keep threads alive waiting for
     * new commands. A negative value means to wait forever. A zero
     * value means not to wait at all.
     **/
    public synchronized void setKeepAliveTime(long msecs) {
        keepAliveTime = msecs;
    }

    /**
     * Create and start a thread to handle a new command.  Call only
     * when holding lock.
     **/
    public synchronized void addIdleWorker() {
        worker = null;
        worker = new ThreadPoolWorker(ThreadPoolManager.maxWorkerID++, this);
        if (logger.isFinestEnabled()) {
            logger.finest("Added worker with ID: " + worker.getWorkerID() + " to pool: " + name);
        }
        idleWorkerPool.put(worker.getWorkerID(), worker);
    }

    /**
     * Create and start up to numberOfThreads threads in the pool.
     * Return the number created. This may be less than the number
     * requested if creating more would exceed maximum pool size bound.
     **/
    protected int createThreads(int numberOfThreads) {
        if (logger.isFinerEnabled()) {
            logger.finer("Attempting to create thread count: " + numberOfThreads);
        }
        int ncreated = 0;
        int poolSize = 0;
        for (int i = 0; i < numberOfThreads; ++i) {
            synchronized (this) {
                poolSize = this.getPoolSize();
                if (poolSize < maxPoolSize) {
                    addIdleWorker();
                    ++ncreated;
                } else break;
            }
        }
        return ncreated;
    }

    public synchronized ThreadPoolWorker getWorker() throws InterruptedException {
        if (logger.isFinerEnabled()) {
            logger.finer("Attempting to retrieve worker for pool: " + name);
        }
        ThreadPoolWorker worker = null;
        if (!isWorkerAvailable()) {
            return worker;
        }
        Iterator<Integer> itr = idleWorkerPool.keySet().iterator();
        if (itr.hasNext()) {
            Integer key = (Integer) itr.next();
            if (logger.isFinestEnabled()) {
                logger.finest("Obtaining Worker with ID: " + key);
            }
            worker = (ThreadPoolWorker) (idleWorkerPool.get(key));
            while (!worker.isWorkerInitialized()) {
                Thread.sleep(10);
            }
            if (logger.isFinestEnabled()) {
                logger.finest("Worker with ID: " + key + " is initialized.");
            }
            idleWorkerPool.remove(key);
            activeWorkerPool.put(key, worker);
        }
        return worker;
    }

    private synchronized boolean isWorkerAvailable() {
        boolean threadAvailable = false;
        if (getPoolSize() < getMaxPoolSize()) {
            if (getIdlePoolSize() < 1) {
                if (logger.isFinestEnabled()) {
                    logger.finest("The idle pool size has dropped to 0. Create a worker thread.");
                }
                createThreads(1);
            }
            threadAvailable = true;
        } else if (getPoolSize() == getMaxPoolSize()) {
            if (getIdlePoolSize() > 0) {
                threadAvailable = true;
            }
        }
        return threadAvailable;
    }

    /**
     * Interrupt all threads in the pool, causing them all to
     * terminate. Assuming that executed tasks do not disable (clear)
     * interruptions, each thread will terminate after processing its
     * current task. Threads will terminate sooner if the executed tasks
     * themselves respond to interrupts.
     **/
    public synchronized void interruptAll() {
        ThreadPoolWorker t = null;
        for (Iterator<ThreadPoolWorker> it = activeWorkerPool.values().iterator(); it.hasNext(); ) {
            t = (ThreadPoolWorker) (it.next());
            t.interrupt();
        }
        for (Iterator<ThreadPoolWorker> it = idleWorkerPool.values().iterator(); it.hasNext(); ) {
            t = (ThreadPoolWorker) (it.next());
            t.interrupt();
        }
    }

    /**
     * The method <code>workerDone</code> is called after the specified thread pool
     * worker completes its work. The method moves this worker from the active pool to
     * the idle pool and sends a notification to all waiting processes that threads
     * have become available 
     * 
     * @param worker a <code>ThreadPoolWorker</code> whose work is complete.
     */
    public synchronized void workerDone(ThreadPoolWorker worker) {
        if (logger.isFinerEnabled()) {
            logger.finer("Worker with " + worker.getWorkerID() + " is done.");
        }
        if (activeWorkerPool.remove(worker.getWorkerID()) != null) {
            try {
                idleWorkerPool.put(worker.getWorkerID(), worker);
                if (logger.isFinerEnabled()) {
                    logger.finer("Added Worker with " + worker.getWorkerID() + " to idle pool again.");
                }
                notifyThreadCompletion();
            } catch (InterruptedException ie) {
            }
        } else {
            if (logger.isFinerEnabled()) {
                logger.finer("Worker with " + worker.getWorkerID() + " was not active when workerDone was called.");
            }
        }
    }

    /**
     * The method <code>terminateWorker</code> terminates the specified worker by removing it from the
     * idle pool so it is no longer available for performing work.
     * 
     * @param worker a <code></code>
     */
    public synchronized void terminateWorker(ThreadPoolWorker worker) {
        if (logger.isFinerEnabled()) {
            logger.finer("Worker with " + worker.getWorkerID() + " is being terminated.");
        }
        idleWorkerPool.remove(worker.getWorkerID());
    }

    public synchronized void terminateAllWorkers() {
        ThreadPoolWorker worker = null;
        Iterator<ThreadPoolWorker> itr = activeWorkerPool.values().iterator();
        while (itr.hasNext()) {
            worker = null;
            worker = (ThreadPoolWorker) itr.next();
            activeWorkerPool.remove(worker.getWorkerID());
        }
        itr = null;
        itr = idleWorkerPool.values().iterator();
        while (itr.hasNext()) {
            worker = null;
            worker = (ThreadPoolWorker) itr.next();
            activeWorkerPool.remove(worker.getWorkerID());
        }
    }

    /**
     * The <code>stopRequestAllWorkers</code> method here.
     *
     */
    public synchronized void stopRequestAllWorkers() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException x) {
        }
        ThreadPoolWorker worker = null;
        Iterator<ThreadPoolWorker> itr = activeWorkerPool.values().iterator();
        while (itr.hasNext()) {
            worker = null;
            worker = (ThreadPoolWorker) itr.next();
            if (worker.isAlive()) {
                worker.stopRequest();
            }
        }
        itr = null;
        itr = idleWorkerPool.values().iterator();
        while (itr.hasNext()) {
            worker = null;
            worker = (ThreadPoolWorker) itr.next();
            if (worker.isAlive()) {
                worker.stopRequest();
            }
        }
    }

    public synchronized boolean isActivePool() {
        if (getActivePoolSize() > 0) {
            return true;
        }
        return false;
    }

    public synchronized void releaseAll() {
        ThreadPoolWorker t = null;
        if (logger.isFinerEnabled()) {
            logger.finer("The active thread pool count " + activeWorkerPool.size() + " and Idle thread pool count " + idleWorkerPool.size());
        }
        for (Iterator<ThreadPoolWorker> it = activeWorkerPool.values().iterator(); it.hasNext(); ) {
            t = (ThreadPoolWorker) (it.next());
            if (logger.isFinerEnabled()) {
                logger.finer("Active Pool : Setting the noStopRequested flag of " + t.getName() + " to false");
            }
            t.setNoStopRequested(false);
        }
        for (Iterator<ThreadPoolWorker> it = idleWorkerPool.values().iterator(); it.hasNext(); ) {
            t = (ThreadPoolWorker) (it.next());
            if (logger.isFinerEnabled()) {
                logger.finer("Idle Pool : Setting the noStopRequested flag of " + t.getName() + " to false");
            }
            t.setNoStopRequested(false);
        }
    }

    /**
     * The <code>printPool</code> method here.
     *
     */
    public synchronized void printPool() {
        System.out.println();
        System.out.println("###########################");
        System.out.println();
        System.out.println("Name                :" + getName());
        System.out.println("Maximum Pool Size   :" + getMaxPoolSize());
        System.out.println("Minimum Pool Size   :" + getMinPoolSize());
        System.out.println("Current Pool Size   :" + getPoolSize());
        System.out.println("# of Idle Workers   :" + getIdlePoolSize());
        System.out.println("# of Active Workers :" + getActivePoolSize());
        System.out.println();
        System.out.println("###########################");
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("###########################").append("\n").append("Name                : " + getName()).append("\n").append("Maximum Pool Size   : " + getMaxPoolSize()).append("\n").append("Minimum Pool Size   : " + getMinPoolSize()).append("\n").append("Current Pool Size   : " + getPoolSize()).append("\n").append("Idle Workers        : " + getIdlePoolSize()).append("\n").append("Active Workers      : " + getActivePoolSize()).append("\n").append("###########################").append("\n");
        return sbuf.toString();
    }

    /**
     * The <code>waitForThreadCompletion</code> method here.
     *
     * @exception InterruptedException if an error occurs
     */
    public void waitForThreadCompletion() throws InterruptedException {
        synchronized (threadLock) {
            threadLock.wait();
        }
    }

    /**
     * The <code>notifyThreadCompletion</code> method here.
     *
     * @exception InterruptedException if an error occurs
     */
    public void notifyThreadCompletion() throws InterruptedException {
        synchronized (threadLock) {
            threadLock.notifyAll();
        }
    }
}
