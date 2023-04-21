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
 *
 * @author Lorin Metzger
 *
 */
public class ActualFileSelect {

    private static final Log LOG = LogFactory.getLog(ActualFileSelect.class);

    private static SessionFactory session_factory = null;

    private static synchronized SessionFactory getSessionFactory() {
        if (session_factory == null) {
            try {
                Configuration config = new Configuration();
                config.setProperties(DatabaseInitializer.getHibernateProperties());
                config.addClass(ActualFile.class);
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
   * Insert a File object into the database
   *
   */
    public static void insert(ActualFile af, Session session) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(af);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.trace("Unable to save actual file data ", e);
        }
    }

    /**
   *
   * Remove the given Image instance from the database.
   *
   * @param Image image - The image object to be removed from the db.
   * @param Session session - the database connection session.
   *
   */
    public static void delete(ActualFile af, Session session) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(af);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.trace("Unable to delete actual file data ", e);
        }
    }

    /**
   *
   * Retrieve the Image object from the database with the given file id.
   *
   * @param long id - the id of the file object this image object is part of.
   *
   */
    public static ActualFile getByID(long id) {
        ActualFile result = null;
        Session session = null;
        try {
            session = getSession();
            Criteria criteria = session.createCriteria(ActualFile.class);
            criteria.add(Expression.eq("id", new Long(id)));
            result = (ActualFile) criteria.uniqueResult();
        } catch (Exception e) {
            LOG.trace("Unable to perform query get image by file id ", e);
        }
        closeSession(session);
        return result;
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
