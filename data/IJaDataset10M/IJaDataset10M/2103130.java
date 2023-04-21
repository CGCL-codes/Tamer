package org.opentrust.jsynch.threadpool;

import org.opentrust.jsynch.Lockable;

/**
 *	An abstraction of "active" management of one or more
 *	thread strategies.
 */
public abstract class ActiveThreadStrategy implements Cloneable {

    /**
	 *	Wait for the strategy('s) to complete. The specified timeout
	 *	controls how long the caller is willing to wait for the operation
	 *	to complete, and is described using standard <code>Lockable</code>
	 *	semantics, including <code>Lockable.DONTBLOCK</code> and 
	 *	<code>Lockable.FOREVER.</code>
	 *
	 *	@exception Lockable.LockableException	includes TimedoutException,
	 *		AbandonedException, etc...
	 */
    public abstract void join(long timeout) throws Lockable.LockableException;

    /**
	 *	Wait (forever if necessary) for the strategy('s) to complete.
	 *
	 *	@exception Lockable.LockableException	includes TimedoutException,
	 *		AbandonedException, etc...
	 */
    public abstract void join() throws Lockable.LockableException;

    /** @return true if the strategy is "canceled", false otherwise */
    public abstract boolean isCanceled();

    /** @return true if the strategy is "active", false otherwise */
    public abstract boolean isActive();

    /** @return The elapsed time of the previous execution */
    public abstract long getElapsedTime();

    /** @return The result of the completed work */
    public abstract Object getResult() throws Exception;

    /** @return any exception associated with the completed work */
    public abstract Exception getException() throws IllegalStateException;

    /** 
	 *	Cancel the strategy('s).
	 *
	 *	The actual work being performed may or may not be canceled, but effectively
	 *	the work is considered "complete", and the result is a CanceledException.
	 */
    public abstract void cancel();

    /**
	 *	Activate the strategy('s).
	 *
	 *	Only for internal use. DO NOT CALL THIS METHOD.
	 */
    protected abstract void activate();

    /**
	 *	Reset the strategy('s) to the inactive, initialized state for 
	 *	possible reuse.
	 */
    protected abstract void reset();

    /**
	 *	Create a clone of the strategy('s).
	 *
	 *	The clone is in a inactive, initialized state (see reset).
	 *
	 *	@return The clone of the strategy('s).
	 *
	 *	@exception	CloneNotSupportedException (typically suppressed
	 *	by sub-classes.)
	 */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
	 *	Base class for "active" management exceptions.
	 */
    public static class ThreadStrategyException extends RuntimeException {

        public ThreadStrategyException(String msg) {
            super(msg);
        }
    }

    /**
	 *	Exception thrown when a strategy times out (expires) before
	 *	it can be executed.
	 *
	 *	<p>Note: This exception is called in the context of the thread
	 *	that attempts a getResult() on the ThreadStrategy.
	 */
    public static class ExpiredException extends ThreadStrategyException {

        public ExpiredException() {
            super("Strategy expired");
        }

        public ExpiredException(ThreadStrategy ts) {
            super("Strategy expired: " + ts.getName());
        }
    }

    /**
	 *	Exception thrown when a strategy is canceled.
	 *
	 *	<p>Note: This exception is called in the context of the thread
	 *	that attempts a getResult() on the ThreadStrategy.
	 */
    public static class CanceledException extends ThreadStrategyException {

        public CanceledException() {
            super("Strategy canceled");
        }

        public CanceledException(ThreadStrategy ts) {
            super("Strategy canceled: " + ts.getName());
        }
    }
}
