package net.cepra.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import net.cepra.test.pool.ResourcePool;
import org.junit.Test;

public class CreateTestUser {

    @Test
    public void create() {
        EntityManager em = PersistenceHelper.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        ResourcePool.HUT.get(em);
        tx.commit();
        em.close();
    }
}
