package mpower_hibernate;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Grabadora
 */
public class MedicalDataPointFacade {

    public void save(MedicalDataPoint a) throws HibernateException {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(a);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw e;
        } finally {
            mpower_hibernate.HibernateUtil.closeSession();
        }
    }

    public void delete(MedicalDataPoint a) throws HibernateException {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        try {
            session.delete(a);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw e;
        } finally {
            mpower_hibernate.HibernateUtil.closeSession();
        }
    }

    public mpower_hibernate.MedicalDataPoint findById(long id) {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        org.hibernate.Query query = session.createQuery(" select aa " + " from  " + " MedicalDataPoint as aa " + "  where  " + " aa.id = ? ");
        query.setLong(0, id);
        return (mpower_hibernate.MedicalDataPoint) query.uniqueResult();
    }

    public List findByType(long type) {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        org.hibernate.Query query = session.createQuery(" select aa " + " from  " + " MedicalDataPoint as aa " + "  where  " + " aa.medicaltype = ? ");
        query.setLong(0, type);
        return query.list();
    }

    public List getAll() {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        org.hibernate.Query query = session.createQuery(" select aa " + " from  " + " MedicalDataPoint as aa ");
        return query.list();
    }

    public List findByUser(long user) {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        org.hibernate.Query query = session.createQuery(" select aa " + " from  " + " MedicalDataPoint as aa " + "  where  " + " aa.user = ?  ORDER by aa.time DESC ");
        query.setLong(0, user);
        return query.list();
    }

    public List findByUserAndType(long user, long type) {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        org.hibernate.Query query = session.createQuery(" select aa " + " from  " + " MedicalDataPoint as aa " + "  where  " + " aa.user = ? AND aa.medicaltype= ?");
        query.setLong(0, user);
        query.setLong(1, type);
        return query.list();
    }
}
