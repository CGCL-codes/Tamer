package com.babelstudio.cpasss.hibernate;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.babelstudio.cpasss.lib.Page;

/**
 * A data access object (DAO) providing persistence and search support for
 * Saledetail entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.babelstudio.cpasss.hibernate.Saledetail
 * @author MyEclipse Persistence Tools
 */
public class SaledetailDAO extends HibernateDaoSupport {

    private static final Logger log = LoggerFactory.getLogger(SaledetailDAO.class);

    public static final String AMOUNT = "amount";

    public static final String SALE_1 = "sale_1";

    public static final String MEMO = "memo";

    protected void initDao() {
    }

    public void save(Saledetail transientInstance) {
        log.debug("saving Saledetail instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(Saledetail persistentInstance) {
        log.debug("deleting Saledetail instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Saledetail findById(java.lang.Integer id) {
        log.debug("getting Saledetail instance with id: " + id);
        try {
            Saledetail instance = (Saledetail) getHibernateTemplate().get("com.babelstudio.cpasss.hibernate.Saledetail", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<Saledetail> findByExample(Saledetail instance) {
        log.debug("finding Saledetail instance by example");
        try {
            List<Saledetail> results = (List<Saledetail>) getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public Page findByPage(Page page, Saledetail instance) {
        log.debug("finding User instance by page");
        try {
            List<Saledetail> results = (List<Saledetail>) getHibernateTemplate().findByExample(instance, page.getStart(), page.getLimit());
            log.debug("find by page successful, result size: " + results.size());
            page.setTotalProperty(results.size());
            page.setRoot(results);
            page.setSuccess(true);
            Session session = this.getSession();
            String hql = "select count(*) from Saledetail as t";
            Query query = session.createQuery(hql);
            Number result = (Number) query.uniqueResult();
            long total = result.longValue();
            page.setTotalProperty((int) total);
            return page;
        } catch (RuntimeException re) {
            log.error("find by page failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding Saledetail instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Saledetail as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List<Saledetail> findByAmount(Object amount) {
        return findByProperty(AMOUNT, amount);
    }

    public List<Saledetail> findBySale_1(Object sale_1) {
        return findByProperty(SALE_1, sale_1);
    }

    public List<Saledetail> findByMemo(Object memo) {
        return findByProperty(MEMO, memo);
    }

    public List findAll() {
        log.debug("finding all Saledetail instances");
        try {
            String queryString = "from Saledetail";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public Saledetail merge(Saledetail detachedInstance) {
        log.debug("merging Saledetail instance");
        try {
            Saledetail result = (Saledetail) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Saledetail instance) {
        log.debug("attaching dirty Saledetail instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Saledetail instance) {
        log.debug("attaching clean Saledetail instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static SaledetailDAO getFromApplicationContext(ApplicationContext ctx) {
        return (SaledetailDAO) ctx.getBean("SaledetailDAO");
    }
}
