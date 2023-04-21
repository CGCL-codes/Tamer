package de.sreindl.amavisadmin.db;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * Data access object (DAO) for domain model class Request.
 * @see de.sreindl.amavisadmin.db.Request
 * @author MyEclipse - Hibernate Tools
 */
public class RequestDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(RequestDAO.class);

    public static final String HANDLER_CLASS = "handlerClass";

    public static final String STATUS = "status";

    public static final String PARAM1 = "param1";

    public static final String PARAM2 = "param2";

    public void save(Request transientInstance) {
        log.debug("saving Request instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(Request persistentInstance) {
        log.debug("deleting Request instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Request findById(String id) {
        log.debug("getting Request instance with id: " + id);
        try {
            Request instance = (Request) getSession().get("de.sreindl.amavisadmin.db.Request", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(Request instance) {
        log.debug("finding Request instance by example");
        try {
            List results = getSession().createCriteria("de.sreindl.amavisadmin.db.Request").add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding Request instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Request as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByHandlerClass(Object handlerClass) {
        return findByProperty(HANDLER_CLASS, handlerClass);
    }

    public List findByStatus(Object status) {
        return findByProperty(STATUS, status);
    }

    public List findByParam1(Object param1) {
        return findByProperty(PARAM1, param1);
    }

    public List findByParam2(Object param1) {
        return findByProperty(PARAM1, param1);
    }

    public Request merge(Request detachedInstance) {
        log.debug("merging Request instance");
        try {
            Request result = (Request) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Request instance) {
        log.debug("attaching dirty Request instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Request instance) {
        log.debug("attaching clean Request instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /** Request can be used */
    public static final Character STATUS_ACTIVE = 'A';

    /** Request has been processed. This entry will be removed by the cleanup job*/
    public static final Character STATUS_PROCESSED = 'P';

    /** Request has been processed with error. 
     * This entry has to be removed manually.
     *
     * @TODO: Maintenance page for error requests should be created
     */
    public static final Character STATUS_ERROR = 'E';

    /** Entry has been expired. This entry will be removed by the cleanup job. 
     */
    public static final Character STATUS_EXPIRED = 'X';
}
