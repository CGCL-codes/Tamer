package ocumed.persistenz.hibernate;

import java.util.List;
import ocumed.persistenz.dao.PatientDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 * Home object for domain model class OcPatient.
 * @see ocumed.persistenz.hibernate.OcPatient
 * @author Hibernate Tools
 */
public class OcPatientHome implements PatientDAO {

    private static final Log log = LogFactory.getLog(OcPatientHome.class);

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void attachDirty(OcPatient instance) {
        log.debug("attaching dirty OcPatient instance");
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

    public void attachClean(OcPatient instance) {
        log.debug("attaching clean OcPatient instance");
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

    public void delete(OcPatient persistentInstance) {
        log.debug("deleting OcPatient instance");
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

    public OcPatient merge(OcPatient detachedInstance) {
        log.debug("merging OcPatient instance");
        try {
            OcPatient result = (OcPatient) sessionFactory.getCurrentSession().merge(detachedInstance);
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

    public OcPatient findById(int id) {
        log.debug("getting OcPatient instance with id: " + id);
        try {
            OcPatient instance = (OcPatient) sessionFactory.getCurrentSession().get("ocumed.persistenz.hibernate.OcPatient", id);
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
     * find all OcPatient instances
     */
    @SuppressWarnings("unchecked")
    public List<OcPatient> findAll() {
        log.debug("finding all OcPatient instances ");
        try {
            List<OcPatient> results = sessionFactory.getCurrentSession().createCriteria(OcPatient.class).list();
            log.debug("finding all OcPatient successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("finding all OcPatient failed", re);
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
    public Integer persist(OcPatient transientInstance) {
        log.debug("persisting OcPatient instance");
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

    /**
     * find all patients by name, case-insensitive
     * 
     * @param string
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<OcPatient> findByName(String criteria) {
        log.debug("finding OcPatient by name: " + criteria);
        try {
            List<OcPatient> results = sessionFactory.getCurrentSession().createCriteria(OcPatient.class).add(Restrictions.or(Restrictions.ilike("patientvorname", "%" + criteria + "%"), Restrictions.ilike("patientnachname", "%" + criteria + "%"))).list();
            log.debug("finding OcPatient by name successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("finding OcPatient by name failed", re);
            throw re;
        }
    }

    /**
     * find a specific patient by svn
     */
    @SuppressWarnings("unchecked")
    public OcPatient findByVersNr(String versNr) {
        log.debug("finding OcPatient by versNr: " + versNr);
        try {
            List<OcPatient> results = sessionFactory.getCurrentSession().createCriteria(OcPatient.class).add(Restrictions.eq("patientsvn", versNr)).list();
            log.debug("finding OcPatient by versNr successful, result size: " + results.size());
            if (results.size() > 0) {
                if (results.size() > 1) {
                    log.warn("warning! multiple patients have the same svn");
                }
                return results.get(0);
            } else {
                return null;
            }
        } catch (RuntimeException re) {
            log.error("finding filtered by versNr OcPatient failed", re);
            throw re;
        }
    }
}
