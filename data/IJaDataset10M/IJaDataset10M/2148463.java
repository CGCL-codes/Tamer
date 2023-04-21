package org.regola.dao;

import java.io.Serializable;
import java.util.List;
import org.regola.model.ModelPattern;

@SuppressWarnings("unchecked")
public interface UniversalDao {

    public List getAll(Class clazz);

    boolean exists(Class clazz, Serializable id);

    public Object get(Class clazz, Serializable id);

    public Object save(Object entity);

    public void remove(Class clazz, Serializable id);

    void removeEntity(Object entity);

    public List find(Class clazz, ModelPattern pattern);

    public int count(Class clazz, ModelPattern pattern);
}
