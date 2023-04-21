package com.hyper9.uvapi.impls;

import com.hyper9.common.beans.UniqueBean;

/**
 * This bean describes an object or server resource that can be uniquely
 * identified by a unique ID.
 * 
 * @author akutz
 * @param <T> The type of the bean's identifier.
 * 
 */
public abstract class UniqueBeanImpl<T> extends BeanImpl implements UniqueBean<T> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 4749562416626642606L;

    private T id;

    /**
     * Default constructor for serialization.
     */
    protected UniqueBeanImpl() {
        super();
    }

    /**
     * Initializes a new instance of the UniqueBeanImpl class.
     * 
     * @param id The bean's unique identifier.
     */
    protected UniqueBeanImpl(T id) {
        super();
        this.id = id;
    }

    public T getID() {
        return this.id;
    }

    public void setID(T toSet) {
        this.id = toSet;
    }
}
