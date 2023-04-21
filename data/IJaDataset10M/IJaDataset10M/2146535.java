package org.wintersleep.repository;

import java.io.Serializable;
import java.util.List;

public interface Repository<T extends Serializable, ID extends Serializable> {

    T findById(ID id);

    T loadById(ID id, boolean lock);

    List<T> findAll();

    long countAll();

    T makePersistent(T entity);

    void makeTransient(T entity);

    void flush();

    T merge(T entity);
}
