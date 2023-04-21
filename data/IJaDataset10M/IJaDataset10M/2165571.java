package by.oslab.hachathon.easycar.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Class to store hibernate session factory and makes basic hibernate operations
 * 
 * 
 * @author Константин
 * @version $Rev: $
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    private static final ThreadLocal threadSession = new ThreadLocal();

    private static final ThreadLocal threadTransaction = new ThreadLocal();

    static {
        try {
            Configuration cfg = new Configuration();
            sessionFactory = cfg.configure().buildSessionFactory();
        } catch (Throwable ex) {
            ex.printStackTrace(System.out);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
	 * Creates session in thread local is it doesn't exist
	 * 
	 * @return
	 * @version 1
	 */
    public static Session getSession() {
        Session s = (Session) threadSession.get();
        try {
            if (s == null) {
                s = sessionFactory.openSession();
                threadSession.set(s);
            }
        } catch (HibernateException ex) {
            throw ex;
        }
        return s;
    }

    /**
	 * Closes session in thread local
	 * 
	 * @version 1
	 */
    public static void closeSession() {
        try {
            Session s = (Session) threadSession.get();
            threadSession.set(null);
            if (s != null && s.isOpen()) {
                s.close();
            }
        } catch (HibernateException ex) {
            throw ex;
        }
    }

    /**
	 * Starts transaction in thread local
	 * 
	 * @version 1
	 */
    public static void beginTransaction() {
        Transaction tx = (Transaction) threadTransaction.get();
        try {
            if (tx == null) {
                tx = getSession().beginTransaction();
                threadTransaction.set(tx);
            }
        } catch (HibernateException ex) {
            throw ex;
        }
    }

    /**
	 * Commits thread local transaction
	 * 
	 * @version 1
	 */
    public static void commitTransaction() {
        Transaction tx = (Transaction) threadTransaction.get();
        try {
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
                tx.commit();
                threadTransaction.set(null);
            }
        } catch (HibernateException ex) {
            rollbackTransaction();
            throw ex;
        }
    }

    /**
	 * Rollbacks thread local transaction
	 * 
	 * @version 1
	 */
    public static void rollbackTransaction() {
        Transaction tx = (Transaction) threadTransaction.get();
        try {
            threadTransaction.set(null);
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
                tx.rollback();
            }
        } catch (HibernateException ex) {
            throw ex;
        } finally {
            closeSession();
        }
    }
}
