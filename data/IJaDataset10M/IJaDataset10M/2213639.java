package com.employee.service;

import java.util.Collection;

public interface StandardService<T> {

    public T find(Long id);

    public Collection<T> findAll(Collection<Long> ids);

    public Collection<T> list();

    public void save(T pc);

    public void delete(T pc);

    public void deleteAll(Collection<T> pcs);

    public void deleteAll(Long[] ids);
}
