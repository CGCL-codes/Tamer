package net.sf.cantina.datasource;

import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.cfg.Configuration;
import org.apache.log4j.Logger;

/**
 * @author Stephane JAIS
 */
public class HypersonicDataSource extends HibernateDataSource {

    protected static final ThreadLocal session = new ThreadLocal();

    protected static SessionFactory sessionFactory;

    private static final Logger logger = Logger.getLogger(HypersonicDataSource.class);

    private String itsPath;

    private boolean itsCreate;

    public HypersonicDataSource(String itsPath, boolean itsCreate) {
        this.itsPath = itsPath;
        this.itsCreate = itsCreate;
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory != null) return sessionFactory;
        logger.debug("Creating hsql session factory. path=" + itsPath);
        try {
            Configuration config = new Configuration().configure();
            config.getProperties().clear();
            config.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver").setProperty("hibernate.dialect", "net.sf.hibernate.dialect.HSQLDialect").setProperty("hibernate.connection.url", "jdbc:hsqldb:" + itsPath).setProperty("hibernate.connection.username", "sa").setProperty("hibernate.connection.password", "");
            config.setProperty("hibernate.hbm2ddl.auto", itsCreate ? "create" : "");
            sessionFactory = config.buildSessionFactory();
        } catch (HibernateException e) {
            logger.error("Could not configure hsql.", e);
        }
        return sessionFactory;
    }

    public void closeSessionFactory() {
        if (sessionFactory != null) {
            logger.debug("Closing hibernate session factory.");
            try {
                sessionFactory.close();
            } catch (HibernateException e) {
                throw new RuntimeException(e);
            }
            sessionFactory = null;
        }
    }

    public Session getSession() throws HibernateException {
        Session s = (Session) session.get();
        if (s == null || !(s.isOpen())) {
            logger.debug("Opening hibernate session.");
            s = getSessionFactory().openSession();
            session.set(s);
            return s;
        }
        return s;
    }

    /**
   * Close the hibernate (SQL) connection.
   *
   * @see #getSession()
   */
    public void closeSession() {
        try {
            Session s = (Session) session.get();
            if (s != null && s.isOpen()) {
                logger.debug("Closing hibernate session.");
                s.close();
            }
        } catch (HibernateException e) {
            logger.warn("Problem closing session.", e);
        }
    }
}
