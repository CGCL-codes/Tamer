package org.springframework.webflow.jpa.hibernate;

import org.springframework.webflow.jpa.DefaultEntityManagerLifecycleController;
import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.context.ManagedSessionContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Hibernate specific implementation of EntityManagerLifecycleController interface. Set for each new entity manager
 * hibernate specific flush mode and starts new JPA transaction on activation, and commit it on deactivation
 *
 * @author Maxim Petrashev
 */
public class EntityManagerLifecycleController extends DefaultEntityManagerLifecycleController {

    public EntityManagerLifecycleController(EntityManagerFactory aEntityManagerFactory) {
        super(aEntityManagerFactory);
    }

    public EntityManager create() {
        EntityManager retVal = super.create();
        Session session = HibernateUtils.getSession(retVal);
        session.setFlushMode(FlushMode.MANUAL);
        return retVal;
    }

    protected void unbind(EntityManager aEntityManager) {
        try {
            SessionFactory sessionFactory = HibernateUtils.getSessionFactory(getEntityManagerFactory());
            ManagedSessionContext.unbind(sessionFactory);
        } finally {
            super.unbind(aEntityManager);
        }
    }

    protected void bind(EntityManager aEntityManager) {
        super.bind(aEntityManager);
        ManagedSessionContext.bind(HibernateUtils.getSession(aEntityManager));
    }

    public void activate(EntityManager aEntityManager) {
        beginTransaction(aEntityManager);
        super.activate(aEntityManager);
    }

    public void deactivate(EntityManager aEntityManager) {
        try {
            disconnectSession(aEntityManager);
        } finally {
            super.deactivate(aEntityManager);
        }
    }

    protected void beginTransaction(EntityManager aEntityManager) {
        aEntityManager.getTransaction().begin();
    }

    protected void disconnectSession(EntityManager aEntityManager) {
        EntityTransaction transaction = aEntityManager.getTransaction();
        transaction.commit();
    }
}
