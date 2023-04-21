package com.converter.dao;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.converter.TabLogin;
import com.converter.TabLoginId;

/**
 * A data access object (DAO) providing persistence and search support for
 * TabLogin entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.converter.TabLogin
 * @author MyEclipse Persistence Tools
 */
public class TabLoginDAO extends HibernateDaoSupport {

    private static final Logger log = LoggerFactory.getLogger(TabLoginDAO.class);

    public static final String PASSWORD = "password";

    public static final String DEPARTMENT = "department";

    public static final String ROLE = "role";

    public static final String NAME = "name";

    protected void initDao() {
    }

    public void save(TabLogin transientInstance) {
        log.debug("saving TabLogin instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(TabLogin persistentInstance) {
        log.debug("deleting TabLogin instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public TabLogin findById(com.converter.TabLoginId id) {
        log.debug("getting TabLogin instance with id: " + id);
        try {
            TabLogin instance = (TabLogin) getHibernateTemplate().get("com.converter.TabLogin", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(TabLogin instance) {
        log.debug("finding TabLogin instance by example");
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
        log.debug("finding TabLogin instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from TabLogin as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByPassword(Object password) {
        return findByProperty(PASSWORD, password);
    }

    public List findByDepartment(Object department) {
        return findByProperty(DEPARTMENT, department);
    }

    public List findByRole(Object role) {
        return findByProperty(ROLE, role);
    }

    public List findByName(Object name) {
        return findByProperty(NAME, name);
    }

    public List findAll() {
        log.debug("finding all TabLogin instances");
        try {
            String queryString = "from TabLogin";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public TabLogin merge(TabLogin detachedInstance) {
        log.debug("merging TabLogin instance");
        try {
            TabLogin result = (TabLogin) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TabLogin instance) {
        log.debug("attaching dirty TabLogin instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(TabLogin instance) {
        log.debug("attaching clean TabLogin instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static TabLoginDAO getFromApplicationContext(ApplicationContext ctx) {
        return (TabLoginDAO) ctx.getBean("TabLoginDAO");
    }
}
