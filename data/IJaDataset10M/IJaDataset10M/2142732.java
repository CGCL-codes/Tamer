package edu.psu.its.lionshare.database;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.Criteria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * This class contains methods for inserting new AccessControl objects into the
 * database as well as query the database for objects that match certain 
 * criteria.
 *
 * @author Lorin Metzger
 *
 */
public class AccessControlSelect {

    private static final Log LOG = LogFactory.getLog(AccessControlSelect.class);

    private static SessionFactory session_factory = null;

    private static synchronized SessionFactory getSessionFactory() {
        if (session_factory == null) {
            try {
                Configuration config = new Configuration();
                config.setProperties(DatabaseInitializer.getHibernateProperties());
                config.addClass(AccessControl.class);
                session_factory = config.buildSessionFactory();
            } catch (Exception e) {
            }
        }
        return session_factory;
    }

    /**
   *
   * Creates a new session with the database that can be used to do querying
   * as well as inserts and deletes.
   *
   * @return Session - a new session.
   *
   */
    public static Session getSession() throws Exception {
        Session session = getSessionFactory().openSession();
        return session;
    }

    /**
   *
   * Insert a AccessControl object into the database
   *
   * @param AccessControl ac - The AccessControl object instance to be inserted 
   *                           into db.
   * @param Session session - a hibernate connection session with db.
   *
   */
    public static void insert(AccessControl ac, Session session) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(ac);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.trace("Unable to save Access Control Object ", e);
        }
    }

    public static void update(AccessControl ac, Session session) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(ac);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.trace("Unable to update metadata data " + ac.getId(), e);
        }
    }

    /**
   *
   * Remove the given Access Control instance from the database.
   *
   * @param AccessControl ac - The ac object to be removed from the db.
   * @param Session session - the database connection session.
   *
   */
    public static void delete(AccessControl ac, Session session) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(ac);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.trace("Unable to delete AccessControl object", e);
        }
    }

    /**
   *
   * Retrieve the AccessControl object from the database with the given id.
   *
   * @param int id - the id of the object to be retrieved.
   * @param Session session - the database connection session.
   *
   * @return AccessControl - the object matching the given id, null if no 
   *                         matching results are found.
   */
    public static AccessControl getACByID(long id) {
        AccessControl result = null;
        Session session = null;
        try {
            session = getSession();
            Criteria criteria = session.createCriteria(AccessControl.class);
            criteria.add(Expression.eq("id", new Long(id)));
            result = (AccessControl) criteria.uniqueResult();
        } catch (Exception e) {
            LOG.trace("Unable to perform query get ac id ", e);
        }
        closeSession(session);
        return result;
    }

    public static List getAllACByFileUrn(String urn) {
        List results = null;
        Session session = null;
        try {
            session = getSession();
            Criteria criteria = session.createCriteria(AccessControl.class);
            criteria.add(Expression.eq("fileUrn", urn));
            results = criteria.list();
        } catch (Exception e) {
            LOG.trace("Unable to perform query get ac id ", e);
        }
        closeSession(session);
        return results;
    }

    public static AccessControl getACByFileUrn(String urn) {
        AccessControl result = null;
        Session session = null;
        try {
            session = getSession();
            Criteria criteria = session.createCriteria(AccessControl.class);
            criteria.add(Expression.eq("fileUrn", urn));
            result = (AccessControl) criteria.uniqueResult();
        } catch (Exception e) {
            LOG.trace("Unable to perform query get ac id ", e);
        }
        closeSession(session);
        return result;
    }

    public static AccessControl getByUrnAndUid(String urn, Long uid) {
        AccessControl result = null;
        Session session = null;
        try {
            session = getSession();
            Criteria criteria = session.createCriteria(AccessControl.class);
            criteria.add(Expression.eq("fileUrn", urn));
            criteria.add(Expression.eq("userid", uid));
            result = (AccessControl) criteria.uniqueResult();
        } catch (Exception e) {
            LOG.trace("Unable to perform query get ac id ", e);
        }
        closeSession(session);
        return result;
    }

    public static List getAllTemplates() {
        List result = null;
        Session session = null;
        try {
            session = getSession();
            Criteria criteria = session.createCriteria(AccessControl.class);
            criteria.add(Expression.isNull("fileUrn"));
            result = criteria.list();
        } catch (Exception e) {
            LOG.trace("Unable to perform query get all templates ", e);
        }
        closeSession(session);
        return result;
    }

    public static List getAllTargetPolicies() {
        List result = null;
        Session session = null;
        try {
            session = getSession();
            Criteria criteria = session.createCriteria(AccessControl.class);
            result = criteria.list();
        } catch (Exception e) {
            LOG.trace("Unable to perform query get all templates ", e);
        }
        closeSession(session);
        return result;
    }

    public static boolean hasTemplatePolicyForName(String name) {
        List result = null;
        Session session = null;
        try {
            session = getSession();
            Criteria criteria = session.createCriteria(AccessControl.class);
            criteria.add(Expression.eq("name", name));
            result = criteria.list();
        } catch (Exception e) {
            LOG.trace("Unable to perform query hasTemplatePolicyForName ", e);
        }
        closeSession(session);
        if (result != null) {
            if (result.size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
   *
   * Gets a list of all Access Control objects from the database that match 
   *
   * @return List - the list of Access Control objects.
   *
   */
    public static List getAll() {
        List results = null;
        Session session = null;
        try {
            session = getSession();
            Criteria criteria = session.createCriteria(AccessControl.class);
            results = criteria.list();
        } catch (Exception e) {
            LOG.trace("Unable to perform query get all ac's ", e);
        }
        closeSession(session);
        return results;
    }

    public static void closeSession(Session session) {
        try {
            if (session != null) {
                session.close();
            }
        } catch (Exception e) {
        }
    }
}
