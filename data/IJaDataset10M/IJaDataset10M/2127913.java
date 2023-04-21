package org.jactr.core.factory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author developer
 */
public abstract class AbstractFactory<T> implements IFactory<T> {

    private final int _maximumPoolSize;

    private final List<T> _pool;

    public AbstractFactory() {
        this(10, false);
    }

    public AbstractFactory(int maximumPoolSize) {
        this(maximumPoolSize, false);
    }

    public AbstractFactory(int maximumPoolSize, boolean precreate) {
        _maximumPoolSize = maximumPoolSize;
        _pool = new ArrayList<T>(_maximumPoolSize);
        if (precreate) for (int i = 0; i < _maximumPoolSize; i++) _pool.add(create());
    }

    /**
   * @see org.jactr.core.factory.IFactory#acquireInstance()
   */
    public synchronized T acquireInstance() {
        T rtn = null;
        if (_pool.size() != 0) rtn = _pool.remove(0); else rtn = create();
        return rtn;
    }

    public synchronized T acquireInstance(T template) {
        T rtn = acquireInstance();
        set(rtn, template);
        return rtn;
    }

    /**
   * @see org.jactr.core.factory.IFactory#releaseInstance(java.lang.Object)
   */
    public synchronized void releaseInstance(T t) {
        if (_pool.size() < _maximumPoolSize) {
            reset(t);
            _pool.add(t);
        } else destroy(t);
    }

    /**
   * create an instance of T.
   * 
   * @param template
   * @return
   */
    protected abstract T create();

    /**
   * reset an instance
   * 
   * @param t
   */
    protected abstract void reset(T t);

    /**
   * set the values of newInstance to those of template
   * 
   * @param newInstance
   * @param template
   */
    protected abstract void set(T newInstance, T template);

    protected abstract void destroy(T t);
}
