package com.kndomain.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class BaseDao {

    private HibernateTemplate hibernateTemplate;

    private Session session;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public Session currentSession() throws HibernateException {
        if (session == null || !session.isOpen()) {
            session = hibernateTemplate.getSessionFactory().openSession();
        }
        return session;
    }

    public Object get(Class clazz, Long id) {
        Object objects = null;
        try {
            Session session = currentSession();
            Transaction tx = session.beginTransaction();
            objects = session.get(clazz, id);
        } catch (HibernateException e) {
        } finally {
        }
        return objects;
    }

    public Long save(Object boObject) {
        Long id = null;
        try {
            Session session = currentSession();
            Transaction tx = session.beginTransaction();
            id = (Long) session.save(boObject);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e);
        } finally {
        }
        return id;
    }

    public void saveOrUpdate(Object boObject) {
        Long id = null;
        try {
            Session session = currentSession();
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(boObject);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e);
        } finally {
        }
    }

    public Object getBoParParameters(Class clazz, String paramName, String queryString) {
        Object bo = this.currentSession().get(clazz, new Long("1"));
        return bo;
    }

    public void close() {
        this.hibernateTemplate.getSessionFactory().close();
    }

    public void open() {
        this.hibernateTemplate.getSessionFactory().openSession();
    }
}
