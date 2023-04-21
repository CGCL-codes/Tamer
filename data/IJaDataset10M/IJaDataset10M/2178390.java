package edu.princeton.wordnet.pojos;

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class SemLink.
 * @see edu.princeton.wordnet.pojos.SemLink
 * @author Hibernate Tools
 */
public class SemLinkHome {

    private static final Log log = LogFactory.getLog(SemLinkHome.class);

    private final SessionFactory sessionFactory = getSessionFactory();

    protected SessionFactory getSessionFactory() {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        } catch (Exception e) {
            log.error("Could not locate SessionFactory in JNDI", e);
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }

    public void persist(SemLink transientInstance) {
        log.debug("persisting SemLink instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(SemLink instance) {
        log.debug("attaching dirty SemLink instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(SemLink instance) {
        log.debug("attaching clean SemLink instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(SemLink persistentInstance) {
        log.debug("deleting SemLink instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public SemLink merge(SemLink detachedInstance) {
        log.debug("merging SemLink instance");
        try {
            SemLink result = (SemLink) sessionFactory.getCurrentSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public SemLink findById(edu.princeton.wordnet.pojos.SemLinkId id) {
        log.debug("getting SemLink instance with id: " + id);
        try {
            SemLink instance = (SemLink) sessionFactory.getCurrentSession().get("edu.princeton.wordnet.pojos.SemLink", id);
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

    public List findByExample(SemLink instance) {
        log.debug("finding SemLink instance by example");
        try {
            List results = sessionFactory.getCurrentSession().createCriteria("edu.princeton.wordnet.pojos.SemLink").add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
