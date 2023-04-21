package semestralka.server;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionF;

    static {
        try {
            sessionF = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Exception in inicialization:" + ex.toString());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session openSession() {
        try {
            return sessionF.openSession();
        } catch (Throwable ex) {
            System.err.println("Exception in opnening session: " + ex.toString());
            throw new HibernateException(ex);
        }
    }
}
