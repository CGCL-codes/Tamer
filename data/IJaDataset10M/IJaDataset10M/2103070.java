package com.dateWeb.dao;

import java.io.Serializable;

public interface BaseDao<T> {

    public void save(T t);

    public void update(T t);

    public T get(Serializable id);
}
