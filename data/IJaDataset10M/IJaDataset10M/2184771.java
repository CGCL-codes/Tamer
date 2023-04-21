package com.sun.star.lib.uno.environments.remote;

/**
 * This interface is an abstraction of the various
 * threadpool implementations.
 * <p>
 * @version 	$Revision$ $ $Date$
 * @author 	    Joerg Budischewski
 * @author 	    Kay Ramme
 * @see         com.sun.star.lib.uno.environments.remote.ThreadPoolFactory
 * @see         com.sun.star.lib.uno.environments.remote.IThreadPoolFactory
 * @since       UDK1.0
 */
public interface IThreadPool {

    /**
     * Retrieves the global threadId for the current thread.
     * <p>
     * @return the thread id
     */
    ThreadId getThreadId();

    /**
	 * Attaches this thread to the thread pool.
	 * <p>
	 * @see                 #enter
	 */
    public void attach();

    /**
     * As above, but hands in an already existing
     * instance of the threadid of the current thread.
     * Returns a handle which can be used in enter and
     * detach calls.<p>
     * The function exists for performance
     * optimization reasons.
     * @see #attach
     */
    public Object attach(ThreadId id);

    /**
	 * Detaches this thread from the thread pool.
	 * @see                 #enter
	 */
    public void detach();

    /**
     * As above, but hands in an already existing
     * instance of the threadid of the current thread
     * and a handle returned by attach.
     * The function exists for performance
     * optimization reasons.
     * @see #attach,#detach
     */
    public void detach(Object handle, ThreadId id);

    /**
	 * Lets this thread enter the thread pool.
	 * This thread then executes all jobs put via
	 * <code>putJob</code> until a reply job arrives.
	 * <p>
	 * @see                 #putJob
	 */
    public Object enter() throws Throwable;

    /**
     * as above but hands in an already existing
     * instance of the threadid of the current thread
     * and a handle returned by attach.
	 * This thread then executes all jobs put via
	 * <code>putJob</code> until a reply job arrives.
	 * <p>
	 * @see                 #putJob
	 */
    public Object enter(Object handle, ThreadId id) throws Throwable;

    /**
	 * Queues a job into the jobQueue of the thread belonging 
	 * to the jobs threadId.
	 * <p>
	 * @param job       the job
	 */
    public void putJob(Job job);

    /**
	 * Disposes this thread pool, thus releasing
	 * all threads by throwing the given
	 * <code>Throwable</code>.
	 * <p>
	 * @param throwing   the Throwable
	 */
    public void dispose(Throwable throwable);

    /**
	 * Destroys the thread pool and tries
	 * to join all created threads immediatly.
	 */
    public void destroy();
}
