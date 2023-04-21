package org.springframework.orm.enhanced;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class JpaFindEntityCallback implements FindEntityCallback {

    private static final Logger LOG = LoggerFactory.getLogger(JpaFindEntityCallback.class);

    private static long transactionCounter = 0;

    private Class<?> clazz;

    private EntityManagerFactory entityManagerFactory;

    private PlatformTransactionManager transactionManager;

    public JpaFindEntityCallback(Class<?> clazz, EntityManagerFactory entityManagerFactory, PlatformTransactionManager transactionManager) {
        this.clazz = clazz;
        this.entityManagerFactory = entityManagerFactory;
        this.transactionManager = transactionManager;
    }

    @Override
    public Object find(Serializable primaryKey) {
        Object entity = null;
        TransactionStatus transactionStatus;
        LOG.info("Looking for '" + clazz.getSimpleName() + "' identified by '" + primaryKey + "'...");
        transactionStatus = beginNewTransaction();
        try {
            entity = getEntityManager().find(clazz, primaryKey);
            transactionManager.commit(transactionStatus);
        } catch (Throwable t) {
            t.printStackTrace();
            transactionManager.rollback(transactionStatus);
        }
        LOG.info(" entity: " + entity);
        return entity;
    }

    private TransactionStatus beginNewTransaction() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("findEntityCallbackTransaction" + (transactionCounter++));
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionManager.getTransaction(transactionDefinition);
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
