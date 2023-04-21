package org.mili.jmibs.impl;

import org.mili.jmibs.api.*;

/**
 * This abstract class provides some basic implementation of object load benchmark interface.
 *
 * @author Michael Lieshoff
 * @version 1.0 12.04.2010
 * @since 1.0
 */
public abstract class AbstractObjectLoadBenchmark<T> extends AbstractBenchmark implements ObjectLoadBenchmark<T> {

    private T model = null;

    private int objectLoad = 0;

    @Override
    public T getModel() {
        return this.model;
    }

    /**
     * @param model the model structure to set
     */
    public void setModel(T model) {
        this.model = model;
    }

    /**
     * @return the object loading count.
     */
    public int getObjectLoad() {
        return this.objectLoad;
    }

    @Override
    public void setObjectLoad(int objectLoad) {
        this.objectLoad = objectLoad;
    }
}
