package it.gestioneimmobili.hibernate.dao;

import it.gestioneimmobili.hibernate.bean.AnagVisionaImmobile;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * Data access object (DAO) for domain model class AnagVisionaImmobile.
 * 
 * @see it.gestioneimmobili.hibernate.bean.AnagVisionaImmobile
 * @author MyEclipse Persistence Tools
 */
public class AnagVisionaImmobileDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(AnagVisionaImmobileDAO.class);

    public static final String DATA_VISIONE = "dataVisione";

    public void save(AnagVisionaImmobile transientInstance) {
        log.debug("saving AnagVisionaImmobile instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void update(AnagVisionaImmobile transientInstance) {
        log.debug("saving AnagVisionaImmobile instance");
        try {
            getSession().update(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(AnagVisionaImmobile persistentInstance) {
        log.debug("deleting AnagVisionaImmobile instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public AnagVisionaImmobile findById(java.lang.Integer id) {
        log.debug("getting AnagVisionaImmobile instance with id: " + id);
        try {
            AnagVisionaImmobile instance = (AnagVisionaImmobile) getSession().get("it.gestioneimmobili.hibernate.bean.AnagVisionaImmobile", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(AnagVisionaImmobile instance) {
        log.debug("finding AnagVisionaImmobile instance by example");
        try {
            List results = getSession().createCriteria("it.gestioneimmobili.hibernate.bean.AnagVisionaImmobile").add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding AnagVisionaImmobile instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from AnagVisionaImmobile as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByDataVisione(Object dataVisione) {
        return findByProperty(DATA_VISIONE, dataVisione);
    }

    public List findAll() {
        log.debug("finding all AnagVisionaImmobile instances");
        try {
            String queryString = "from AnagVisionaImmobile";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public AnagVisionaImmobile merge(AnagVisionaImmobile detachedInstance) {
        log.debug("merging AnagVisionaImmobile instance");
        try {
            AnagVisionaImmobile result = (AnagVisionaImmobile) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(AnagVisionaImmobile instance) {
        log.debug("attaching dirty AnagVisionaImmobile instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(AnagVisionaImmobile instance) {
        log.debug("attaching clean AnagVisionaImmobile instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}
