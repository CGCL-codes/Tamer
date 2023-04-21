package cn.jsprun.dao.impl;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.jsprun.dao.MagiclogDao;
import cn.jsprun.domain.Magiclog;
import cn.jsprun.utils.HibernateUtil;

public class MagiclogDaoImpl implements MagiclogDao {

    public boolean deleteMageiclog(Magiclog magiclog) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            session.delete(magiclog);
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

    @SuppressWarnings("unchecked")
    public List<Magiclog> findAllMagiclog() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery("from Magiclog");
            List<Magiclog> list = query.list();
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

    @SuppressWarnings("unchecked")
    public List<Magiclog> findMagiclogByAction(Byte[] action) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        StringBuffer sql = new StringBuffer("from Magiclog as m where m.id.action in (");
        for (int i = 0; i < action.length; i++) {
            sql.append(action[i]);
            sql.append(",");
        }
        String sqlstr = sql.substring(0, sql.length() - 1);
        sqlstr = sqlstr + ")";
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery(sqlstr);
            List<Magiclog> list = query.list();
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

    public boolean insertMagiclog(Magiclog magiclog) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            session.save(magiclog);
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

    public boolean modifyMagiclog(Magiclog magiclog) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            session.update(magiclog);
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

    @SuppressWarnings("unchecked")
    public List<Magiclog> findMageiclogByMageicId(Short magicid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery("from Magiclog as m where m.id.magicid = ?");
            query.setParameter(0, magicid);
            List<Magiclog> list = query.list();
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

    public List<Magiclog> getMagiclogByActionAndTargetUid(Integer targetuid, Byte[] action) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        StringBuffer hql = new StringBuffer("from Magiclog as m where m.id.targetuid=? and m.id.action in (");
        for (int i = 0; i < action.length; i++) {
            hql.append(action[i]);
            hql.append(",");
        }
        String sqlstr = hql.substring(0, hql.length() - 1);
        sqlstr = sqlstr + ")";
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery(sqlstr);
            query.setInteger(0, targetuid);
            List<Magiclog> list = query.list();
            transaction.commit();
            return list;
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            exception.printStackTrace();
            return null;
        }
    }

    public List<Magiclog> getMagiclogByActionAndUid(Integer uid, Byte[] action) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        StringBuffer hql = new StringBuffer("from Magiclog as m where m.id.uid=? and m.id.action in (");
        for (int i = 0; i < action.length; i++) {
            hql.append(action[i]);
            hql.append(",");
        }
        String sqlstr = hql.substring(0, hql.length() - 1);
        sqlstr = sqlstr + ")";
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery(sqlstr);
            query.setInteger(0, uid);
            List<Magiclog> list = query.list();
            transaction.commit();
            return list;
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            exception.printStackTrace();
            return null;
        }
    }

    public List<Magiclog> getMagiclogByActionAndTargetUid(Integer targetuid, Byte[] action, Integer firstNum, Integer maxNum) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        StringBuffer hql = new StringBuffer("from Magiclog as m where m.id.targetuid=? and m.id.action in (");
        for (int i = 0; i < action.length; i++) {
            hql.append(action[i]);
            hql.append(",");
        }
        String sqlstr = hql.substring(0, hql.length() - 1);
        sqlstr = sqlstr + ")";
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery(sqlstr);
            query.setInteger(0, targetuid);
            query.setFirstResult(firstNum);
            query.setMaxResults(maxNum);
            List<Magiclog> list = query.list();
            transaction.commit();
            return list;
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            exception.printStackTrace();
            return null;
        }
    }

    public List<Magiclog> getMagiclogByActionAndUid(Integer uid, Byte[] action, Integer firstNum, Integer maxNum) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        StringBuffer hql = new StringBuffer("from Magiclog as m where m.id.uid=? and m.id.action in (");
        for (int i = 0; i < action.length; i++) {
            hql.append(action[i]);
            hql.append(",");
        }
        String sqlstr = hql.substring(0, hql.length() - 1);
        sqlstr = sqlstr + ")";
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery(sqlstr);
            query.setInteger(0, uid);
            query.setFirstResult(firstNum);
            query.setMaxResults(maxNum);
            List<Magiclog> list = query.list();
            transaction.commit();
            return list;
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            exception.printStackTrace();
            return null;
        }
    }
}
