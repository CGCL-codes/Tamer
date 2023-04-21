package com.ixora.common.pooling;

import java.util.HashMap;
import java.util.Map;
import com.ixora.common.pooling.exception.FailedToCreateObject;

/**
 * KeyedPoolableObjectPoolDefault.
 * @author Daniel Moraru
 */
public class KeyedPoolableObjectPoolDefault implements KeyedPoolableObjectPool {

    /**
	 * Pools map.
	 */
    private Map<Object, PoolableObjectPool> pools;

    /**
	 * Object factory providing objects to the pool.
	 */
    private KeyedPoolableObjectFactoryFactory factory;

    /**
	 * Inactivity controller.
	 */
    private InactivityController inactivityController;

    /**
	 * Pool size controller.
	 */
    private PoolSizeController poolSizeController;

    /**
	 * Constructor for KeyedPoolableObjectPoolDefault.
	 */
    public KeyedPoolableObjectPoolDefault(KeyedPoolableObjectFactoryFactory factory, InactivityController inactivityController, PoolSizeController poolSizeController) {
        super();
        if (factory == null) {
            throw new IllegalArgumentException("null object factory");
        }
        this.factory = factory;
        this.inactivityController = inactivityController;
        this.poolSizeController = poolSizeController;
        this.pools = new HashMap<Object, PoolableObjectPool>();
    }

    /**
	 * @see com.ixora.common.pooling.KeyedPoolableObjectPool#getObject(java.lang.Object)
	 */
    public synchronized PoolableObject getObject(Object key) throws FailedToCreateObject {
        PoolableObjectPool pool = this.pools.get(key);
        if (pool == null) {
            pool = new PoolableObjectPoolDefault(this.factory.getFactory(key), this.inactivityController, this.poolSizeController);
            this.pools.put(key, pool);
        }
        return pool.getObject();
    }

    /**
	 * @see com.ixora.common.pooling.KeyedPoolableObjectPool#releaseObject(java.lang.Object, com.ixora.common.pooling.PoolableObject)
	 */
    public synchronized void releaseObject(Object key, PoolableObject obj) {
        ((PoolableObjectPool) this.pools.get(key)).releaseObject(obj);
    }

    /**
	 * @see com.ixora.common.pooling.KeyedPoolableObjectPool#getState(java.lang.Object)
	 */
    public synchronized PoolInfo getState(Object key) {
        PoolableObjectPool pool = (PoolableObjectPool) this.pools.get(key);
        return pool == null ? null : pool.getPoolInfo();
    }
}
