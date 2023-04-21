package org.jazzteam.shareideas.service.implementation;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jazzteam.shareideas.db.HibernateUtil;
import org.jazzteam.shareideas.exceptions.GeneralServiceException;
import org.jazzteam.shareideas.model.Identity;
import org.jazzteam.shareideas.service.IModelService;

public abstract class ModelServiceReal<T extends Identity> implements IModelService<T> {

    private Class type;

    public ModelServiceReal(Class type) {
        this.type = type;
    }

    @Override
    public void delete(long id) throws IllegalArgumentException, GeneralServiceException {
        if (id <= 0) {
            throw new IllegalArgumentException("Id can not be null");
        }
        try {
            Session session = HibernateUtil.getSession();
            T deleted = (T) session.load(type, id);
            session.delete(deleted);
        } catch (HibernateException ex) {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw new GeneralServiceException("Service can not delete " + type.getSimpleName() + "on id" + id);
        }
    }

    @Override
    public void delete(T entity) throws IllegalArgumentException, GeneralServiceException {
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        try {
            Session session = HibernateUtil.getSession();
            session.delete(entity);
        } catch (HibernateException ex) {
            ex.printStackTrace();
            throw new GeneralServiceException("Service can not delete " + type.getSimpleName());
        }
    }

    @Override
    public T get(long id) throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("Id can not be null");
        }
        T obj = null;
        try {
            Session session = HibernateUtil.getSession();
            obj = (T) session.createQuery(String.format("from %s it where it.id=:id", type.getSimpleName())).setLong("id", id).uniqueResult();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public List<T> getAll() {
        List<T> result = new ArrayList<T>();
        try {
            Session session = HibernateUtil.getSession();
            result = session.createQuery(String.format("from %s", type.getSimpleName())).list();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public T save(T entity) throws IllegalArgumentException, GeneralServiceException {
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        try {
            Session session = HibernateUtil.getSession();
            session.save(entity);
        } catch (HibernateException ex) {
            ex.printStackTrace();
            throw new GeneralServiceException("Service can not save " + type.getSimpleName());
        }
        return entity;
    }
}
