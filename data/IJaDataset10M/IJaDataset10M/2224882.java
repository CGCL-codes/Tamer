package pl.nni.msz.db.interfaces.dbi;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.nni.msz.db.SessionFactory;

public class Lock {

    public void lockUsers() {
        Session session = null;
        try {
            session = SessionFactory.currentSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("update Users user set user.locked = true");
            q.executeUpdate();
            tx.commit();
            session.clear();
        } catch (ObjectNotFoundException onfe) {
        } catch (HibernateException e) {
            System.err.println("Hibernate Exception" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void unlockUsers() {
        Session session = null;
        try {
            session = SessionFactory.currentSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("update Users user set user.locked = false");
            q.executeUpdate();
            tx.commit();
            session.clear();
        } catch (ObjectNotFoundException onfe) {
        } catch (HibernateException e) {
            System.err.println("Hibernate Exception" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
