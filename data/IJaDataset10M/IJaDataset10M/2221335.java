package com._pmz0178.blogtxt.dao;

import java.util.List;

public interface BaseDao<E> {

    void saveOrUpdate(E entity);

    void delete(E entity);

    List<E> list(E criteria, String order, String asc);
}
