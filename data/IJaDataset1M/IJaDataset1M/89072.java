package org.webhop.ywdc.util;

import org.hibernate.Session;

/**
 * Wrapper-class for you DAO (and all other DAO's).
 * If you want to use an other HibernateUtil, just change the
 * ConnectionProvider.class -Binding in GuiceModule or create
 * other Provider- and Connection classes.
 */
public class HibernateConnection implements IConnection<Session> {

    private HibernateUtil hibernateUtil;

    public HibernateConnection(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public void closeSession() {
        hibernateUtil.closeSession();
    }

    @Override
    public void commitTransaction() {
        hibernateUtil.commitTransaction();
    }

    @Override
    public void connect() {
        hibernateUtil.Configure(true);
        hibernateUtil.beginTransaction();
    }

    @Override
    public void disConnect() {
        hibernateUtil.closeSessionFactory();
    }

    @Override
    public Session getSession() {
        return hibernateUtil.getSession();
    }

    @Override
    public void rollbackTransaction() {
        hibernateUtil.rollbackTransaction();
    }
}
