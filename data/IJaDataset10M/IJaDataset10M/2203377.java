package com.abacus.pay.dao;

import java.lang.reflect.ParameterizedType;
import org.hibernate.Session;

public class GenericDaoImpl<T, ID extends java.io.Serializable> implements GenericDao<T, ID> {

    private Class<T> clazz = null;

    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        super();
        clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Session getSession() {
        return HibernateUtil.getCurrentSession();
    }

    @Override
    public void save(T t) {
        getSession().save(t);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T findById(ID id) {
        return (T) getSession().get(clazz, id);
    }

    @Override
    public void modify(T t) {
        getSession().update(t);
    }

    @Override
    public void remove(ID id) {
        remove(findById(id));
    }

    @Override
    public void remove(T t) {
        getSession().delete(t);
    }
}
