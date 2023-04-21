package net.rpm.client.service;

import java.io.Serializable;
import java.util.List;

public interface IService<T, ID extends Serializable> {

    List<T> getEntityList();

    List<T> getSortedList();

    T get(ID id);

    T save(T entity);

    T merge(T entity);

    void remove(ID entityId);

    boolean exists(ID id);

    Integer getCount();
}
