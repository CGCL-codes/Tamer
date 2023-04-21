package org.weras.commons.persistence.hibernate;

import javax.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Utility methods to manipulate Hibernate
 * 
 * @author Fabr�cio Silva Epaminondas
 * 
 */
abstract class HibernateHelper {

    protected SessionFactory sessionFactory = null;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public final void initialize() {
    }

    public final void destroy() {
        sessionFactory.close();
    }

    public final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public final void closeSession() {
        getCurrentSession().close();
    }

    public final void beginTransaction() throws PersistenceException {
        getCurrentSession().beginTransaction();
    }

    public final boolean isActiveTransaction() throws PersistenceException {
        Transaction t = getCurrentSession().getTransaction();
        return t != null && t.isActive();
    }

    /**
	 * Commits active transaction without close the session
	 * 
	 * @throws PersistenceException
	 */
    public final void commitTransaction() throws PersistenceException {
        getCurrentSession().getTransaction().commit();
    }

    /**
	 * Commits and releases the connection
	 * 
	 * @throws PersistenceException
	 */
    public final void finalizeTransaction() throws PersistenceException {
        commitTransaction();
        closeSession();
    }

    public final void rollbackTransaction() {
        getCurrentSession().getTransaction().rollback();
    }

    public final void disconnect() {
        sessionFactory.close();
    }

    public final void flushSession() throws PersistenceException {
        getCurrentSession().flush();
    }

    public final void clearSession() {
        getCurrentSession().clear();
    }
}
