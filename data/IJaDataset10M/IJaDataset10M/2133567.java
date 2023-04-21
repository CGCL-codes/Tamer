package com.manning.junitbook.ch18;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Settings;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.event.EventListeners;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.impl.SessionImpl;
import org.hibernate.tool.ant.JPAConfigurationTask;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class AbstractJpaTestCase {

    private static EntityManagerFactory entityManagerFactory;

    protected static Connection connection;

    protected EntityManager em;

    @BeforeClass
    public static void setupDatabase() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("chapter-18");
        connection = getConnection(entityManagerFactory);
    }

    @AfterClass
    public static void closeDatabase() throws Exception {
        if (connection != null) {
            connection.prepareStatement("SHUTDOWN").execute();
            connection.close();
            connection = null;
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Before
    public void setEntityManager() {
        em = entityManagerFactory.createEntityManager();
        if (false) {
            Object delegate = em.getDelegate();
            SessionImpl session = (SessionImpl) delegate;
            EventListeners listeners = session.getListeners();
            PostInsertEventListener[] originalPostInsertEventListener = listeners.getPostInsertEventListeners();
            for (PostInsertEventListener listener : originalPostInsertEventListener) {
                System.err.println(">>> PostInsertEventListener: " + listener.getClass().getName());
            }
        }
    }

    @After
    public void closeEntityManager() {
        assert em != null;
        em.close();
    }

    public static Connection getConnection(Object object) throws Exception {
        Connection connection = null;
        if (object instanceof EntityManagerFactoryImpl) {
            EntityManagerFactoryImpl impl = (EntityManagerFactoryImpl) object;
            SessionFactory sessionFactory = impl.getSessionFactory();
            if (sessionFactory instanceof SessionFactoryImpl) {
                SessionFactoryImpl sfi = (SessionFactoryImpl) sessionFactory;
                Settings settings = sfi.getSettings();
                ConnectionProvider provider = settings.getConnectionProvider();
                connection = provider.getConnection();
            }
        }
        return connection;
    }

    protected void beginTransaction() {
        assert em != null;
        em.getTransaction().begin();
    }

    protected void commitTransaction() {
        assert em != null;
        em.getTransaction().commit();
    }

    protected void commitTransaction(boolean clearContext) {
        commitTransaction();
        if (clearContext) {
            em.clear();
        }
    }

    protected void analyzeSchema(SqlHandler handler) {
        ConfigurationCreator cfgCreator = new ConfigurationCreator();
        Configuration cfg = cfgCreator.createConfiguration();
        SchemaExport export = new SchemaExport(cfg);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        PrintStream newOut = new PrintStream(outputStream);
        System.setOut(newOut);
        try {
            export.create(true, true);
            final String sql = outputStream.toString();
            handler.handle(sql);
        } finally {
            System.setOut(oldOut);
            newOut.close();
        }
    }

    protected interface SqlHandler {

        void handle(String sql);
    }

    private class ConfigurationCreator extends JPAConfigurationTask {

        @Override
        protected Configuration createConfiguration() {
            return super.createConfiguration();
        }
    }
}
