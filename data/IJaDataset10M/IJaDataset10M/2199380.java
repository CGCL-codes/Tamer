package org.jsmtpd.generic.threadpool;

public interface ThreadPool {

    /**
     * Will gracefully shutdown each running thread
     *
     */
    public abstract void gracefullShutdown();

    /**
     * Will force each thread to shutdown
     *
     */
    public abstract void forceShutdown();

    /**
     * 
     * @return true if any free thread
     */
    public abstract boolean hasFreeThread();

    /**
     * 
     */
    public abstract int countFreeThread();

    /**
     * passes the obj parameter to the thread instance, and runs its doJob mehtod
     * @param obj the object to pass
     * @throws BusyThreadPoolException when the pool is exhausted
     */
    public abstract void assignFreeThread(Object obj) throws BusyThreadPoolException;
}
