package com.ideo.sweetdevria.proxy.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Administrateur
 * 
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            if (sessionFactory == null) {
                sessionFactory = new Configuration().configure().buildSessionFactory();
            }
        }
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory(String configFile) {
        if (sessionFactory == null) {
            if (sessionFactory == null) {
                sessionFactory = new Configuration().configure(configFile).buildSessionFactory();
            }
        }
        return sessionFactory;
    }

    public static Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
