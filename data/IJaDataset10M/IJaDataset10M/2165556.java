package cn.jsprun.dao.impl;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.jsprun.dao.RewardlogDao;
import cn.jsprun.domain.Rewardlog;
import cn.jsprun.utils.HibernateUtil;

public class RewardlogDaoImpl implements RewardlogDao {

    public boolean deleteRewardlogByTid(int tid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery("delete from Rewardlog as r where r.id.tid=?");
            query.setParameter(0, tid);
            query.executeUpdate();
            tr.commit();
            return true;
        } catch (HibernateException er) {
            if (tr != null) {
                tr.rollback();
            }
            er.printStackTrace();
        }
        return false;
    }

    public List<Rewardlog> findRewardlogByHql(String hql, int start, int maxrow) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setFirstResult(start);
            query.setMaxResults(maxrow);
            List<Rewardlog> list = query.list();
            tr.commit();
            return list;
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public int findRewardlogCountByHql(String hql) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            List list = query.list();
            tr.commit();
            if (list != null && list.size() > 0) {
                return (Integer) list.get(0);
            }
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return 0;
    }

    public boolean insertRewardlog(Rewardlog rewardlog) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            session.save(rewardlog);
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
}
