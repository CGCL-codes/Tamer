package com.daveoxley.cbus.threadpool;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

/**
 *
 * @author Dave Oxley <dave@daveoxley.co.uk>
 */
public class ThreadImplPool extends GenericObjectPool {

    /**
     *
     * @param config
     */
    public ThreadImplPool(Config config) {
        super(new ThreadImplFactory(), config);
    }

    /**
     *
     * @return
     * @throws java.lang.Exception
     */
    @Override
    public Object borrowObject() throws Exception {
        ThreadImpl thread = (ThreadImpl) super.borrowObject();
        thread.setPool(this);
        return thread;
    }
}
