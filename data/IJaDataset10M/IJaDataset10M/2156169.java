package joecohen.hibernatedemo;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibUtil {

    private static boolean transaction = false;

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session startTransaction() {
        while (transaction == true) ;
        transaction = true;
        try {
            Session s = sessionFactory.getCurrentSession();
            s.beginTransaction();
            return s;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            transaction = false;
            return null;
        }
    }

    public static void commitTransaction() {
        try {
            sessionFactory.getCurrentSession().getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        transaction = false;
    }

    public static void shutdown() {
        try {
            startTransaction();
            Query sql = sessionFactory.getCurrentSession().createSQLQuery("SHUTDOWN");
            sql.executeUpdate();
            commitTransaction();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openDBEditor() {
        String[] s = new String[] { "--driver", "org.hsqldb.jdbcDriver", "--url", "jdbc:hsqldb:file:db/testdb", "--user", "sa" };
        org.hsqldb.util.DatabaseManagerSwing.main(s);
    }
}
