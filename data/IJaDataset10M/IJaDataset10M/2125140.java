package com.googlecode.hibernatedao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateInit {

    private static HibernateInit instance = null;

    private SessionFactory sessionFactory = null;

    private HibernateInit() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static HibernateInit getInstance() {
        if (instance == null) {
            instance = new HibernateInit();
        }
        return instance;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
