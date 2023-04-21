package com.ecs.etrade.da;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppUsers entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.ecs.etrade.da.AppUsers
 * @author MyEclipse Persistence Tools
 */
public class AppUsersDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(AppUsersDAO.class);

    public static final String CURRENT_PASSWORD = "currentPassword";

    public static final String PREVIOUS_PASSWORD = "previousPassword";

    protected void initDao() {
    }

    public void save(AppUsers transientInstance) {
        log.debug("saving AppUsers instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(AppUsers persistentInstance) {
        log.debug("deleting AppUsers instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public AppUsers findById(java.lang.String id) {
        log.debug("getting AppUsers instance with id: " + id);
        try {
            AppUsers instance = (AppUsers) getHibernateTemplate().get("com.ecs.etrade.da.AppUsers", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(AppUsers instance) {
        log.debug("finding AppUsers instance by example");
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
        log.debug("finding AppUsers instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from AppUsers as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByCurrentPassword(Object currentPassword) {
        return findByProperty(CURRENT_PASSWORD, currentPassword);
    }

    public List findByPreviousPassword(Object previousPassword) {
        return findByProperty(PREVIOUS_PASSWORD, previousPassword);
    }

    public List findAll() {
        log.debug("finding all AppUsers instances");
        try {
            String queryString = "from AppUsers";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public AppUsers merge(AppUsers detachedInstance) {
        log.debug("merging AppUsers instance");
        try {
            AppUsers result = (AppUsers) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(AppUsers instance) {
        log.debug("attaching dirty AppUsers instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(AppUsers instance) {
        log.debug("attaching clean AppUsers instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static AppUsersDAO getFromApplicationContext(ApplicationContext ctx) {
        return (AppUsersDAO) ctx.getBean("AppUsersDAO");
    }
}
