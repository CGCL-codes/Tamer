package net.sf.raptor.hibernate;

import java.sql.SQLException;
import java.util.Properties;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * 
 */
public class StatefullSessionManager implements SessionManager {

    private SessionFactory sessionFactory = null;

    private Session session = null;

    private Configuration cfg;

    public StatefullSessionManager() {
    }

    /**
     * getSession
     */
    public Session getSession() throws java.sql.SQLException, HibernateException {
        return session;
    }

    /**
	 * initSession
	 */
    public void initSession() throws SQLException, HibernateException {
        initConfiguration();
        sessionFactory = cfg.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public void initConfiguration() throws MappingException {
        String orSchema = System.getProperty("ORSchema", "ORSchema.xml");
        cfg = new Configuration();
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.HSQLDialect");
        connectionProperties.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
        connectionProperties.setProperty("hibernate.connection.username", "sa");
        connectionProperties.setProperty("hibernate.connection.password", "");
        connectionProperties.setProperty("hibernate.connection.url", "jdbc:hsqldb:c:\\tmp\\hibtestdb");
        cfg.addProperties(connectionProperties);
        cfg.addFile(orSchema);
    }

    public static void main(String[] args) throws HibernateException {
        System.setProperty("ORSchema", "./target/classes/de/xcom/or/test/test_or_schema.xml");
        StatefullSessionManager mgr = new StatefullSessionManager();
        mgr.initConfiguration();
        SchemaExport export = new SchemaExport(mgr.cfg);
        export.create(false, true);
    }

    /**
	 * releaseSession
	 */
    public void releaseSession(Session session) throws java.sql.SQLException, HibernateException {
    }

    /**
	 * terminateSession 
	 */
    public void terminateSession() throws java.sql.SQLException, HibernateException {
        if (session != null) {
            try {
                session.connection().commit();
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
