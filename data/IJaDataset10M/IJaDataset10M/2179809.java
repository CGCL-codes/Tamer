package com.narirelays.ems.persistence.orm;

import java.util.List;
import java.util.Set;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * ResourceCategory entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.narirelays.ems.persistence.orm.ResourceCategory
 * @author MyEclipse Persistence Tools
 */
public class ResourceCategoryDAO extends HibernateDaoSupport {

    private static final Logger log = LoggerFactory.getLogger(ResourceCategoryDAO.class);

    public static final String NAME = "name";

    protected void initDao() {
    }

    public void save(ResourceCategory transientInstance) {
        log.debug("saving ResourceCategory instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(ResourceCategory persistentInstance) {
        log.debug("deleting ResourceCategory instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public ResourceCategory findById(java.lang.String id) {
        log.debug("getting ResourceCategory instance with id: " + id);
        try {
            ResourceCategory instance = (ResourceCategory) getHibernateTemplate().get("com.narirelays.ems.persistence.orm.ResourceCategory", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(ResourceCategory instance) {
        log.debug("finding ResourceCategory instance by example");
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
        log.debug("finding ResourceCategory instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from ResourceCategory as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByName(Object name) {
        return findByProperty(NAME, name);
    }

    public List findAll() {
        log.debug("finding all ResourceCategory instances");
        try {
            String queryString = "from ResourceCategory";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public ResourceCategory merge(ResourceCategory detachedInstance) {
        log.debug("merging ResourceCategory instance");
        try {
            ResourceCategory result = (ResourceCategory) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(ResourceCategory instance) {
        log.debug("attaching dirty ResourceCategory instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(ResourceCategory instance) {
        log.debug("attaching clean ResourceCategory instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static ResourceCategoryDAO getFromApplicationContext(ApplicationContext ctx) {
        return (ResourceCategoryDAO) ctx.getBean("ResourceCategoryDAO");
    }
}
