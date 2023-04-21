package hospitalpets.control;

import org.hibernate.classic.Session;
import org.hibernate.*;
import org.hibernate.cfg.*;

/**
 * @netbeans.hibernate.util
 */
public class Configuracion {

    public static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Creacion fallida de la SessionFactory." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static final ThreadLocal session = new ThreadLocal();

    /**
     * 
     * @throws org.hibernate.HibernateException 
     * @return 
     */
    public Session currentSession() throws HibernateException {
        Session s = (Session) session.get();
        if (s == null) {
            s = sessionFactory.openSession();
            session.set(s);
        }
        return s;
    }

    public SessionFactory getSesion() {
        return sessionFactory;
    }

    public void cerrar() {
        sessionFactory.close();
    }

    /**
     * 
     * @throws org.hibernate.HibernateException 
     */
    public static void closeSession() throws HibernateException {
        Session s = (Session) session.get();
        if (s != null) s.close();
        session.set(null);
    }
}
