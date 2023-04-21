package de.sreindl.amavisadmin.db;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * Data access object (DAO) for domain model class ConfigurationSetting.
 * @see de.sreindl.mailadmin.db.ConfigurationSetting
 * @author Stephen Reindl
 */
public class ConfigurationDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(ConfigurationDAO.class);

    public static final String VALUE = "value";

    public static final String JOB = "job";

    public void save(ConfigurationSetting transientInstance) {
        log.debug("saving ConfigurationSetting instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(ConfigurationSetting persistentInstance) {
        log.debug("deleting ConfigurationSetting instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public ConfigurationSetting findById(java.lang.String id) {
        log.debug("getting Configuration instance with id: " + id);
        try {
            ConfigurationSetting instance = (ConfigurationSetting) getSession().get("de.sreindl.mailadmin.db.Configuration", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(ConfigurationSetting instance) {
        log.debug("finding ConfigurationSetting instance by example");
        try {
            List results = getSession().createCriteria("de.sreindl.mailadmin.db.ConfigurationSetting").add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding ConfigurationSetting instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from ConfigurationSetting as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByValue(Object email) {
        return findByProperty(VALUE, email);
    }

    /**
     * Find configuration entries for a particular job
     *
     * #1638972
     */
    public List findJob(Object job) {
        return findByProperty(JOB, job);
    }

    public ConfigurationSetting merge(ConfigurationSetting detachedInstance) {
        log.debug("merging ConfigurationSetting instance");
        try {
            ConfigurationSetting result = (ConfigurationSetting) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(ConfigurationSetting instance) {
        log.debug("attaching dirty ConfigurationSetting instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(ConfigurationSetting instance) {
        log.debug("attaching clean ConfigurationSetting instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}
