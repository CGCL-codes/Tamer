package br.gov.frameworkdemoiselle.transaction;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import br.gov.frameworkdemoiselle.annotation.Bundle;
import br.gov.frameworkdemoiselle.util.ResourceBundle;

/**
 * Represents the strategy destinated to manage JPA transactions.
 * 
 * @author SERPRO
 * @see Transaction
 */
@Alternative
public class JPATransaction implements Transaction {

    private static final long serialVersionUID = 1L;

    @Inject
    @Bundle(baseName = "demoiselle-jpa-bundle")
    private ResourceBundle bundle;

    @Inject
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void begin() throws NotSupportedException, SystemException {
        getEntityManager().getTransaction().begin();
    }

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        getEntityManager().getTransaction().commit();
    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        getEntityManager().clear();
        getEntityManager().getTransaction().rollback();
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        getEntityManager().getTransaction().setRollbackOnly();
    }

    @Override
    public int getStatus() throws SystemException {
        int result = Status.STATUS_NO_TRANSACTION;
        if (getEntityManager().getTransaction().isActive()) {
            result = Status.STATUS_ACTIVE;
        }
        return result;
    }

    @Override
    public void setTransactionTimeout(int seconds) throws SystemException {
        throw new SystemException(bundle.getString("operation-not-supported"));
    }

    @Override
    public boolean isActive() throws SystemException {
        return getEntityManager().getTransaction().isActive();
    }

    @Override
    public boolean isMarkedRollback() throws SystemException {
        return getEntityManager().getTransaction().getRollbackOnly();
    }
}
