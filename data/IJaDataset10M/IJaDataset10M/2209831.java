package com.pioneer.app.proview;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.JDBCConnectionException;
import config.BaseHibernateDAO;
import config.HibernateSessionFactory;

public class ProcessDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(Process.class);

    public Process findById(java.lang.String id) {
        log.debug("getting Process instance with id: " + id);
        try {
            Process instance = (Process) getSession().get("com.pioneer.app.proview.Process", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public void delete(Process persistentInstance) {
        log.debug("deletting Process persistentInstance  ");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public void add(Process persistentInstance) {
        log.debug("deletting Process persistentInstance  ");
        try {
            getSession().save(persistentInstance);
            log.debug("add successful");
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public void update(Process persistentInstance) {
        log.debug("deletting Process persistentInstance  ");
        try {
            getSession().update(persistentInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByCondition(String condition) {
        log.debug("finding by condition " + condition);
        try {
            String queryString = null;
            if (null != condition) {
                queryString = "from Process where " + condition;
            } else {
                queryString = "from Process";
            }
            queryString += " order by name";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setMaxResults(500);
            return queryObject.list();
        } catch (JDBCConnectionException re) {
            log.error("get failed", re);
            throw re;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public void deleteObjs(java.lang.String[] ids) {
        log.debug("deleting by objects ");
        Transaction tx = getSession().beginTransaction();
        try {
            if (null != ids) {
                Process obj = null;
                java.lang.String id = null;
                for (int i = 0; i < ids.length; i++) {
                    id = ids[i];
                    obj = this.findById(id);
                    this.delete(obj);
                }
                tx.commit();
            }
        } catch (RuntimeException re) {
            log.error("get failed", re);
            tx.rollback();
            throw re;
        }
    }
}
