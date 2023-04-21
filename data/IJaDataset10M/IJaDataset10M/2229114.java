package com.narirelays.ems.persistence.orm;

import java.util.List;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * EquipmentFitting entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.narirelays.ems.persistence.orm.EquipmentFitting
 * @author MyEclipse Persistence Tools
 */
public class EquipmentFittingDAO extends HibernateDaoSupport {

    private static final Logger log = LoggerFactory.getLogger(EquipmentFittingDAO.class);

    protected void initDao() {
    }

    public void save(EquipmentFitting transientInstance) {
        log.debug("saving EquipmentFitting instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(EquipmentFitting persistentInstance) {
        log.debug("deleting EquipmentFitting instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public EquipmentFitting findById(com.narirelays.ems.persistence.orm.EquipmentFittingId id) {
        log.debug("getting EquipmentFitting instance with id: " + id);
        try {
            EquipmentFitting instance = (EquipmentFitting) getHibernateTemplate().get("com.narirelays.ems.persistence.orm.EquipmentFitting", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(EquipmentFitting instance) {
        log.debug("finding EquipmentFitting instance by example");
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
        log.debug("finding EquipmentFitting instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from EquipmentFitting as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findAll() {
        log.debug("finding all EquipmentFitting instances");
        try {
            String queryString = "from EquipmentFitting";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public EquipmentFitting merge(EquipmentFitting detachedInstance) {
        log.debug("merging EquipmentFitting instance");
        try {
            EquipmentFitting result = (EquipmentFitting) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(EquipmentFitting instance) {
        log.debug("attaching dirty EquipmentFitting instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(EquipmentFitting instance) {
        log.debug("attaching clean EquipmentFitting instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static EquipmentFittingDAO getFromApplicationContext(ApplicationContext ctx) {
        return (EquipmentFittingDAO) ctx.getBean("EquipmentFittingDAO");
    }
}
