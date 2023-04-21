package com.google.appengine.datanucleus.jpa;

import junit.framework.TestCase;
import org.datanucleus.api.jpa.JPAEntityManager;
import com.google.appengine.datanucleus.DatastoreManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Max Ross <maxr@google.com>
 */
public class JPADataSourceConfigTest extends TestCase {

    public void testTransactionalEMF() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(JPATestCase.EntityManagerFactoryName.transactional_ds_non_transactional_ops_not_allowed.name());
        JPAEntityManager em = (JPAEntityManager) emf.createEntityManager();
        DatastoreManager storeMgr = (DatastoreManager) em.getExecutionContext().getStoreManager();
        assertTrue(storeMgr.connectionFactoryIsAutoCreateTransaction());
        em.close();
        emf.close();
    }

    public void testNonTransactionalEMF() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(JPATestCase.EntityManagerFactoryName.nontransactional_ds_non_transactional_ops_not_allowed.name());
        JPAEntityManager em = (JPAEntityManager) emf.createEntityManager();
        DatastoreManager storeMgr = (DatastoreManager) em.getExecutionContext().getStoreManager();
        assertFalse(storeMgr.connectionFactoryIsAutoCreateTransaction());
        em.close();
        emf.close();
    }
}
