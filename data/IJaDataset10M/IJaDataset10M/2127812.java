package org.actioncenters.activitylog.db.data;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for Activity entities. Transaction control of the
 * save(), update() and delete() operations can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see org.actioncenters.activitylog.db.data.Activity
 * @author MyEclipse Persistence Tools
 */
public class ActivityDAO extends HibernateDaoSupport {

    /** The Constant log. */
    private static Log log = LogFactory.getLog(ActivityDAO.class);

    /** The Constant ACTIVITYDESCRIPTION. */
    public static final String ACTIVITYDESCRIPTION = "activitydescription";

    /** The Constant SUBSYSTEM. */
    public static final String SUBSYSTEM = "subsystem";

    /** The Constant USERID. */
    public static final String USERID = "userid";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initDao() {
    }

    /**
     * Save.
     * 
     * @param transientInstance
     *            the transient instance
     */
    public void save(Activity transientInstance) {
        log.debug("saving Activity instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    /**
     * Delete.
     * 
     * @param persistentInstance
     *            the persistent instance
     */
    public void delete(Activity persistentInstance) {
        log.debug("deleting Activity instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    /**
     * Find by id.
     * 
     * @param id
     *            the id
     * 
     * @return the activity
     */
    public Activity findById(java.lang.String id) {
        log.debug("getting Activity instance with id: " + id);
        try {
            Activity instance = (Activity) getHibernateTemplate().get("org.actioncenters.activitylog.db.data.Activity", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    /**
     * Find by example.
     * 
     * @param instance
     *            the instance
     * 
     * @return the list< activity>
     */
    @SuppressWarnings("unchecked")
    public List<Activity> findByExample(Activity instance) {
        log.debug("finding Activity instance by example");
        try {
            List<Activity> results = (List<Activity>) getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    /**
     * Find by property.
     * 
     * @param propertyName
     *            the property name
     * @param value
     *            the value
     * 
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<Activity> findByProperty(String propertyName, Object value) {
        log.debug("finding Activity instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Activity as model where model." + propertyName + "= ?";
            return (List<Activity>) getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    /**
     * Find by activitydescription.
     * 
     * @param activitydescription
     *            the activitydescription
     * 
     * @return the list< activity>
     */
    public List<Activity> findByActivitydescription(Object activitydescription) {
        return findByProperty(ACTIVITYDESCRIPTION, activitydescription);
    }

    /**
     * Find by subsystem.
     * 
     * @param subsystem
     *            the subsystem
     * 
     * @return the list< activity>
     */
    public List<Activity> findBySubsystem(Object subsystem) {
        return findByProperty(SUBSYSTEM, subsystem);
    }

    /**
     * Find by userid.
     * 
     * @param userid
     *            the userid
     * 
     * @return the list< activity>
     */
    public List<Activity> findByUserid(Object userid) {
        return findByProperty(USERID, userid);
    }

    /**
     * Find all.
     * 
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<Activity> findAll() {
        log.debug("finding all Activity instances");
        try {
            String queryString = "from Activity";
            return (List<Activity>) getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * Merge.
     * 
     * @param detachedInstance
     *            the detached instance
     * 
     * @return the activity
     */
    public Activity merge(Activity detachedInstance) {
        log.debug("merging Activity instance");
        try {
            Activity result = (Activity) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    /**
     * Attach dirty.
     * 
     * @param instance
     *            the instance
     */
    public void attachDirty(Activity instance) {
        log.debug("attaching dirty Activity instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /**
     * Attach clean.
     * 
     * @param instance
     *            the instance
     */
    public void attachClean(Activity instance) {
        log.debug("attaching clean Activity instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /**
     * Gets the from application context.
     * 
     * @param ctx
     *            the ctx
     * 
     * @return the from application context
     */
    public static ActivityDAO getFromApplicationContext(ApplicationContext ctx) {
        return (ActivityDAO) ctx.getBean("ActivityDAO");
    }
}
