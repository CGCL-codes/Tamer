package org.nexopenframework.pool.providers;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.nexopenframework.pool.ObjectPool;
import org.nexopenframework.pool.PoolException;
import org.nexopenframework.pool.PoolMetadata;
import org.nexopenframework.pool.PoolProvider;
import org.nexopenframework.pool.PooledObjectFactory;
import org.springframework.beans.factory.DisposableBean;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class CommonsPoolProvider implements PoolProvider, PoolableObjectFactory {

    private PooledObjectFactory objectFactory;

    /**
	 * 
	 * @see org.nexopenframework.pool.PoolProvider#createPool(org.nexopenframework.pool.PoolMetadata, org.nexopenframework.pool.PooledObjectFactory)
	 */
    public ObjectPool createPool(PoolMetadata metaData, PooledObjectFactory pooledObjectFactory, String poolName) throws PoolException {
        this.objectFactory = pooledObjectFactory;
        GenericObjectPool gop = new GenericObjectPool(this);
        CommonsObjectPool pool = new CommonsObjectPool(gop, metaData, poolName);
        return pool;
    }

    public void activateObject(Object obj) {
    }

    public void destroyObject(Object obj) throws Exception {
        if (obj instanceof DisposableBean) {
            ((DisposableBean) obj).destroy();
        }
    }

    public Object makeObject() {
        return objectFactory.createObject();
    }

    public void passivateObject(Object arg0) throws Exception {
    }

    public boolean validateObject(Object obj) {
        return true;
    }
}
