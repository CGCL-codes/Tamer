package cn.jsprun.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.jsprun.dao.ForumfieldsDao;
import cn.jsprun.domain.Forumfields;
import cn.jsprun.utils.HibernateUtil;

public class ForumfieldsDaoImpl implements ForumfieldsDao {

    public boolean addForumfield(Forumfields forumfield) {
        boolean flag = false;
        if (forumfield != null) {
            Session session = null;
            Transaction tran = null;
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                tran = session.beginTransaction();
                session.save(forumfield);
                flag = true;
                tran.commit();
            } catch (HibernateException e) {
                flag = false;
                if (tran != null) {
                    tran.rollback();
                }
                e.printStackTrace();
            }
        }
        return flag;
    }

    public boolean removeForumfield(Forumfields forumfield) {
        boolean flag = false;
        if (forumfield != null) {
            Session session = null;
            Transaction tran = null;
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                tran = session.beginTransaction();
                session.delete(forumfield);
                flag = true;
                tran.commit();
            } catch (HibernateException e) {
                flag = false;
                if (tran != null) {
                    tran.rollback();
                }
                e.printStackTrace();
            }
        }
        return flag;
    }

    public boolean updateForumfield(Forumfields forumfield) {
        boolean flag = false;
        if (forumfield != null) {
            Session session = null;
            Transaction tran = null;
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                tran = session.beginTransaction();
                session.saveOrUpdate(forumfield);
                flag = true;
                tran.commit();
            } catch (HibernateException e) {
                flag = false;
                if (tran != null) {
                    tran.rollback();
                }
                e.printStackTrace();
            }
        }
        return flag;
    }

    public Forumfields findById(Short fid) {
        Session session = null;
        Transaction tran = null;
        Forumfields forumfield = null;
        if (fid != null) {
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                tran = session.beginTransaction();
                forumfield = (Forumfields) session.get(Forumfields.class, fid);
                tran.commit();
            } catch (HibernateException e) {
                if (tran != null) {
                    tran.rollback();
                }
                e.printStackTrace();
            }
        }
        return forumfield;
    }

    @SuppressWarnings("unchecked")
    public List<Forumfields> findAll() {
        Session session = null;
        Transaction tran = null;
        Query query = null;
        List<Forumfields> forumfields = null;
        try {
            forumfields = new ArrayList<Forumfields>();
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tran = session.beginTransaction();
            query = session.createQuery("from Forumfields");
            forumfields = query.list();
            tran.commit();
        } catch (HibernateException e) {
            if (tran != null) {
                tran.rollback();
            }
            e.printStackTrace();
        }
        return forumfields;
    }

    public List<Forumfields> findById(List<Short> id) {
        Session session = null;
        Transaction tran = null;
        List<Forumfields> forumfieldsList = new ArrayList<Forumfields>();
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tran = session.beginTransaction();
            for (int i = 0; i < id.size(); i++) {
                Forumfields forumfields = (Forumfields) session.get(Forumfields.class, id.get(i));
                forumfieldsList.add(forumfields);
            }
            tran.commit();
        } catch (HibernateException e) {
            if (tran != null) {
                tran.rollback();
            }
            e.printStackTrace();
        }
        return forumfieldsList;
    }

    public void updateForumfields(List<Forumfields> forumsfUpdateList) {
        Session session = null;
        Transaction tran = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tran = session.beginTransaction();
            for (int i = 0; i < forumsfUpdateList.size(); i++) {
                session.update(forumsfUpdateList.get(i));
            }
            tran.commit();
        } catch (HibernateException e) {
            if (tran != null) {
                tran.rollback();
            }
            e.printStackTrace();
        }
    }
}
