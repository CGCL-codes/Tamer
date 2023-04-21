package net.sf.jerkbot.util;

import net.sf.jerkbot.exceptions.JerkBotException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;

/**
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 *         JPA utility class using the extended thread local pattern
 * @version 0.0.1
 */
public class JPAUtil {

    private static EntityManagerFactory entityManagerFactory;

    private static final ThreadLocal<EntityManager> threadEntityManager = new ThreadLocal<EntityManager>();

    private static final ThreadLocal<EntityTransaction> threadEntityTransaction = new ThreadLocal<EntityTransaction>();

    private static final Logger Log = LoggerFactory.getLogger(JPAUtil.class.getName());

    private static final Owner trueOwner = new Owner(true);

    private static final Owner fakeOwner = new Owner(false);

    /**
     * Sets the entity manager factory.
     *
     * @param f the new entity manager factory
     */
    public static synchronized void setEntityManagerFactory(EntityManagerFactory f) {
        entityManagerFactory = f;
    }

    /**
     * Returns the EntityManagerFactory used for this static class.
     *
     * @return EntityManagerFactory
     */
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public static Object acquireThreadOwnerShip() throws Exception {
        EntityManager em = threadEntityManager.get();
        if (em == null) {
            em = getEntityManagerFactory().createEntityManager();
            threadEntityManager.set(em);
            return trueOwner;
        }
        Log.debug("Session Found - Give a Fake identity");
        return fakeOwner;
    }

    /**
     * Retrieves the current EntityManager local to the thread.
     * If no Session is open, opens a new EntityManager for the running thread.
     *
     * @return EntityManager
     */
    public static EntityManager getEntityManager() {
        return threadEntityManager.get();
    }

    /**
     * Closes the EntityManager local to the thread.
     */
    public static void closeEntityManager(Object ownership) {
        if (ownership == null) {
            return;
        }
        Log.debug("Closing entity manager for this thread.");
        if (((Owner) ownership).identity) {
            Log.debug("Identity is accepted. Now closing the session");
            EntityManager em = threadEntityManager.get();
            if ((null != em) && em.isOpen()) {
                em.close();
            }
            threadEntityManager.remove();
        } else {
            Log.debug("Identity is rejected. Ignoring the request");
        }
    }

    /**
     * Start a new database entity transaction.
     */
    public static void beginEntityTransaction() {
        EntityTransaction et = threadEntityTransaction.get();
        if (null == et) {
            et = getEntityManager().getTransaction();
            if (!et.isActive()) {
                et.begin();
                threadEntityTransaction.set(et);
            }
        }
    }

    /**
     * Commit the database entity transaction.
     */
    public static void commitEntityTransaction(Object owner) {
        EntityTransaction et = threadEntityTransaction.get();
        try {
            Log.debug("Trying to commit database transaction of this thread.");
            if ((null != et) && et.isActive()) {
                et.commit();
            }
            threadEntityTransaction.remove();
        } catch (RollbackException ex) {
            rollbackEntityTransaction(owner);
            throw new JerkBotException(ex);
        }
    }

    /**
     * Rollback the database entity transaction.
     */
    public static void rollbackEntityTransaction(Object owner) {
        EntityTransaction et = threadEntityTransaction.get();
        try {
            if ((null != et) && et.isActive()) {
                Log.debug("Trying to rollback database transaction of this thread.");
                et.rollback();
                threadEntityTransaction.remove();
            }
        } finally {
            closeEntityManager(owner);
        }
    }

    /**
     * Internal class , for handling the identity. Hidden for the
     * developers
     */
    private static class Owner {

        boolean identity = false;

        public Owner(boolean identity) {
            this.identity = identity;
        }
    }
}
