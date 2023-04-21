package bisnes.dao;

import hibernates.Zamowienia;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class Zamowienia.
 * @see bisnes.dao.Zamowienia
 * @author Hibernate Tools
 */
public class ZamowieniaDao extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(ZamowieniaDao.class);

    public void persist(Zamowienia transientInstance) {
        log.debug("persisting Zamowienia instance");
        try {
            getHibernateTemplate().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(Zamowienia instance) {
        log.debug("attaching dirty Zamowienia instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Zamowienia instance) {
        log.debug("attaching clean Zamowienia instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Zamowienia persistentInstance) {
        log.debug("deleting Zamowienia instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Zamowienia merge(Zamowienia detachedInstance) {
        log.debug("merging Zamowienia instance");
        try {
            Zamowienia result = (Zamowienia) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Zamowienia findById(long id) {
        log.debug("getting Zamowienia instance with id: " + id);
        try {
            Zamowienia instance = (Zamowienia) getHibernateTemplate().get("bisnes.dao.Zamowienia", id);
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Zamowienia> findByExample(Zamowienia instance) {
        log.debug("finding Zamowienia instance by example");
        try {
            getSession().createCriteria("LIMIT 10");
            List<Zamowienia> results = (List<Zamowienia>) getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
