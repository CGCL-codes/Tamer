package org.baatar.thread;

/**
 * Base abstract thread manager class.
 * 
 * @author b_zeren
 */
public abstract class ThreadManager implements Runnable {

    private static int threadCounter = 0;

    private static synchronized int getNewThreadCounter() {
        return threadCounter++;
    }

    protected boolean initialized = false;

    protected Thread thread;

    private ThreadStatus actualThreadStatus = ThreadStatus.tsHasNotBeenRunYet;

    private ThreadStatus requestedThreadStatus = ThreadStatus.tsHasNotBeenRunYet;

    private ThreadStatusListener threadStatusListener = null;

    /**
     * Name of the encapsulated thread object.
     * 
     */
    protected String threadName = null;

    /**
     * A boolean value that indicates whether this ThreadManager object
     * is initialized.
     * 
     * @return boolean true if this ThreadManager object is initialized or false 
     * otherwise.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Main thread "run" method.
     * 
     * @see Runnable
     */
    @Override
    public void run() {
        try {
            setRequestedThreadStatus(ThreadStatus.tsInProgress);
            setActualThreadStatus();
            goAhead();
            if (this.actualThreadStatus == ThreadStatus.tsInProgress) {
                setRequestedThreadStatus(ThreadStatus.tsSucceeded);
                setActualThreadStatus();
            }
        } catch (Exception ex) {
            setRequestedThreadStatus(ThreadStatus.tsFailed);
            setActualThreadStatus();
        }
    }

    /**
     * Starts thread process if initialization is successful.
     * 
     * @return boolean true if thread is started successfully.
     */
    public synchronized boolean start() {
        if (initialized) {
            if ((this.actualThreadStatus == ThreadStatus.tsHasNotBeenRunYet) && (this.requestedThreadStatus == this.actualThreadStatus)) {
                thread.start();
                return true;
            }
            if (this.requestedThreadStatus == ThreadStatus.tsInProgress) return true;
            if ((this.actualThreadStatus == ThreadStatus.tsInProgress) && (this.actualThreadStatus != this.requestedThreadStatus)) {
                this.requestedThreadStatus = ThreadStatus.tsInProgress;
                return true;
            }
        }
        return false;
    }

    /**
     * Suspends thread process if it is already running.
     * 
     * @return boolean true if thread is suspended successfully.
     */
    public synchronized boolean suspend() {
        if (initialized) {
            if ((this.actualThreadStatus == ThreadStatus.tsInProgress) && (this.requestedThreadStatus == this.actualThreadStatus)) {
                setRequestedThreadStatus(ThreadStatus.tsSuspended);
                return true;
            }
            if (this.requestedThreadStatus == ThreadStatus.tsSuspended) return true;
            if ((this.actualThreadStatus == ThreadStatus.tsSuspended) && (this.actualThreadStatus != this.requestedThreadStatus)) {
                this.requestedThreadStatus = ThreadStatus.tsSuspended;
                return true;
            }
        }
        return false;
    }

    /**
     * Resumes thread process if it is already suspended.
     * 
     * @return boolean true if thread is resumed successfully.
     */
    public synchronized boolean resume() {
        if (initialized) {
            if ((this.actualThreadStatus == ThreadStatus.tsSuspended) && (this.requestedThreadStatus == this.actualThreadStatus)) {
                setRequestedThreadStatus(ThreadStatus.tsInProgress);
                this.notify();
                return true;
            }
            if (this.requestedThreadStatus == ThreadStatus.tsInProgress) return true;
            if ((this.actualThreadStatus == ThreadStatus.tsInProgress) && (this.actualThreadStatus != this.requestedThreadStatus)) {
                this.requestedThreadStatus = ThreadStatus.tsInProgress;
                return true;
            }
        }
        return false;
    }

    /**
     * Stops thread process if it is already running or suspended.
     * 
     * @return boolean true if thread is stopped successfully.
     */
    public synchronized boolean stop() {
        if (initialized) {
            if (((this.actualThreadStatus == ThreadStatus.tsInProgress) || (this.actualThreadStatus == ThreadStatus.tsSuspended)) && (this.requestedThreadStatus == this.actualThreadStatus)) {
                boolean suspended = this.actualThreadStatus == ThreadStatus.tsSuspended;
                setRequestedThreadStatus(ThreadStatus.tsStopped);
                if (suspended) {
                    this.notify();
                }
                return true;
            }
            if (this.requestedThreadStatus == ThreadStatus.tsStopped) return true;
            if ((this.actualThreadStatus == ThreadStatus.tsStopped) && (this.actualThreadStatus != this.requestedThreadStatus)) {
                this.requestedThreadStatus = ThreadStatus.tsStopped;
                return true;
            }
        }
        return false;
    }

    /**
     * Internal method which checks if this ThreadManager object is suspended or 
     * process is canceled.
     * 
     * @returns boolean true if process is canceled or false otherwise.
     */
    protected synchronized boolean processCancelled() throws InterruptedException {
        if (this.requestedThreadStatus != this.actualThreadStatus) {
            setActualThreadStatus();
        }
        if (this.actualThreadStatus == ThreadStatus.tsStopped) {
            return true;
        }
        while ((this.actualThreadStatus == ThreadStatus.tsSuspended) && (this.requestedThreadStatus == ThreadStatus.tsSuspended)) {
            this.wait();
        }
        if (this.requestedThreadStatus != this.actualThreadStatus) {
            setActualThreadStatus();
        }
        return this.actualThreadStatus == ThreadStatus.tsStopped;
    }

    /**
     * Internal method which allows this ThreadManager object to wait for a 
     * desired period of time.
     * 
     * @param timeout the time period to wait in milliseconds.
     */
    protected synchronized void waitForAPeriod(int timeout) throws InterruptedException {
        this.wait(timeout);
    }

