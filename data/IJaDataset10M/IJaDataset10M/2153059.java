package edu.fullcoll.uportal.portlets.earlyalert.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.fullcoll.uportal.portlets.earlyalert.model.alert.AlertComments;
import edu.fullcoll.uportal.portlets.earlyalert.services.HibernateUtil;

/**
 * Home object for domain model class AlertComments.
 * @see edu.fullcoll.uportal.portlets.earlyalert.model.alert.AlertComments
 * @author Brad Rippe (brippe@fullcoll.edu
 * @since 1.0
 */
public class AlertCommentsHome {

    private static final Log log = LogFactory.getLog(AlertCommentsHome.class);

    /** 
	 * @see org.hibernate.Session#persist(Object) 
	 * @param transientInstance a transient instance to be made persistent 
	 */
    public void persist(AlertComments transientInstance) {
        Session hSession = null;
        Transaction nTransaction = null;
        log.debug("persisting AssignedAlerts instance");
        try {
            hSession = HibernateUtil.getBannerSessionFactory().openSession();
            nTransaction = hSession.beginTransaction();
            hSession.persist(transientInstance);
            nTransaction.commit();
            log.debug("persist successful");
        } catch (RuntimeException re) {
            if (nTransaction != null) nTransaction.rollback();
            log.error("persist failed", re);
            throw re;
        } finally {
            if (hSession != null) hSession.close();
        }
    }

    /**
     * @see org.hibernate.Session#save(Object)
     * @param instance a transient instance of a persistent class 
     */
    public void save(AlertComments instance) {
        Session hSession = null;
        Transaction nTransaction = null;
        log.debug("attaching dirty AlertComments instance");
        try {
            hSession = HibernateUtil.getBannerSessionFactory().openSession();
            nTransaction = hSession.beginTransaction();
            hSession.save(instance);
            nTransaction.commit();
            log.debug("attach successful");
        } catch (RuntimeException re) {
            if (nTransaction != null) nTransaction.rollback();
            log.error("save failed", re);
            throw re;
        } finally {
            if (hSession != null) hSession.close();
        }
    }

    /**
     * @see org.hibernate.Session#saveOrUpdate(Object)
     * @param instance a transient or detached instance containing new or updated state 
     */
    public void attachDirty(AlertComments instance) {
        Session hSession = null;
        Transaction nTransaction = null;
        log.debug("attaching dirty AlertComments instance");
        try {
            hSession = HibernateUtil.getBannerSessionFactory().openSession();
            nTransaction = hSession.beginTransaction();
            hSession.saveOrUpdate(instance);
            nTransaction.commit();
            log.debug("attach successful");
        } catch (RuntimeException re) {
            if (nTransaction != null) nTransaction.rollback();
            log.error("attachDirty saveOrUpdate failed", re);
            throw re;
        } finally {
            if (hSession != null) hSession.close();
        }
    }

    /**
     * @see org.hibernate.Session#delete(Object)
     * @param persistentInstance the instance to be removed 
     */
    public void delete(AlertComments persistentInstance) {
        Session hSession = null;
        Transaction nTransaction = null;
        log.debug("deleting AlertComments instance");
        try {
            hSession = HibernateUtil.getBannerSessionFactory().openSession();
            nTransaction = hSession.beginTransaction();
            hSession.delete(persistentInstance);
            nTransaction.commit();
            log.debug("delete successful");
        } catch (RuntimeException re) {
            if (nTransaction != null) nTransaction.rollback();
            log.error("delete failed", re);
            throw re;
        } finally {
            if (hSession != null) hSession.close();
        }
    }

    /**
     * Retrieve a AlertComments by it's primary id
     * @param id the primary id
     * @return a AlertComments or null
     */
    public AlertComments findById(int id) {
        Session hSession = null;
        Transaction nTransaction = null;
        log.debug("getting AlertComments instance with id: " + id);
        try {
            hSession = HibernateUtil.getBannerSessionFactory().openSession();
            nTransaction = hSession.beginTransaction();
            AlertComments instance = (AlertComments) hSession.get("edu.fullcoll.uportal.portlets.earlyalert.model.alert.AlertComments", new Integer(id));
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            if (nTransaction != null) nTransaction.rollback();
            log.error("get failed", re);
            throw re;
        } finally {
            if (hSession != null) hSession.close();
        }
    }
}
