package com.ixora.common.pooling;

import com.ixora.common.pooling.exception.FailedToCreateObject;

/**
 * Default object pool.
 * @author Daniel Moraru
 */
public final class ObjectPoolDefault implements ObjectPool {

    /**
	 * Pool.
	 */
    private Pool pool;

    /**
	 * Object factory providing objects to the pool.
	 */
    private ObjectFactory factory;

    /**
	 * Constructor for ObjectPoolDefault.
	 * @param factory
	 * @param inactivityController
	 * @param poolSizeController
	 */
    public ObjectPoolDefault(ObjectFactory factory, InactivityController inactivityController, PoolSizeController poolSizeController) {
        super();
        if (factory == null) {
            throw new IllegalArgumentException("null object factory");
        }
        this.factory = factory;
        this.pool = new Pool(null, inactivityController, poolSizeController);
    }

    /**
	 * @see com.ixora.common.pooling.ObjectPool#getObject()
	 */
    public synchronized Object getObject() throws FailedToCreateObject {
        Object ret = this.pool.getObject();
        if (ret == null) {
            ret = this.factory.createObject();
            this.pool.addObject(ret);
            ret = this.pool.getObject();
        }
        return ret;
    }

    /**
	 * @see com.ixora.common.pooling.ObjectPool#releaseObject(java.lang.Object)
	 */
    public synchronized void releaseObject(Object obj) {
        this.pool.returnObject(obj);
    }

    /**
	 * @see com.ixora.common.pooling.ObjectPool#getState()
	 */
    public synchronized PoolInfo getState() {
        return this.pool.getState();
    }
}