    /**
     * Performs initialization tasks of the thread manager object.
     * 
     * @param threadName name property to assign thread object.
     * 
     * @return boolean true if initialization is successful.
     */
    public boolean initialize() throws Exception {
        if ((this.actualThreadStatus == ThreadStatus.tsHasNotBeenRunYet) && (this.requestedThreadStatus == ThreadStatus.tsHasNotBeenRunYet) && !initialized) {
            if (threadName == null) threadName = "Thread" + String.valueOf(getNewThreadCounter());
            if (initializeManager()) {
                thread = new Thread(this, threadName);
                initialized = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Finalizes this thread manager object.
     * 
     */
    public void uninitialize() throws Exception {
        if ((this.actualThreadStatus != ThreadStatus.tsInProgress) && (this.requestedThreadStatus != ThreadStatus.tsInProgress) && initialized) {
            thread.join();
            thread = null;
            setRequestedThreadStatus(ThreadStatus.tsHasNotBeenRunYet);
            setActualThreadStatus();
            finalizeManager();
            initialized = false;
        }
    }

    /**
     * Abstract method for initialization specific needs.
     * 
     * @returns boolean true if initialization is successful.
     */
    protected abstract boolean initializeManager() throws Exception;

    /**
     * Abstract method for finalization specific needs.
     * 
     */
    protected abstract void finalizeManager() throws Exception;

    /**
     * Abstract method for the main work process of this ThreadManager object 
     * which will work in the "run" method comes from Runnable interface.
     * 
     * @see Runnable
     * 
     */
    protected abstract void goAhead() throws Exception;

    /**
     * Abstract method to be used when there is specific tasks to be done while 
     * status of this ThreadManager object is being changed.
     * 
     * @param oldThreadStatus previous value of the status of this ThreadManager 
     * object.
     * 
     * @param newThreadStatus new value of the status of this ThreadManager 
     * object.
     */
    protected abstract void statusIsBeingChanged(ThreadStatus oldThreadStatus, ThreadStatus newThreadStatus);

    /**
     * Abstract method to be used if there is specific tasks to be done when 
     * status of this ThreadManager object is changed.
     * 
     * @param oldThreadStatus previous value of the status of this ThreadManager 
     * object.
     * 
     * @param newThreadStatus new value of the status of this ThreadManager 
     * object.
     */
    protected abstract void statusChanged(ThreadStatus oldThreadStatus, ThreadStatus newThreadStatus);

    /**
     * The thread instance encapsulated by this ThreadManager object.
     * 
     * @see Thread
     * 
     * @returns Thread instance which is encapsulated by this ThreadManager 
     * object.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * Checks whether this ThreadManager object is stopped or not.
     * 
     * @returns boolean true if this ThreadManager object is stopped, false 
     * otherwise.
     */
    public boolean isStopped() {
        return this.actualThreadStatus == ThreadStatus.tsStopped;
    }

    /**
     * Checks whether this ThreadManager object is suspended or not.
     * 
     * @returns boolean true if this ThreadManager object is suspended, false 
     * otherwise.
     */
    public boolean isSuspended() {
        return this.actualThreadStatus == ThreadStatus.tsSuspended;
    }

    /**
     * Checks whether this ThreadManager object is succeeded or not.
     * 
     * @returns boolean true if this ThreadManager object is succeeded, false 
     * otherwise.
     */
    public boolean isSucceeded() {
        return this.actualThreadStatus == ThreadStatus.tsSucceeded;
    }

    /**
     * Checks whether this ThreadManager object is in progress or not.
     * 
     * @returns boolean true if this ThreadManager object is in progress, false 
     * otherwise.
     */
    public boolean isInProgress() {
        return this.actualThreadStatus == ThreadStatus.tsInProgress;
    }

    /**
     * Internal method which sets the requestedThreadStatus of this 
     * ThreadManager object.
     * 
     */
    private void setRequestedThreadStatus(ThreadStatus threadStatus) {
        if (threadStatusListener != null) threadStatusListener.OnThreadStatusIsBeingChanged(this.actualThreadStatus, this.requestedThreadStatus);
        this.requestedThreadStatus = threadStatus;
        statusIsBeingChanged(this.actualThreadStatus, this.requestedThreadStatus);
    }

    /**
     * Internal method which sets the actualThreadStatus as 
     * requestedThreadStatus.
     * 
     */
    private void setActualThreadStatus() {
        ThreadStatus oldStatus = this.actualThreadStatus;
        this.actualThreadStatus = this.requestedThreadStatus;
        if (threadStatusListener != null) threadStatusListener.OnThreadStatusChange(oldStatus, this.requestedThreadStatus);
        statusChanged(oldStatus, this.requestedThreadStatus);
    }

    /**
     * The ThreadStatusListener instance which monitors status change of
     * this ThreadManager object.
     * 
     * @see ThreadStatusListener
     * 
     * @return ThreadStatusListener of this ThreadManager object. If one was 
     * created and has already been assigned.
     */
    public ThreadStatusListener getThreadStatusListener() {
        return threadStatusListener;
    }

    /**
     * Sets the ThreadStatusListener instance which will monitors status change 
     * of this ThreadManager object.
     * 
     * @param threadStatusListener ThreadStatusListener instance to be
     * assigned to this ThreadManager object.
     * 
     * @see ThreadStatusListener
     * 
     */
    public void setThreadStatusListener(ThreadStatusListener threadStatusListener) {
        this.threadStatusListener = threadStatusListener;
    }

    /**
     * Name of the encapsulated thread object.
     * 
     * @return String name of the thread object.
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * @param threadName the threadName to set
     */
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
