package org.gbif.biogarage.service;

import java.util.List;

public interface GenericManager<T> {

    T newInstance();

    List<T> list(int page, int pagesize);

    List<T> search(String querystring, int page, int pagesize);

    T get(Long id);

    T save(T object);

    void remove(Long id);

    void remove(T obj);
}
