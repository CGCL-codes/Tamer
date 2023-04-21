package net.sourceforge.gateway.sstp.document;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.gateway.sstp.databases.HibernateUtil;
import net.sourceforge.gateway.sstp.databases.tables.TransmissionDTO;
import net.sourceforge.gateway.sstp.utilities.Utils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate utility functions.
 */
public class DocumentManager {

    /**
	 * Random number generator. Used to randomize sleep() times in case of
	 * database contention.
	 */
    protected static final Random R = new Random();

    /**
	 * Logging Facility
	 */
    protected static final Logger LOG = Logger.getLogger("net.sourceforge.gateway");

    /**
	 * Save a message in the database with hibernate
	 * 
	 * @param msg
	 *            the message
	 * @return true if save went OK, false if there was a problem
	 */
    public boolean save(TransmissionDTO msg) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session sess = sessionFactory.openSession();
        Transaction tx = null;
        boolean success = false;
        if (sess != null) {
            try {
                tx = sess.beginTransaction();
                if (tx == null) {
                    throw new RuntimeException();
                }
                sess.save(msg);
                sess.flush();
                tx.commit();
                success = true;
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                LOG.debug("Could not save message.");
                LOG.error("Hibernate exception! Rolling back transaction.", e);
                success = false;
            } finally {
                if (sess.isOpen()) {
                    sess.close();
                }
            }
        } else {
            LOG.error("Could not create hibernate session!?!?");
            success = false;
        }
        return success;
    }

    /**
	 * Returns a count of all documents stored from a given transmitter
	 * 
	 * @return
	 */
    public long countDocumentsByTransmitter(String transmitter) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session sess = sessionFactory.openSession();
        List<Long> countList = null;
        Transaction tx = null;
        try {
            Query query = sess.createQuery("select count(*) from " + TransmissionDTO.class.getName() + " x  where x.transmitter = :var");
            query.setString("var", transmitter);
            tx = sess.beginTransaction();
            if (tx == null) {
                throw new RuntimeException();
            }
            countList = query.list();
            sess.flush();
            tx.commit();
        } catch (Exception x) {
        }
        if (countList != null) {
            return countList.get(0);
        } else {
            return 0;
        }
    }

    /**
	 * Returns a count of all documents stored from a given transmitter
	 * 
	 * @return
	 */
    public int countDocumentsByTransmitter(String transmitter, Date startDate, Date endDate) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session sess = sessionFactory.openSession();
        Random rand = new Random();
        int results = 0;
        Transaction tx = null;
        if (sess != null) {
            try {
                tx = sess.beginTransaction();
                if (tx == null) {
                    throw new RuntimeException();
                }
                Criteria crt = sess.createCriteria(TransmissionDTO.class);
                crt.setProjection(Projections.rowCount());
                Calendar cal = Calendar.getInstance();
                cal.setTime(endDate);
                crt.add(Restrictions.lt("timestamp", cal));
                cal.setTime(startDate);
                crt.add(Restrictions.gt("timestamp", cal));
                crt.add(Restrictions.eq("transmitter", transmitter));
                results = (Integer) crt.uniqueResult();
                sess.flush();
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                LOG.error("Hibernate exception! Rolling back transaction.", e);
                try {
                    Thread.yield();
                    Thread.sleep(Math.abs(rand.nextLong()) % 1024);
                } catch (Exception d) {
                    LOG.warn("Thread yield() and/or sleep failed", d);
                }
            } finally {
                if (sess.isOpen()) {
                    sess.close();
                }
            }
        } else {
            LOG.error("No Hibernate session.");
        }
        return results;
    }

    public List<TransmissionDTO> selectAllDocumentsLike(String transmissionid, String transmitter, String documentid, String documenttype, String schemaversion) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session sess = sessionFactory.openSession();
        List<TransmissionDTO> results = null;
        Transaction tx = null;
        if (sess != null) {
            try {
                LOG.debug("Begin Transaction");
                tx = sess.beginTransaction();
                if (tx == null) {
                    throw new RuntimeException();
                }
                Criteria criteria = sess.createCriteria(TransmissionDTO.class.getName());
                if (transmissionid != null && !transmissionid.equals("")) {
                    criteria.add(Restrictions.like("id", transmissionid));
                }
                if (transmitter != null && !transmitter.equals("")) {
                    criteria.add(Restrictions.like("transmitter", transmitter));
                }
                if (documentid != null && !documentid.equals("")) {
                    criteria.add(Restrictions.like("identity", documentid));
                }
                if (documenttype != null && !documenttype.equals("")) {
                    criteria.add(Restrictions.like("transmissionType", documenttype));
                }
                if (schemaversion != null && !schemaversion.equals("")) {
                    criteria.add(Restrictions.like("version", schemaversion));
                }
                results = criteria.list();
                LOG.debug("Number of Objects Returned: " + results.size());
                sess.flush();
                LOG.debug("Commit Transaction");
                tx.commit();
            } catch (RuntimeException e) {
                LOG.error("Hibernate exception! Rolling back transaction.");
                if (tx != null) {
                    tx.rollback();
                }
                LOG.error(e.toString());
            } finally {
                if (sess.isOpen()) {
                    sess.close();
                }
            }
        } else {
            LOG.error("No Hibernate session.");
        }
        return results;
    }

    /**
	 * Deletes Documents from the DB.
	 * 
	 * @param date
	 *            the Date we are querying with refence to
	 * @return the number of documents deleted.
	 */
    public int deleteAllDocumentsBefore(Date date) {
        int deleted = 0;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session sess = sessionFactory.openSession();
        List<TransmissionDTO> results = null;
        Transaction tx = null;
        if (sess != null) {
            try {
                LOG.debug("Begin Transaction");
                tx = sess.beginTransaction();
                if (tx == null) {
                    throw new RuntimeException();
                }
                Criteria criteria = sess.createCriteria(TransmissionDTO.class.getName());
                Timestamp tstamp = new Timestamp(date.getTime());
                criteria.add(Restrictions.le("timestamp", tstamp));
                results = criteria.list();
                LOG.debug("Number of Objects Returned: " + results.size());
                sess.flush();
                Iterator itr = results.iterator();
                while (itr.hasNext()) {
                    sess.delete(itr.next());
                    deleted++;
                }
                sess.flush();
                LOG.debug("Commit Transaction");
                tx.commit();
            } catch (RuntimeException e) {
                LOG.error("Hibernate exception! Rolling back transaction.");
                if (tx != null) {
                    tx.rollback();
                }
                LOG.error(e.toString());
            } finally {
                if (sess.isOpen()) {
                    sess.close();
                }
            }
        } else {
            LOG.error("No Hibernate session.");
        }
        return deleted;
    }

    /**
     * Initiates a download prompt on client's browser to obtain given data as
     * specified file
     * 
     * @param fc
     *        the current faces context
     * @param sendString
     *        the data to send
     * @param filename
     *        the name of the file that will be suggested in browser download
     * @param contentType
     *        the MIME type of file (text/xml etc)
     */
    public void sendFile2Client(FacesContext fc, String sendString, String filename, String contentType) {
        if (sendString != null && filename != null) {
            try {
                byte[] data = sendString.getBytes();
                HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
                response.setHeader("Content-disposition", "attachment; filename=" + filename);
                response.setContentLength(data.length);
                response.setContentType(contentType);
                ServletOutputStream out = response.getOutputStream();
                out.write(data);
                fc.responseComplete();
            } catch (Exception e) {
                LOG.error("Error sending file to browser: " + e);
            }
        } else {
            LOG.error("Message/Filename to send does not exist!");
        }
    }
}
