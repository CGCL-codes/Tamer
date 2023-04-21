package com.rcreations.spring.dao;

import java.io.Serializable;
import java.util.List;

/**
 */
public interface CrudDao<I> {

    /**
    * Finds an object by example
    * @param instance
    * @return
    */
    @SuppressWarnings("unchecked")
    public abstract List<I> findByExample(I instance);

    public abstract I findById(Serializable id);

    @SuppressWarnings("unchecked")
    public abstract List<I> findAll();

    public abstract I save(I transientInstance);

    public abstract int count();

    public abstract void delete(I instance);
}
