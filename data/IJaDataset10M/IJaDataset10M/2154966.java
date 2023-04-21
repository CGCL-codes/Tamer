package model.myutil;

import model.dao.DAOLogDB;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Tidus Le
 */
public class myConnector {

    static SessionFactory factory;

    static Session session;

    public static void open_conect() {
        try {
            factory = new Configuration().configure("/model/myutil/hibernate.cfg.xml").buildSessionFactory();
            session = factory.openSession();
        } catch (Exception ex) {
            DAOLogDB.write("DAOProduct.open_conect " + ex.getMessage());
        }
    }

    public static void close_conect() {
        try {
            session.close();
        } catch (Exception ex) {
            DAOLogDB.write("DAOProduct.close_conect " + ex.getMessage());
        }
    }

    public static Session getSession() {
        return session;
    }
}
