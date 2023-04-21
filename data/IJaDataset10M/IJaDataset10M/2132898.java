package cn.jsprun.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.jsprun.dao.TypemodelsDao;
import cn.jsprun.domain.Typemodels;
import cn.jsprun.utils.HibernateUtil;

public class TypemodelsDaoImpl implements TypemodelsDao {

    public boolean addTypemodel(Typemodels typemodel) {
        boolean flag = false;
        if (typemodel != null) {
            Session session = null;
            Transaction tran = null;
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                tran = session.beginTransaction();
                session.save(typemodel);
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

    public boolean removeTypemodel(Typemodels typemodel) {
        boolean flag = false;
        if (typemodel != null) {
            Session session = null;
            Transaction tran = null;
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                tran = session.beginTransaction();
                session.delete(typemodel);
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

    public boolean updateTypemodel(Typemodels typemodel) {
        boolean flag = false;
        if (typemodel != null) {
            Session session = null;
            Transaction tran = null;
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                tran = session.beginTransaction();
                session.saveOrUpdate(typemodel);
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

    @SuppressWarnings("unchecked")
    public List<Typemodels> findAll() {
        Session session = null;
        Transaction tran = null;
        Query query = null;
        List<Typemodels> typemodels = null;
        try {
            typemodels = new ArrayList<Typemodels>();
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tran = session.beginTransaction();
            query = session.createQuery("from Typemodels order by displayorder");
            typemodels = query.list();
            tran.commit();
        } catch (HibernateException e) {
            if (tran != null) {
                tran.rollback();
            }
            e.printStackTrace();
        }
        return typemodels;
    }

    public Typemodels findById(Short id) {
        Session session = null;
        Transaction tran = null;
        Typemodels typemodel = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tran = session.beginTransaction();
            typemodel = (Typemodels) session.get(Typemodels.class, id);
            tran.commit();
        } catch (HibernateException e) {
            if (tran != null) {
                tran.rollback();
            }
            e.printStackTrace();
        }
        return typemodel;
    }

    @SuppressWarnings("unchecked")
    public List<Typemodels> findByProperty(String propertyName, Object value) {
        Session session = null;
        Transaction tran = null;
        Query query = null;
        List<Typemodels> typemodels = null;
        try {
            typemodels = new ArrayList<Typemodels>();
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tran = session.beginTransaction();
            query = session.createQuery("from Typemodels as t where t." + propertyName + "=? order by displayorder");
            query.setString(0, value.toString());
            typemodels = query.list();
            tran.commit();
        } catch (HibernateException e) {
            if (tran != null) {
                tran.rollback();
            }
            e.printStackTrace();
        }
        return typemodels;
    }
}
