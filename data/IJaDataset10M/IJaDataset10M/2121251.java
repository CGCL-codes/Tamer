package gov.esporing.ost.storage.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private static String CONFIG_FILE_LOCATION = "/gov/esporing/ost/storage/hibernate/hibernate.cfg.xml";

    static {
        try {
            sessionFactory = new Configuration().configure(CONFIG_FILE_LOCATION).buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
