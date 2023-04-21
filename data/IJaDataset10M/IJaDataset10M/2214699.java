package mpower_hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.*;

/**
 *
 * @author etkivbe
 */
public class PermissionFacade {

    public void savePermission(Permission aa) {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        session.save(aa);
        tx.commit();
        mpower_hibernate.HibernateUtil.closeSession();
    }

    public mpower_hibernate.Permission findById(String aaID) {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        Permission p = new Permission();
        session.load(p, aaID);
        tx.commit();
        mpower_hibernate.HibernateUtil.closeSession();
        return p;
    }
}
