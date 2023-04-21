package ch.exm.storm.loader.hibernate;

import org.hibernate.SessionFactory;

public class HibernateQueryExecutor {

    private SessionFactory sessionFactory;

    HibernateQueryExecutor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
