package org.mahjong.matoso.util;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.mahjong.matoso.util.exception.FatalException;

/**
 * Utility class for using hibernate.<br>
 * Manage SessionFactoy, Session and Transaction.
 * 
 * @author ctrung
 * @date 26 juin 2009
 */
public class HibernateUtil {

    /** Logger */
    private static Logger logger = Logger.getLogger(HibernateUtil.class);

    /** Property key of the system to get the project of Matoso */
    private static final String PROJECT_KEY = "MATOSO_HOME";

    /** Hibernate configuration instance */
    private static Configuration configuration;

    /** Hibernate session factory instance */
    private static SessionFactory sessionFactory;

    /** Session is stocked in a ThreadLocal instance */
    private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();

    /** Transaction is stocked in a ThreadLocal instance */
    private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

    static {
        try {
            configuration = new Configuration().configure();
            String connectionUrl = configuration.getProperty(Environment.URL);
            if (logger.isDebugEnabled()) logger.info("connectionUrl = " + connectionUrl);
            String matosoHome = System.getProperty(PROJECT_KEY);
            if (logger.isDebugEnabled()) logger.info("matosoHome = " + matosoHome);
            connectionUrl = connectionUrl.replace("${MATOSO_HOME}", matosoHome);
            if (logger.isInfoEnabled()) logger.info("connection url = " + connectionUrl);
            configuration.setProperty(Environment.URL, connectionUrl);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable t) {
            logger.error("Building session factory failed", t);
            throw new ExceptionInInitializerError(t);
        }
    }

    /**
	 * Retrieves the current Session local to the thread.<br>
	 * If no session is opened, opens a new session for the current thread.
	 * 
	 * @return Session
	 * @throws FatalException
	 *             Thrown if Session can't be built.
	 */
    public static Session getSession() throws FatalException {
        Session s = threadSession.get();
        try {
            if (s == null) {
                if (logger.isDebugEnabled()) logger.debug("Opening new Session for this thread.");
                s = sessionFactory.openSession();
            }
            threadSession.set(s);
        } catch (HibernateException e) {
            throw new FatalException(e);
        }
        return s;
    }

    /**
	 * Close the current Session local to the tread.
	 * 
	 * @throws FatalException
	 *             Indicates the Session couldn't be closed.
	 */
    public static void closeSession() throws FatalException {
        try {
            Session s = threadSession.get();
            threadSession.set(null);
            if (s != null && s.isOpen()) {
                if (logger.isDebugEnabled()) logger.debug("Closing Session of this thread");
                s.close();
            }
        } catch (HibernateException e) {
            throw new FatalException(e);
        }
    }

    /**
	 * Start a new database transaction.
	 * 
	 * @throws FatalException
	 */
    public static void beginTransaction() throws FatalException {
        Transaction tx = threadTransaction.get();
        try {
            if (tx == null) {
                if (logger.isDebugEnabled()) logger.debug("Starting new database transaction in this thread.");
                tx = getSession().beginTransaction();
                threadTransaction.set(tx);
            }
        } catch (HibernateException e) {
            throw new FatalException(e);
        }
    }

    /**
	 * Commit the transaction.
	 * 
	 * @throws FatalException
	 */
    public static void commitTransaction() throws FatalException {
        Transaction tx = threadTransaction.get();
        try {
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
                if (logger.isDebugEnabled()) logger.debug("Commiting database transaction of this thread.");
                tx.commit();
            }
            threadTransaction.set(null);
        } catch (HibernateException e) {
            rollbackTransaction();
            throw new FatalException(e);
        }
    }

    /**
	 * Rollback the transaction.
	 * 
	 * @throws FatalException
	 */
    public static void rollbackTransaction() throws FatalException {
        Transaction tx = threadTransaction.get();
        try {
            threadTransaction.set(null);
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
                if (logger.isDebugEnabled()) logger.debug("Trying to rollback the database transaction of this thread.");
                tx.rollback();
            }
        } catch (HibernateException e) {
            throw new FatalException(e);
        } finally {
            closeSession();
        }
    }

    /**
	 * Hibernate initialization.
	 */
    public static void init() {
    }

    public static void save(Object object) throws FatalException {
        beginTransaction();
        getSession().save(object);
        commitTransaction();
    }

    public static void delete(Object object) throws FatalException {
        beginTransaction();
        getSession().delete(object);
        commitTransaction();
    }
}
