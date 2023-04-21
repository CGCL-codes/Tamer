package br.com.guaraba.wally.commons.dao.factories;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.hibernate.Session;

public class EntityManagerFactory {

    private static EntityManager entityManager = null;

    public static EntityManager getInstance(final String persistence_unit) {
        if (entityManager == null) {
            entityManager = Persistence.createEntityManagerFactory(persistence_unit).createEntityManager();
        }
        return entityManager;
    }

    public static Session getSession() {
        return (Session) entityManager.getDelegate();
    }
}
