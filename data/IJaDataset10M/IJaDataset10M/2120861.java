package ocumed.persistenz.hibernate;

import java.util.List;
import ocumed.persistenz.dao.ZeitbestaetigungDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class OcZeitbestaetigung.
 * @see ocumed.persistenz.hibernate.OcZeitbestaetigung
 * @author Hibernate Tools
 */
public class OcZeitbestaetigungHome implements ZeitbestaetigungDAO {

    private static final Log log = LogFactory.getLog(OcZeitbestaetigungHome.class);

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void attachDirty(OcZeitbestaetigung instance) {
        log.debug("attaching dirty OcZeitbestaetigung instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            Transaction tx = sessionFactory.getCurrentSession().getTransaction();
            if (tx != null) {
                log.error("executing rollback.");
                tx.rollback();
            }
            throw re;
        }
    }

    public void attachClean(OcZeitbestaetigung instance) {
        log.debug("attaching clean OcZeitbestaetigung instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            Transaction tx = sessionFactory.getCurrentSession().getTransaction();
            if (tx != null) {
                log.error("executing rollback.");
                tx.rollback();
            }
            throw re;
        }
    }

    public void delete(OcZeitbestaetigung persistentInstance) {
        log.debug("deleting OcZeitbestaetigung instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            Transaction tx = sessionFactory.getCurrentSession().getTransaction();
            if (tx != null) {
                log.error("executing rollback.");
                tx.rollback();
            }
            throw re;
        }
    }

    public OcZeitbestaetigung merge(OcZeitbestaetigung detachedInstance) {
        log.debug("merging OcZeitbestaetigung instance");
        try {
            OcZeitbestaetigung result = (OcZeitbestaetigung) sessionFactory.getCurrentSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            Transaction tx = sessionFactory.getCurrentSession().getTransaction();
            if (tx != null) {
                log.error("executing rollback.");
                tx.rollback();
            }
            throw re;
        }
    }

    public OcZeitbestaetigung findById(int id) {
        log.debug("getting OcZeitbestaetigung instance with id: " + id);
        try {
            OcZeitbestaetigung instance = (OcZeitbestaetigung) sessionFactory.getCurrentSession().get("ocumed.persistenz.hibernate.OcZeitbestaetigung", id);
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            Transaction tx = sessionFactory.getCurrentSession().getTransaction();
            if (tx != null) {
                log.error("executing rollback.");
                tx.rollback();
            }
            throw re;
        }
    }

    /** 
     * commit a transaction
     */
    public void commit() {
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    /**
     * rollback a transaction
     */
    public void rollback() {
        sessionFactory.getCurrentSession().getTransaction().rollback();
    }

    /** 
     * start a transaction
     */
    public void start() {
        sessionFactory.getCurrentSession().getTransaction().begin();
    }

    /**
     * find all OcZeitbestaetigung instances
     */
    @SuppressWarnings("unchecked")
    public List<OcZeitbestaetigung> findAll() {
        log.debug("finding all OcZeitbestaetigung instances ");
        try {
            List<OcZeitbestaetigung> results = sessionFactory.getCurrentSession().createCriteria(OcZeitbestaetigung.class).list();
            log.debug("finding all OcZeitbestaetigung successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("finding all OcZeitbestaetigung failed", re);
            Transaction tx = sessionFactory.getCurrentSession().getTransaction();
            if (tx != null) {
                log.error("executing rollback.");
                tx.rollback();
            }
            throw re;
        }
    }

    /**
     * persist a object, and return the generated id
     *
     * @param transientInstance
     * @return generated id 
     */
    public Integer persist(OcZeitbestaetigung transientInstance) {
        log.debug("persisting OcZeitbestaetigung instance");
        try {
            Integer ret = (Integer) sessionFactory.getCurrentSession().save(transientInstance);
            log.debug("persist successful");
            return ret;
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            Transaction tx = sessionFactory.getCurrentSession().getTransaction();
            if (tx != null) {
                log.error("executing rollback.");
                tx.rollback();
            }
            throw re;
        }
    }
}
