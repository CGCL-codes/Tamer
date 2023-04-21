package org.actioncenters.udm.data;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for Workspaceuserrolehistory entities.
 * Transaction control of the save(), update() and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring transactions. Each of these methods provides
 * additional information for how to configure it for the desired type of transaction control.
 * 
 * @see org.actioncenters.udm.data.Workspaceuserrolehistory
 * @author MyEclipse Persistence Tools
 */
public class WorkspaceuserrolehistoryDAO extends HibernateDaoSupport {

    /** The Constant LOG. */
    private static final Log LOG = LogFactory.getLog(WorkspaceuserrolehistoryDAO.class);

    /** The Constant ID. */
    public static final String ID = "id";

    /** The Constant ROLE. */
    public static final String ROLE = "role";

    /** The Constant USER. */
    public static final String USER = "user";

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
    public void save(Workspaceuserrolehistory transientInstance) {
        LOG.debug("saving Workspaceuserrolehistory instance");
        try {
            getHibernateTemplate().save(transientInstance);
            LOG.debug("save successful");
        } catch (RuntimeException re) {
            LOG.error("save failed", re);
            throw re;
        }
    }

    /**
     * Delete.
     * 
     * @param persistentInstance
     *            the persistent instance
     */
    public void delete(Workspaceuserrolehistory persistentInstance) {
        LOG.debug("deleting Workspaceuserrolehistory instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            LOG.debug("delete successful");
        } catch (RuntimeException re) {
            LOG.error("delete failed", re);
            throw re;
        }
    }

    /**
     * Find by id.
     * 
     * @param id
     *            the id
     * 
     * @return the workspaceuserrolehistory
     */
    public Workspaceuserrolehistory findById(java.lang.Long id) {
        LOG.debug("getting Workspaceuserrolehistory instance with id: " + id);
        try {
            Workspaceuserrolehistory instance = (Workspaceuserrolehistory) getHibernateTemplate().get("org.actioncenters.udm.data.Workspaceuserrolehistory", id);
            return instance;
        } catch (RuntimeException re) {
            LOG.error("get failed", re);
            throw re;
        }
    }

    /**
     * Find by example.
     * 
     * @param instance
     *            the instance
     * 
     * @return the list< workspaceuserrolehistory>
     */
    @SuppressWarnings("unchecked")
    public List<Workspaceuserrolehistory> findByExample(Workspaceuserrolehistory instance) {
        LOG.debug("finding Workspaceuserrolehistory instance by example");
        try {
            List<Workspaceuserrolehistory> results = (List<Workspaceuserrolehistory>) getHibernateTemplate().findByExample(instance);
            LOG.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            LOG.error("find by example failed", re);
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
    public List<Workspaceuserrolehistory> findByProperty(String propertyName, Object value) {
        LOG.debug("finding Workspaceuserrolehistory instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Workspaceuserrolehistory as model where model." + propertyName + "= ?";
            return (List<Workspaceuserrolehistory>) getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            LOG.error("find by property name failed", re);
            throw re;
        }
    }

    /**
     * Find by id.
     * 
     * @param id
     *            the id
     * 
     * @return the list< workspaceuserrolehistory>
     */
    public List<Workspaceuserrolehistory> findById(Object id) {
        return findByProperty(ID, id);
    }

    /**
     * Find by role.
     * 
     * @param role
     *            the role
     * 
     * @return the list< workspaceuserrolehistory>
     */
    public List<Workspaceuserrolehistory> findByRole(Object role) {
        return findByProperty(ROLE, role);
    }

    /**
     * Find by user.
     * 
     * @param user
     *            the user
     * 
     * @return the list< workspaceuserrolehistory>
     */
    public List<Workspaceuserrolehistory> findByUser(Object user) {
        return findByProperty(USER, user);
    }

    /**
     * Find all.
     * 
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<Workspaceuserrolehistory> findAll() {
        LOG.debug("finding all Workspaceuserrolehistory instances");
        try {
            String queryString = "from Workspaceuserrolehistory";
            return (List<Workspaceuserrolehistory>) getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            LOG.error("find all failed", re);
            throw re;
        }
    }

    /**
     * Merge.
     * 
     * @param detachedInstance
     *            the detached instance
     * 
     * @return the workspaceuserrolehistory
     */
    public Workspaceuserrolehistory merge(Workspaceuserrolehistory detachedInstance) {
        LOG.debug("merging Workspaceuserrolehistory instance");
        try {
            Workspaceuserrolehistory result = (Workspaceuserrolehistory) getHibernateTemplate().merge(detachedInstance);
            LOG.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            LOG.error("merge failed", re);
            throw re;
        }
    }

    /**
     * Attach dirty.
     * 
     * @param instance
     *            the instance
     */
    public void attachDirty(Workspaceuserrolehistory instance) {
        LOG.debug("attaching dirty Workspaceuserrolehistory instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            LOG.debug("attach successful");
        } catch (RuntimeException re) {
            LOG.error("attach failed", re);
            throw re;
        }
    }

    /**
     * Attach clean.
     * 
     * @param instance
     *            the instance
     */
    public void attachClean(Workspaceuserrolehistory instance) {
        LOG.debug("attaching clean Workspaceuserrolehistory instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            LOG.debug("attach successful");
        } catch (RuntimeException re) {
            LOG.error("attach failed", re);
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
    public static WorkspaceuserrolehistoryDAO getFromApplicationContext(ApplicationContext ctx) {
        return (WorkspaceuserrolehistoryDAO) ctx.getBean("WorkspaceuserrolehistoryDAO");
    }
}
