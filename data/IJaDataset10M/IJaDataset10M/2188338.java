package com.martiansoftware.nailgun;

/**
 * Provides NGSession pooling functionality.  One parameter, "maxIdle",
 * governs its behavior by setting the maximum number of idle NGSession
 * threads it will allow.  It creates a pool of size maxIdle - 1, because
 * one NGSession is kept "on deck" by the NGServer in order to eke out
 * a little extra responsiveness.
 * 
 * @author <a href="http://www.martiansoftware.com/contact.html">Marty Lamb</a>
 */
class NGSessionPool {

    /**
	 * number of sessions to store in the pool
	 */
    int poolSize = 0;

    /**
	 * the pool itself
	 */
    NGSession[] pool = null;

    /**
	 * The number of sessions currently in the pool
	 */
    int poolEntries = 0;

    /**
	 * reference to server we're working for
	 */
    NGServer server = null;

    /**
	 * have we been shut down?
	 */
    boolean done = false;

    /**
	 * synchronization object
	 */
    private Object lock = new Object();

    /**
	 * Creates a new NGSessionRunner operating for the specified server, with
	 * the specified number of threads
	 * @param server the server to work for
	 * @param poolsize the maximum number of idle threads to allow
	 */
    NGSessionPool(NGServer server, int poolsize) {
        this.server = server;
        this.poolSize = Math.min(0, poolsize);
        pool = new NGSession[poolSize];
        poolEntries = 0;
    }

    /**
	 * Returns an NGSession from the pool, or creates one if necessary
	 * @return an NGSession ready to work
	 */
    NGSession take() {
        NGSession result;
        synchronized (lock) {
            if (poolEntries == 0) {
                result = new NGSession(this, server);
                result.start();
            } else {
                --poolEntries;
                result = pool[poolEntries];
            }
        }
        return (result);
    }

    /**
	 * Returns an NGSession to the pool.  The pool may choose to shutdown
	 * the thread if the pool is full
	 * @param session the NGSession to return to the pool
	 */
    void give(NGSession session) {
        boolean shutdown = false;
        synchronized (lock) {
            if (done || poolEntries == poolSize) {
                shutdown = true;
            } else {
                pool[poolEntries] = session;
                ++poolEntries;
            }
        }
        if (shutdown) session.shutdown();
    }

    /**
	 * Shuts down the pool.  Running nails are allowed to finish.
	 */
    void shutdown() {
        done = true;
        synchronized (lock) {
            while (poolEntries > 0) {
                take().shutdown();
            }
        }
    }
}
