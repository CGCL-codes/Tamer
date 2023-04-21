package cn.jsprun.dao.impl;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.jsprun.dao.BuddysDao;
import cn.jsprun.domain.Buddys;
import cn.jsprun.utils.HibernateUtil;

public class BuddysDaoImpl implements BuddysDao {

    public boolean deleteBuddysByUid(int uid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery("delete from Buddys as b where b.id.uid=?");
            query.setParameter(0, uid);
            query.executeUpdate();
            tr.commit();
            return true;
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public List<Buddys> findBuddysByUid(int uid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery("from Buddys as b where b.id.uid = ? ");
            query.setParameter(0, uid);
            List<Buddys> list = query.list();
            tr.commit();
            return list;
        } catch (HibernateException he) {
            if (tr != null) {
                tr.rollback();
            }
            he.printStackTrace();
        }
        return null;
    }

    public boolean insertBuddys(Buddys buddy) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            session.save(buddy);
            tr.commit();
            return true;
        } catch (HibernateException he) {
            if (tr != null) {
                tr.rollback();
            }
            he.printStackTrace();
        }
        return false;
    }
}
