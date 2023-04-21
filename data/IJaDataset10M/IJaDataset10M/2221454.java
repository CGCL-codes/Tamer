package net.sourceforge.jsxe.util;

/**
 * Services work requests in the background.
 * @author Slava Pestov
 * @version $Id$
 * @since jsXe 0.5 pre3
 */
public class WorkThread extends Thread {

    public WorkThread(WorkThreadPool pool, ThreadGroup group, String name) {
        super(group, name);
        setPriority(Thread.MIN_PRIORITY);
        this.pool = pool;
    }

    /**
     * Sets if the current request can be aborted.
     */
    public void setAbortable(boolean abortable) {
        synchronized (abortLock) {
            this.abortable = abortable;
            if (aborted) {
                stop(new Abort());
            }
        }
    }

    /**
     * Returns if the work thread is currently running a request.
     */
    public boolean isRequestRunning() {
        return requestRunning;
    }

    /**
     * Returns the status text.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status text.
     */
    public void setStatus(String status) {
        this.status = status;
        pool.fireProgressChanged(this);
    }

    /**
     * Returns the progress value.
     */
    public int getProgressValue() {
        return progressValue;
    }

    /**
     * Sets the progress value.
     */
    public void setProgressValue(int progressValue) {
        this.progressValue = progressValue;
        pool.fireProgressChanged(this);
    }

    /**
     * Returns the progress maximum.
     */
    public int getProgressMaximum() {
        return progressMaximum;
    }

    /**
     * Sets the maximum progress value.
     */
    public void setProgressMaximum(int progressMaximum) {
        this.progressMaximum = progressMaximum;
        pool.fireProgressChanged(this);
    }

    /**
     * Aborts the currently running request, if allowed.
     */
    public void abortCurrentRequest() {
        synchronized (abortLock) {
            if (abortable && !aborted) {
                stop(new Abort());
            }
            aborted = true;
        }
    }

    public void run() {
        Log.log(Log.DEBUG, this, "Work request thread starting [" + getName() + "]");
        for (; ; ) {
            doRequests();
        }
    }

    private WorkThreadPool pool;

    private Object abortLock = new Object();

    private boolean requestRunning;

    private boolean abortable;

    private boolean aborted;

    private String status;

    private int progressValue;

    private int progressMaximum;

    private void doRequests() {
        WorkThreadPool.Request request;
        for (; ; ) {
            request = pool.getNextRequest();
            if (request == null) {
                break;
            } else {
                requestRunning = true;
                pool.fireStatusChanged(this);
                doRequest(request);
                requestRunning = false;
            }
        }
        pool.fireStatusChanged(this);
        synchronized (pool.waitForAllLock) {
            pool.waitForAllLock.notifyAll();
        }
        synchronized (pool.lock) {
            try {
                pool.lock.wait();
            } catch (InterruptedException ie) {
                Log.log(Log.ERROR, this, ie);
            }
        }
    }

    private void doRequest(WorkThreadPool.Request request) {
        Log.log(Log.DEBUG, WorkThread.class, "Running in work thread: " + request);
        try {
            request.run.run();
        } catch (Abort a) {
            Log.log(Log.ERROR, WorkThread.class, "Unhandled abort");
        } catch (Throwable t) {
            Log.log(Log.ERROR, WorkThread.class, "Exception " + "in work thread:");
            Log.log(Log.ERROR, WorkThread.class, t);
        } finally {
            synchronized (abortLock) {
                aborted = abortable = false;
            }
            status = null;
            progressValue = progressMaximum = 0;
            pool.requestDone();
            pool.fireStatusChanged(this);
        }
    }

    public static class Abort extends Error {

        public Abort() {
            super("Work request aborted");
        }
    }
}
