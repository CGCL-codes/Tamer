package org.perfectjpattern.jee.integration.dao;

import java.io.*;
import java.lang.reflect.*;
import org.hibernate.*;
import org.perfectjpattern.core.api.structural.adapter.*;
import org.perfectjpattern.core.structural.adapter.*;
import org.perfectjpattern.jee.api.integration.dao.*;

/**
 * Adapts Hibernate's {@link Session} to the JPA implementation-free 
 * PerfectJPattern's {@link ISession} definition 
 * 
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $Date: Feb 10, 2009 8:26:06 PM $
 */
public final class HibernateSessionAdapter extends Adapter<ISession, Session> {

    /**
     * Constructs a {@link HibernateSessionAdapter} from the Adaptee 
     * {@link Session} instance
     * 
     * @param anAdaptee Adaptee Hibernate Transaction
     * @throws IllegalArgumentException
     */
    public HibernateSessionAdapter(Session anAdaptee) throws IllegalArgumentException {
        super(ISession.class, anAdaptee);
    }

    public ITransaction getTransaction() {
        Transaction myAdaptee = getUnderlying().getTransaction();
        if (theTransactionAdapter == null || theTransactionAdapter.getAdaptee() != myAdaptee) {
            theTransactionAdapter = new HibernateTransactionAdapter(myAdaptee);
        }
        return theTransactionAdapter.getTarget();
    }

    @SuppressWarnings("unchecked")
    public <E> E find(Class<E> aPersistentClass, Object anId) {
        assert anId instanceof Serializable : "'anId' must be Serializable";
        E myElement = (E) getUnderlying().load(aPersistentClass, (Serializable) anId);
        return myElement;
    }

    public Session getDelegate() {
        return getUnderlying();
    }

    public IQuery createQuery(String aSqlString) {
        IQuery myQuery = new HibernateQueryAdapter(getUnderlying().createQuery(aSqlString)).getTarget();
        return myQuery;
    }

    public IQuery createNativeQuery(String aSqlString, Class<?> aPersistentClass) {
        IQuery myQuery = new HibernateQueryAdapter(getUnderlying().createSQLQuery(aSqlString)).getTarget();
        return myQuery;
    }

    public IQuery createNamedQuery(String aQueryName, Class<?> aPersistentClass) {
        IQuery myQuery = new HibernateQueryAdapter(getUnderlying().getNamedQuery(aQueryName)).getTarget();
        return myQuery;
    }

    public void remove(Object anObject) {
        getUnderlying().delete(anObject);
    }

    public void update(Object anObject) {
        getUnderlying().saveOrUpdate(anObject);
    }

    @SuppressWarnings("unchecked")
    public <Id> Id persist(Object anObject) {
        return (Id) getUnderlying().save(anObject);
    }

    public void joinTransaction() {
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    protected Object invokeUnderlying(Method aMethod, Object[] anArguments) throws Throwable {
        try {
            return super.invokeUnderlying(aMethod, anArguments);
        } catch (Throwable anException) {
            throw new DaoException(anException);
        }
    }

    private IAdapter<ITransaction, Transaction> theTransactionAdapter;
}
