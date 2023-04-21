package com.meidusa.amoeba.net.poolable;

/**
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public interface PoolableObject {

    public void setObjectPool(ObjectPool pool);

    public ObjectPool getObjectPool();

    public void setActive(boolean isactive);

    public boolean isRemovedFromPool();

    public boolean isActive();
}
