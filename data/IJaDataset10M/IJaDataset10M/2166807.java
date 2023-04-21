package com.recursivity.jpa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class JpaHandle {

    private static ThreadLocal<Map<String, EntityManager>> entityManagers = new ThreadLocal<Map<String, EntityManager>>() {

        @Override
        protected Map<String, EntityManager> initialValue() {
            return new HashMap<String, EntityManager>();
        }
    };

    private static ThreadLocal<Map<EntityManager, EntityTransaction>> transactions = new ThreadLocal<Map<EntityManager, EntityTransaction>>() {

        @Override
        protected Map<EntityManager, EntityTransaction> initialValue() {
            return new HashMap<EntityManager, EntityTransaction>();
        }
    };

    public static boolean isActive() {
        if (entityManagers.get().isEmpty()) return false;
        return true;
    }

    public static EntityManager get() {
        return get(PersistenceUnit.unitName);
    }

    public static EntityManager get(String unitName) {
        if (!entityManagers.get().containsKey(unitName)) {
            EntityManager em = PersistenceManagerFactory.getEntityManager(unitName);
            synchronized (entityManagers.get()) {
                entityManagers.get().put(unitName, em);
            }
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            transactions.get().put(em, tx);
            return em;
        }
        return entityManagers.get().get(unitName);
    }

    public static void commit() {
        Iterator<EntityManager> ems = transactions.get().keySet().iterator();
        while (ems.hasNext()) {
            EntityManager em = ems.next();
            EntityTransaction tx = em.getTransaction();
            if (tx.isActive()) tx.commit();
            if (em.isOpen()) em.close();
        }
        transactions.get().clear();
        entityManagers.get().clear();
    }

    public static void rollback() {
        Iterator<EntityManager> ems = transactions.get().keySet().iterator();
        while (ems.hasNext()) {
            EntityManager em = ems.next();
            EntityTransaction tx = em.getTransaction();
            if (tx.isActive()) tx.rollback();
            if (em.isOpen()) em.close();
        }
        transactions.get().clear();
        entityManagers.get().clear();
    }
}
