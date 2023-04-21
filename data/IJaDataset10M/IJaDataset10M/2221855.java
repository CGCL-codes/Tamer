package edu.fullcoll.uportal.portlets.earlyalert.dao;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.fullcoll.uportal.portlets.earlyalert.model.alert.Alerts;
import edu.fullcoll.uportal.portlets.earlyalert.services.HibernateUtil;

/**
 * Home object for domain model class Alerts.
 * @see edu.fullcoll.uportal.portlets.earlyalert.model.Alerts
 * @author Brad Rippe (brippe@fullcoll.edu)
 * @since 1.0
 */
public class AlertsHome {

    private static final Log log = LogFactory.getLog(AlertsHome.class);

    /**
     * Retrieve an Alert by it's primary id
     * @param id the primary id
     * @return a Alert or null
     */
    public Alerts findById(int id) {
        Session hSession = null;
        Transaction nTransaction = null;
        log.debug("getting Alerts instance with id: " + id);
        try {
            hSession = HibernateUtil.getBannerSessionFactory().openSession();
            nTransaction = hSession.beginTransaction();
            Alerts instance = (Alerts) hSession.get("edu.fullcoll.uportal.portlets.earlyalert.model.alert.Alerts", new Integer(id));
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

    /**
	 * Retrieve an alert by it's alert code
	 * @param alertCode the code that is associated with the alert to be retrieved
	 * @return the alert associated with the code otherwise null 
	 */
    public Alerts getAlertByCode(String alertCode) {
        Session hSession = null;
        Transaction nTransaction = null;
        try {
            hSession = HibernateUtil.getBannerSessionFactory().openSession();
            nTransaction = hSession.beginTransaction();
            StringBuffer qry = new StringBuffer();
            qry.append("from Alerts as a where a.alertCode = ?");
            Query q = hSession.createQuery(qry.toString());
            q.setString(0, alertCode);
            Iterator<Alerts> alertItr = q.list().iterator();
            if (alertItr.hasNext()) return (Alerts) alertItr.next(); else return null;
        } catch (Exception e) {
            if (nTransaction != null) nTransaction.rollback();
            log.error("Problem retrieving alerts by alert code", e);
        } finally {
            if (hSession != null) hSession.close();
        }
        return null;
    }

    /**
	 * Retrieve an alert by it's alert code
	 * @param alertCode the code that is associated with the alert to be retrieved
	 * @return the alert associated with the code otherwise null 
	 */
    public Alerts getAlertByCampusAndCode(String alertCampusCode, String alertCode) {
        Session hSession = null;
        Transaction nTransaction = null;
        try {
            hSession = HibernateUtil.getBannerSessionFactory().openSession();
            nTransaction = hSession.beginTransaction();
            StringBuffer qry = new StringBuffer();
            qry.append("from Alerts as a where a.alertCode = ? " + "and a.alertCampusCode = ?");
            Query q = hSession.createQuery(qry.toString());
            q.setString(0, alertCode);
            if (alertCampusCode.length() > 1) alertCampusCode = alertCampusCode.substring(0, 1);
            q.setString(1, alertCampusCode);
            Iterator<Alerts> alertItr = q.list().iterator();
            if (alertItr.hasNext()) return (Alerts) alertItr.next(); else return null;
        } catch (Exception e) {
            if (nTransaction != null) nTransaction.rollback();
            log.error("Problem retrieving alerts by alert campus and code", e);
        } finally {
            if (hSession != null) hSession.close();
        }
        return null;
    }

    /**
	 * Retrieve an alerts by campus code. 1 for Cypress College, 2 for Fullerton, and 3
	 * for School of Continuing Education.
	 * @param alertCode the code that is associated with the alert to be retrieved
	 * @return the alert associated with the code otherwise null 
	 */
    public List<Alerts> getAlertsByCampusCode(String alertCampusCode) {
        Session hSession = null;
        Transaction nTransaction = null;
        try {
            hSession = HibernateUtil.getBannerSessionFactory().openSession();
            nTransaction = hSession.beginTransaction();
            StringBuffer qry = new StringBuffer();
            qry.append("from Alerts as a where a.alertCampusCode = ?");
            Query q = hSession.createQuery(qry.toString());
            if (alertCampusCode.length() > 1) alertCampusCode = alertCampusCode.substring(0, 1);
            q.setString(0, alertCampusCode);
            return q.list();
        } catch (Exception e) {
            if (nTransaction != null) nTransaction.rollback();
            log.error("Problem retrieving alerts by alert campus code", e);
        } finally {
            if (hSession != null) hSession.close();
        }
        return null;
    }

    /**
     * List of all the alerts in the system that can be assigned to a student.
     * @return list of the alerts in the system.
     */
    public List<Alerts> getAllAlerts() {
        Session hSession = null;
        Transaction nTransaction = null;
        try {
            hSession = HibernateUtil.getBannerSessionFactory().openSession();
            nTransaction = hSession.beginTransaction();
            StringBuffer qry = new StringBuffer();
            qry.append("select distinct a from Alerts a " + "order by a.alerttypes, a.alertName");
            Query q = hSession.createQuery(qry.toString());
            return q.list();
        } catch (Exception e) {
            if (nTransaction != null) nTransaction.rollback();
            log.error("Problem retrieving all alerts", e);
        } finally {
            if (hSession != null) hSession.close();
        }
        return null;
    }
}
