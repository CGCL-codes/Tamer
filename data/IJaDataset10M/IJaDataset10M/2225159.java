package com.markatta.hund.service;

import com.markatta.hund.model.Status;
import com.markatta.hund.model.StatusType;
import java.util.List;

/**
 *
 * @author johan
 */
public interface EntityService<T> {

    public T find(long id);

    public List<T> getAll();

    public T saveOrUpdate(T entity);

    public long totalCount();

    public long countStatus(StatusType type);
}
