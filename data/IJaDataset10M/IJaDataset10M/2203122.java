package com.ecs.etrade.da;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for Cust
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.ecs.etrade.da.Cust
 * @author MyEclipse Persistence Tools
 */
public class CustDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(CustDAO.class);

    public static final String VERSION = "version";

    public static final String SALUTATION = "salutation";

    public static final String FIRST_NAME = "firstName";

    public static final String LAST_NAME = "lastName";

    public static final String MIDDLE_NAME = "middleName";

    public static final String GUARDIAN_NAME = "guardianName";

    public static final String REMISIER_BROKERAGE_PCT = "remisierBrokeragePct";

    public static final String REMISIER_MIN_BROKERAGE = "remisierMinBrokerage";

    protected void initDao() {
    }

    public void save(Cust transientInstance) {
        log.debug("saving Cust instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(Cust persistentInstance) {
        log.debug("deleting Cust instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Cust findById(java.lang.String id) {
        log.debug("getting Cust instance with id: " + id);
        try {
            Cust instance = (Cust) getHibernateTemplate().get("com.ecs.etrade.da.Cust", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(Cust instance) {
        log.debug("finding Cust instance by example");
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
        log.debug("finding Cust instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Cust as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByVersion(Object version) {
        return findByProperty(VERSION, version);
    }

    public List findBySalutation(Object salutation) {
        return findByProperty(SALUTATION, salutation);
    }

    public List findByFirstName(Object firstName) {
        return findByProperty(FIRST_NAME, firstName);
    }

    public List findByLastName(Object lastName) {
        return findByProperty(LAST_NAME, lastName);
    }

    public List findByMiddleName(Object middleName) {
        return findByProperty(MIDDLE_NAME, middleName);
    }

    public List findByGuardianName(Object guardianName) {
        return findByProperty(GUARDIAN_NAME, guardianName);
    }

    public List findByRemisierBrokeragePct(Object remisierBrokeragePct) {
        return findByProperty(REMISIER_BROKERAGE_PCT, remisierBrokeragePct);
    }

    public List findByRemisierMinBrokerage(Object remisierMinBrokerage) {
        return findByProperty(REMISIER_MIN_BROKERAGE, remisierMinBrokerage);
    }

    public List findAll() {
        log.debug("finding all Cust instances");
        try {
            String queryString = "from Cust";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public Cust merge(Cust detachedInstance) {
        log.debug("merging Cust instance");
        try {
            Cust result = (Cust) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Cust instance) {
        log.debug("attaching dirty Cust instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Cust instance) {
        log.debug("attaching clean Cust instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static CustDAO getFromApplicationContext(ApplicationContext ctx) {
        return (CustDAO) ctx.getBean("CustDAO");
    }
}
