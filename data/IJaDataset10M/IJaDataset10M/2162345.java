package com.hmw.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate 工具类 用于初始化Hibernate,并进行Session和Transaction的管理
 */
public class HibernateUtils {

    private static Configuration configuration;

    private static SessionFactory sessionFactory;

    /** 保存Session对象实例的线程局部变量 */
    public static final ThreadLocal<Session> threadLocalSession = new ThreadLocal<Session>();

    /** 保存Transaction对象实例的线程局部变量 */
    public static final ThreadLocal<Transaction> threadLocalTransaction = new ThreadLocal<Transaction>();

    private static final ThreadLocal<Interceptor> threadInterceptor = new ThreadLocal<Interceptor>();

    static {
        try {
            configuration = new Configuration();
            sessionFactory = configuration.configure().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    private HibernateUtils() {
    }

    public static Session currentSession() {
        Session session = threadLocalSession.get();
        try {
            if (session == null || !session.isOpen()) {
                session = openSession();
                threadLocalSession.set(session);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取Session失败");
        }
        return session;
    }

    public static Session openSession() throws Exception {
        return getSessionFactory().openSession();
    }

    /**
	 * Returns the original Hibernate configuration.
	 * 
	 * @return Configuration
	 */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
	 * Returns the SessionFactory used for this static class.
	 * 
	 * @return SessionFactory
	 */
    public static SessionFactory getSessionFactory() throws Exception {
        return sessionFactory;
    }

    /**
	 * Closes the Session local to the thread.
	 */
    public static void closeSession() {
        Session session = (Session) threadLocalSession.get();
        threadLocalSession.set(null);
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("关闭Session失败");
        }
    }

    public static void beginTransaction() {
        Transaction tx = (Transaction) threadLocalTransaction.get();
        try {
            if (tx == null) {
                tx = currentSession().beginTransaction();
                threadLocalTransaction.set(tx);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("开始事务失败");
        }
    }

    public static void commitTransaction() {
        Transaction tx = (Transaction) threadLocalTransaction.get();
        try {
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
                tx.commit();
            }
            threadLocalTransaction.set(null);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("提交失败");
        } finally {
            closeSession();
        }
    }

    public static void rollbackTransaction() {
        Transaction tx = (Transaction) threadLocalTransaction.get();
        try {
            threadLocalTransaction.set(null);
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) tx.rollback();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("回滚失败");
        } finally {
            closeSession();
        }
    }

    /**
	 * Rebuild the SessionFactory with the static Configuration.
	 * 
	 */
    public static void rebuildSessionFactory() {
        synchronized (sessionFactory) {
            try {
                sessionFactory = getConfiguration().buildSessionFactory();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
	 * Rebuild the SessionFactory with the given Hibernate Configuration.
	 * 
	 * @param cfg
	 */
    public static void rebuildSessionFactory(Configuration cfg) {
        synchronized (sessionFactory) {
            try {
                sessionFactory = cfg.buildSessionFactory();
                configuration = cfg;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
	 * Retrieves the current Session local to the thread.
	 * <p/>
	 * If no Session is open, opens a new Session for the running thread.
	 * 
	 * @return Session
	 */
    public static Session getSession() {
        Session s = threadLocalSession.get();
        try {
            if (s == null) {
                if (getInterceptor() != null) {
                    System.out.println("Using interceptor: " + getInterceptor().getClass());
                    s = getSessionFactory().openSession(getInterceptor());
                } else {
                    s = getSessionFactory().openSession();
                }
                threadLocalSession.set(s);
            }
        } catch (Exception ex) {
            throw new HibernateException(ex);
        }
        return s;
    }

    /**
	 * Reconnects a Hibernate Session to the current Thread.
	 * 
	 * @param session
	 *            The Hibernate Session to be reconnected.
	 */
    @SuppressWarnings("deprecation")
    public static void reconnect(Session session) {
        try {
            session.reconnect();
            threadLocalSession.set(session);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * Disconnect and return Session from current Thread.
	 * 
	 * @return Session the disconnected Session
	 */
    public static Session disconnectSession() {
        Session session = getSession();
        try {
            threadLocalSession.set(null);
            if (session.isConnected() && session.isOpen()) session.disconnect();
        } catch (HibernateException ex) {
            throw new HibernateException(ex);
        }
        return session;
    }

    /**
	 * Register a Hibernate interceptor with the current thread.
	 * <p>
	 * Every Session opened is opened with this interceptor after registration.
	 * Has no effect if the current Session of the thread is already open,
	 * effective on next close()/getSession().
	 */
    public static void registerInterceptor(Interceptor interceptor) {
        threadInterceptor.set(interceptor);
    }

    private static Interceptor getInterceptor() {
        Interceptor interceptor = threadInterceptor.get();
        return interceptor;
    }
}
