package bgo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import bgo.utils.HibernateUtil;

/**
 * The class Mail holds the Information for an user-to-user-message.
 * 
 * @author Matthias Becker
 */
public class Mail implements Serializable {

    /** maked wicket and eclipse happy */
    private static final long serialVersionUID = 1L;

    /** message-id */
    private int mailID;

    /** holds the message */
    private String message;

    /** holds the topic */
    private String topic;

    /** sender id */
    private int fromId;

    /** receiver id */
    private int toId;

    /** sender name */
    private String fromName;

    /** receiver name */
    private String toName;

    /** was read? */
    private boolean wasRead;

    /** send-time */
    private Date sendTime;

    /**
	 * Deletes this Mail in db
	 * 
	 * @return true, if deleted
	 */
    public boolean delete() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.delete(this);
        session.getTransaction().commit();
        return true;
    }

    /**
	 * Marks this mail as read in databases
	 */
    public void markAsRead() {
        this.setWasRead(true);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.update(this);
        session.getTransaction().commit();
    }

    /**
	 * Returns all mails of Player p
	 * 
	 * @param p
	 *            Player who wants his mails
	 * @return List containing mail-objects
	 */
    public List getMails(Player p) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        List result = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Mail where TOID like :un order by SENDTIME DESC");
            q.setInteger("un", p.getPlayerId());
            result = q.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) {
                tx.rollback();
            }
            throw he;
        }
        return result;
    }

    /**
	 * Return the number of unread Mails for Player p
	 * 
	 * @param p
	 *            Player requesting his mail-count
	 * @return number of unread mails
	 */
    public int getNewMailCount(Player p) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        List result = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("select count (*) from Mail where TOID like :un and WASREAD = false");
            q.setInteger("un", p.getPlayerId());
            result = q.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) {
                tx.rollback();
            }
            throw he;
        }
        if (result.isEmpty()) {
            return 0;
        }
        return ((Long) result.get(0)).intValue();
    }

    /**
	 * sendMail sends a Mail to a user specified by toName
	 * 
	 * @param from
	 *            Player-Object describing the sender
	 * @param toName
	 *            Name of the receiver
	 * @return message indicating success by "Nachricht wurde gesendet" or an
	 *         error description
	 */
    public String send(Player from, String toName) {
        Player helper = new Player();
        if (!helper.existsuser(toName)) {
            return "Der Empfänger existiert nicht, bitte überprüfen!";
        }
        helper = helper.getPlayerByName(toName);
        this.setFromId(from.getPlayerId());
        this.setFromName(from.getName());
        this.setToId(helper.getPlayerId());
        this.setToName(helper.getName());
        this.setWasRead(false);
        this.setSendTime(new Date());
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(this);
        session.getTransaction().commit();
        return "Nachricht wurde gesendet";
    }

    /**
	 * @return the message
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * @param message
	 *            the message to set
	 */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
	 * @return the topic
	 */
    public String getTopic() {
        return topic;
    }

    /**
	 * @param topic
	 *            the topic to set
	 */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
	 * @return the from_id
	 */
    public int getFromId() {
        return fromId;
    }

    /**
	 * @param fromId
	 *            the fromId to set
	 */
    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    /**
	 * @return the from_name
	 */
    public String getFromName() {
        return fromName;
    }

    /**
	 * @param fromName
	 *            the fromName to set
	 */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /**
	 * @return the to_id
	 */
    public int getToId() {
        return toId;
    }

    /**
	 * @param toId
	 *            the toId to set
	 */
    public void setToId(int toId) {
        this.toId = toId;
    }

    /**
	 * @return the to_name
	 */
    public String getToName() {
        return toName;
    }

    /**
	 * @param toName
	 *            the toName to set
	 */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
	 * @return the sendTime
	 */
    public Date getSendTime() {
        return sendTime;
    }

    /**
	 * @param sendTime
	 *            the sendTime to set
	 */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
	 * @return the wasRead
	 */
    public boolean getWasRead() {
        return wasRead;
    }

    /**
	 * @param wasRead
	 *            the wasRead to set
	 */
    public void setWasRead(boolean wasRead) {
        this.wasRead = wasRead;
    }

    /**
	 * @return the mailID
	 */
    public int getMailID() {
        return mailID;
    }

    /**
	 * @param mailID
	 *            the mailID to set
	 */
    public void setMailID(int mailID) {
        this.mailID = mailID;
    }
}
