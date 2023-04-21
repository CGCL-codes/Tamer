package org.yass.dao;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.yass.util.persistence.EntityManagerFactoryProxy;

/**
 * 
 * @author svenduzont
 * @param <T>
 */
@SuppressWarnings("all")
public class AbstractDao<T> {

    private final DaoHelper helper = DaoHelper.getInstance();

    /**
	 * 
	 */
    protected void beginTx() {
        getTransaction().begin();
    }

    public boolean checkDatabase(final boolean create) {
        return helper.checkDataBase(create);
    }

    /**
	 * 
	 */
    protected void commitTx() {
        getTransaction().commit();
    }

    /**
	 * @param string
	 * @return
	 */
    protected Query createNamedQuery(final String string) {
        return getEntityManager().createNamedQuery(string);
    }

    /**
	 * @param sqlStatement
	 * @return
	 */
    protected Query createNativeQuery(final String sqlStatement) {
        return getEntityManager().createNativeQuery(sqlStatement);
    }

    /**
	 * @return
	 */
    private EntityManager getEntityManager() {
        return EntityManagerFactoryProxy.getInstance().createEntityManager();
    }

    /**
	 * @param setParameter
	 * @return
	 */
    protected Collection<T> getResultList(final Query setParameter) {
        return setParameter.getResultList();
    }

    /**
	 * @param setParameter
	 * @return
	 */
    protected T getSingleResult(final Query setParameter) {
        return (T) setParameter.getSingleResult();
    }

    /**
	 * @return
	 */
    private EntityTransaction getTransaction() {
        return getEntityManager().getTransaction();
    }

    /**
	 * @param library
	 */
    protected void persist(final Object entity) {
        getEntityManager().persist(entity);
    }

    /**
	 * @param ctx
	 */
    protected void remove(final Object entity) {
        getEntityManager().remove(entity);
    }

    /**
	 * 
	 */
    protected void rollbackTx() {
        getTransaction().rollback();
    }
}
