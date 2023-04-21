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
 * This class contains methods for inserting new ChatHost objects into the
 * database as well as query the database for objects that match certain 
 * criteria.
 *
 * @author Lorin Metzger
 *
 */
public class ChatHostSelect {

    private static final Log LOG = LogFactory.getLog(ChatHostSelect.class);

    private static SessionFactory session_factory = null;

    private static synchronized SessionFactory getSessionFactory() {
        if (session_factory == null) {
            try {
                Configuration config = new Configuration();
                config.setProperties(DatabaseInitializer.getHibernateProperties());
                config.addClass(ChatHost.class);
                session_factory = config.buildSessionFactory();
            } catch (Exception e) {
                LOG.trace("Unable to create session factory", e);
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
   * Insert a PeerserverHost object into the database
   *
   * @param Peerserver ps -The peerserver object instance to be inserted into db
   * @param Session session - a hibernate connection session with db.
   *
   */
    public static void insert(ChatHost ch, Session session) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(ch);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.trace("Unable to save chat host data", e);
        }
    }

    public static void update(ChatHost host, Session session) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(host);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.trace("Unable to update ChatHost data ", e);
        }
    }

    /**
   *
   * Remove the given ChatHost instance from the database.
   *
   * @param ChatHost ch - The ChatHost object to be removed from the db.
   * @param Session session - the database connection session.
   *
   */
    public static void delete(ChatHost ch, Session session) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(ch);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.trace("Unable to delete chat host ", e);
        }
    }

    /**
   *
   * Retrieve the ChatHost object from the database with the given ps id.
   *
   * @param int id - the id of the object to be retrieved.
   * @param Session session - the database connection session.
   *
   * @return ChatHost - the object matching the given id, null if no 
   *                          matching results are found.
   */
    public static ChatHost getPeerserverByID(long id) {
        ChatHost result = null;
        Session session = null;
        try {
            session = getSession();
            Criteria criteria = session.createCriteria(ChatHost.class);
            criteria.add(Expression.eq("id", new Long(id)));
            result = (ChatHost) criteria.uniqueResult();
        } catch (Exception e) {
            LOG.trace("Unable to perform query get chat host by id ", e);
        }
        closeSession(session);
        return result;
    }

    public static List getChatHosts() {
        List results = null;
        Session session = null;
        try {
            session = getSession();
            Criteria criteria = session.createCriteria(ChatHost.class);
            results = criteria.list();
        } catch (Exception e) {
            LOG.trace("Unable to perform query get all chat hosts ", e);
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
