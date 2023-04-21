package eu.coaxion.gnumedj.postgre.v14;

import org.orm.*;
import org.hibernate.Query;
import java.util.List;

/**
 * messages in GNUmed relating to a patient, a provider, and a context
 */
public class Message_inbox {

    public Message_inbox() {
    }

    public static Message_inbox loadMessage_inboxByORMID(int pk) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadMessage_inboxByORMID(session, pk);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox getMessage_inboxByORMID(int pk) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return getMessage_inboxByORMID(session, pk);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox loadMessage_inboxByORMID(int pk, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadMessage_inboxByORMID(session, pk, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox getMessage_inboxByORMID(int pk, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return getMessage_inboxByORMID(session, pk, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox loadMessage_inboxByORMID(PersistentSession session, int pk) throws PersistentException {
        try {
            return (Message_inbox) session.load(eu.coaxion.gnumedj.postgre.v14.Message_inbox.class, new Integer(pk));
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox getMessage_inboxByORMID(PersistentSession session, int pk) throws PersistentException {
        try {
            return (Message_inbox) session.get(eu.coaxion.gnumedj.postgre.v14.Message_inbox.class, new Integer(pk));
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox loadMessage_inboxByORMID(PersistentSession session, int pk, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            return (Message_inbox) session.load(eu.coaxion.gnumedj.postgre.v14.Message_inbox.class, new Integer(pk), lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox getMessage_inboxByORMID(PersistentSession session, int pk, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            return (Message_inbox) session.get(eu.coaxion.gnumedj.postgre.v14.Message_inbox.class, new Integer(pk), lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox[] listMessage_inboxByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return listMessage_inboxByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox[] listMessage_inboxByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return listMessage_inboxByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox[] listMessage_inboxByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Message_inbox as Message_inbox");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            List list = query.list();
            return (Message_inbox[]) list.toArray(new Message_inbox[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox[] listMessage_inboxByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Message_inbox as Message_inbox");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            query.setLockMode("this", lockMode);
            List list = query.list();
            return (Message_inbox[]) list.toArray(new Message_inbox[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox loadMessage_inboxByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadMessage_inboxByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox loadMessage_inboxByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadMessage_inboxByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox loadMessage_inboxByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        Message_inbox[] message_inboxs = listMessage_inboxByQuery(session, condition, orderBy);
        if (message_inboxs != null && message_inboxs.length > 0) return message_inboxs[0]; else return null;
    }

    public static Message_inbox loadMessage_inboxByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        Message_inbox[] message_inboxs = listMessage_inboxByQuery(session, condition, orderBy, lockMode);
        if (message_inboxs != null && message_inboxs.length > 0) return message_inboxs[0]; else return null;
    }

    public static java.util.Iterator<Message_inbox> iterateMessage_inboxByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return iterateMessage_inboxByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator<Message_inbox> iterateMessage_inboxByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return iterateMessage_inboxByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator<Message_inbox> iterateMessage_inboxByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Message_inbox as Message_inbox");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            return query.iterate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator<Message_inbox> iterateMessage_inboxByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Message_inbox as Message_inbox");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            query.setLockMode("this", lockMode);
            return query.iterate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Message_inbox loadMessage_inboxByCriteria(Message_inboxCriteria message_inboxCriteria) {
        Message_inbox[] message_inboxs = listMessage_inboxByCriteria(message_inboxCriteria);
        if (message_inboxs == null || message_inboxs.length == 0) {
            return null;
        }
        return message_inboxs[0];
    }

    public static Message_inbox[] listMessage_inboxByCriteria(Message_inboxCriteria message_inboxCriteria) {
        return message_inboxCriteria.listMessage_inbox();
    }

    public static Message_inbox createMessage_inbox() {
        return new eu.coaxion.gnumedj.postgre.v14.Message_inbox();
    }

    public boolean save() throws PersistentException {
        try {
            eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().saveObject(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean delete() throws PersistentException {
        try {
            eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().deleteObject(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean refresh() throws PersistentException {
        try {
            eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession().refresh(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean evict() throws PersistentException {
        try {
            eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession().evict(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean deleteAndDissociate() throws PersistentException {
        try {
            if (getFk_staff() != null) {
                getFk_staff().getMessage_inbox().remove(this);
            }
            if (getFk_inbox_item_type() != null) {
                getFk_inbox_item_type().getMessage_inbox().remove(this);
            }
            if (getFk_patient() != null) {
                getFk_patient().getMessage_inbox().remove(this);
            }
            return delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean deleteAndDissociate(org.orm.PersistentSession session) throws PersistentException {
        try {
            if (getFk_staff() != null) {
                getFk_staff().getMessage_inbox().remove(this);
            }
            if (getFk_inbox_item_type() != null) {
                getFk_inbox_item_type().getMessage_inbox().remove(this);
            }
            if (getFk_patient() != null) {
                getFk_patient().getMessage_inbox().remove(this);
            }
            try {
                session.delete(this);
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    private int pk_audit;

    private int row_version;

    private java.sql.Timestamp modified_when;

    private String modified_by;

    private int pk;

    private eu.coaxion.gnumedj.postgre.v14.Staff fk_staff;

    private eu.coaxion.gnumedj.postgre.v14.Inbox_item_type fk_inbox_item_type;

    private String comment;

    private Integer ufk_context;

    private String data;

    private Short importance;

    private eu.coaxion.gnumedj.postgre.v14.Identity fk_patient;

    public void setPk_audit(int value) {
        this.pk_audit = value;
    }

    public int getPk_audit() {
        return pk_audit;
    }

    public void setRow_version(int value) {
        this.row_version = value;
    }

    public int getRow_version() {
        return row_version;
    }

    public void setModified_when(java.sql.Timestamp value) {
        this.modified_when = value;
    }

    public java.sql.Timestamp getModified_when() {
        return modified_when;
    }

    public void setModified_by(String value) {
        this.modified_by = value;
    }

    public String getModified_by() {
        return modified_by;
    }

    private void setPk(int value) {
        this.pk = value;
    }

    public int getPk() {
        return pk;
    }

    public int getORMID() {
        return getPk();
    }

    /**
	 * a free-text comment, may be NULL but not empty
	 */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
	 * a free-text comment, may be NULL but not empty
	 */
    public String getComment() {
        return comment;
    }

    /**
	 * an optional, *u*nchecked *f*oreign *k*ey, it is up to
	 * 	 the application to know what this points to, it will
	 * 	 have to make sense within the context of the combination
	 * 	 of staff ID, item type, and comment
	 */
    public void setUfk_context(int value) {
        setUfk_context(new Integer(value));
    }

    /**
	 * an optional, *u*nchecked *f*oreign *k*ey, it is up to
	 * 	 the application to know what this points to, it will
	 * 	 have to make sense within the context of the combination
	 * 	 of staff ID, item type, and comment
	 */
    public void setUfk_context(Integer value) {
        this.ufk_context = value;
    }

    /**
	 * an optional, *u*nchecked *f*oreign *k*ey, it is up to
	 * 	 the application to know what this points to, it will
	 * 	 have to make sense within the context of the combination
	 * 	 of staff ID, item type, and comment
	 */
    public Integer getUfk_context() {
        return ufk_context;
    }

    /**
	 * arbitrary data an application might wish to attach to
	 * 	 the message, like a cookie, basically
	 */
    public void setData(String value) {
        this.data = value;
    }

    /**
	 * arbitrary data an application might wish to attach to
	 * 	 the message, like a cookie, basically
	 */
    public String getData() {
        return data;
    }

    /**
	 * the relative importance of this message:
	 * 
	 * 	 -1: lower than most things already in the inbox ("low")
	 * 
	 * 	  0: same as most things ("standard")
	 * 
	 * 	  1: higher than most things already there ("high")
	 */
    public void setImportance(short value) {
        setImportance(new Short(value));
    }

    /**
	 * the relative importance of this message:
	 * 
	 * 	 -1: lower than most things already in the inbox ("low")
	 * 
	 * 	  0: same as most things ("standard")
	 * 
	 * 	  1: higher than most things already there ("high")
	 */
    public void setImportance(Short value) {
        this.importance = value;
    }

    /**
	 * the relative importance of this message:
	 * 
	 * 	 -1: lower than most things already in the inbox ("low")
	 * 
	 * 	  0: same as most things ("standard")
	 * 
	 * 	  1: higher than most things already there ("high")
	 */
    public Short getImportance() {
        return importance;
    }

    public void setFk_staff(eu.coaxion.gnumedj.postgre.v14.Staff value) {
        this.fk_staff = value;
    }

    public eu.coaxion.gnumedj.postgre.v14.Staff getFk_staff() {
        return fk_staff;
    }

    public void setFk_inbox_item_type(eu.coaxion.gnumedj.postgre.v14.Inbox_item_type value) {
        this.fk_inbox_item_type = value;
    }

    public eu.coaxion.gnumedj.postgre.v14.Inbox_item_type getFk_inbox_item_type() {
        return fk_inbox_item_type;
    }

    public void setFk_patient(eu.coaxion.gnumedj.postgre.v14.Identity value) {
        this.fk_patient = value;
    }

    public eu.coaxion.gnumedj.postgre.v14.Identity getFk_patient() {
        return fk_patient;
    }

    public String toString() {
        return String.valueOf(getPk());
    }
}
