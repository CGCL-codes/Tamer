package com.open_squad.openplan.model;

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class DetailPart.
 * @see com.open_squad.openplan.model.DetailPart
 * @author Hibernate Tools
 */
public class DetailPartHome {

    private static final Log log = LogFactory.getLog(DetailPartHome.class);

    private final SessionFactory sessionFactory = getSessionFactory();

    protected SessionFactory getSessionFactory() {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        } catch (Exception e) {
            log.error("Could not locate SessionFactory in JNDI", e);
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }

    public void persist(DetailPart transientInstance) {
        log.debug("persisting DetailPart instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(DetailPart instance) {
        log.debug("attaching dirty DetailPart instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(DetailPart instance) {
        log.debug("attaching clean DetailPart instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(DetailPart persistentInstance) {
        log.debug("deleting DetailPart instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public DetailPart merge(DetailPart detachedInstance) {
        log.debug("merging DetailPart instance");
        try {
            DetailPart result = (DetailPart) sessionFactory.getCurrentSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public DetailPart findById(int id) {
        log.debug("getting DetailPart instance with id: " + id);
        try {
            DetailPart instance = (DetailPart) sessionFactory.getCurrentSession().get("com.open_squad.openplan.model.DetailPart", id);
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

    public List<DetailPart> findByExample(DetailPart instance) {
        log.debug("finding DetailPart instance by example");
        try {
            List<DetailPart> results = (List<DetailPart>) sessionFactory.getCurrentSession().createCriteria("com.open_squad.openplan.model.DetailPart").add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
