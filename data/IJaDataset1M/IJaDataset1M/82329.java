package com.ecs.etrade.da;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * MarginDetails entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.ecs.etrade.da.MarginDetails
 * @author MyEclipse Persistence Tools
 */
public class MarginDetailsDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(MarginDetailsDAO.class);

    public static final String VERSION = "version";

    public static final String MARGIN_PCT = "marginPct";

    protected void initDao() {
    }

    public void save(MarginDetails transientInstance) {
        log.debug("saving MarginDetails instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(MarginDetails persistentInstance) {
        log.debug("deleting MarginDetails instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public MarginDetails findById(com.ecs.etrade.da.MarginDetailsId id) {
        log.debug("getting MarginDetails instance with id: " + id);
        try {
            MarginDetails instance = (MarginDetails) getHibernateTemplate().get("com.ecs.etrade.da.MarginDetails", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(MarginDetails instance) {
        log.debug("finding MarginDetails instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding MarginDetails instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from MarginDetails as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByVersion(Object version) {
        return findByProperty(VERSION, version);
    }

    public List findByMarginPct(Object marginPct) {
        return findByProperty(MARGIN_PCT, marginPct);
    }

    public List findAll() {
        log.debug("finding all MarginDetails instances");
        try {
            String queryString = "from MarginDetails";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public MarginDetails merge(MarginDetails detachedInstance) {
        log.debug("merging MarginDetails instance");
        try {
            MarginDetails result = (MarginDetails) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(MarginDetails instance) {
        log.debug("attaching dirty MarginDetails instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(MarginDetails instance) {
        log.debug("attaching clean MarginDetails instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static MarginDetailsDAO getFromApplicationContext(ApplicationContext ctx) {
        return (MarginDetailsDAO) ctx.getBean("MarginDetailsDAO");
    }
}
