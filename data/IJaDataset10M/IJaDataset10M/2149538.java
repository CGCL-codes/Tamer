package org.nilisoft.jftp4i;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import org.nilisoft.jftp4i.GenericServer;
import org.nilisoft.jftp4i.framework.ISession;
import org.nilisoft.jftp4i.framework.SessionContext;
import org.nilisoft.jftp4i.util.Utils;

/**
 * <p> This class is responsable to mantaim a user session established when
 * the server directs the system call. </p>
 *
 * <p> For each user their will be an user session defined in the request
 * object. An user session will last exactly the time that user is connected,
 * and will only be lost, when the user is disconnect to the server. </p>
 * 
 * <p> Created on August 31, 2004, 4:19 PM </p>
 *
 * @author  Jlopes
 */
public class UserSession extends GenericServer implements ISession {

    /** 
   * <p> Creates a new instance of UserSession </p>
   *
   * @param id  The ID to this user session
   */
    public UserSession(String id, UserBean user, SessionContext sessionCtx) {
        super(id);
        this.sessionCtx = sessionCtx;
        initiateSession(user);
    }

    /**
   * <p> Defines an attribute/object to be added in the user session linked 
   * with the id passed as parameter. </p>
   *
   * @param id  The attribute ID
   * @param attr  The attribute to be linked
   */
    public synchronized void setUserAttribute(String id, Object attr) {
        if (id != null && attr != null && isInitiated()) {
            UserSession.KeyValuePair keyValue = new UserSession.KeyValuePair(id, attr);
            this.attributes = (Vector) inner_hash.get(this.user);
            Utils.addElementOnPosition(keyValue, attributes);
            inner_hash.put(user, attributes);
        }
    }

    /**
   * <p> Verifys if the user session is initiated or not. </p>
   */
    public synchronized boolean isInitiated() {
        return this.initiated;
    }

    /**
   * <p> Initializes the init FLAG of this session. </p>
   */
    public synchronized void setInitiated() {
        this.initiated = true;
    }

    /**
   * <p> Creates a new user session with the user passed as parameter. </p>
   *
   * @param user  The user to create the session.
   */
    public synchronized void initiateSession(UserBean user) {
        this.setInitiated();
        this.user = user;
        if (this.attributes == null) this.attributes = new Vector();
        this.inner_hash.put(user, attributes);
        log.info("Session just created. User name: ".concat(user.getUserName()));
    }

    /**
   * <p> Destroys the current user session. </p>
   */
    public synchronized void destroySession() {
        inner_hash.remove(this.user);
        this.attributes.removeAllElements();
        this.initiated = false;
        log.info("Session destroyed. User name: ".concat(user.getUserName()));
    }

    /**
   * <p> Returns the attribute linked with the specified id passed as parameter. </p>
   *
   * @param id  The attribute id
   */
    public synchronized Object getUserAttribute(String id) {
        for (int n = 0; n < attributes.size() && isInitiated(); n++) {
            UserSession.KeyValuePair entry = (UserSession.KeyValuePair) attributes.elementAt(n);
            if (id.trim().equals(entry.getKey().trim())) return entry.getValue();
        }
        return null;
    }

    /**
   * <p> Returns the global session context. </p>
   */
    public SessionContext getSessionContext() {
        return this.sessionCtx;
    }

    /**
   * <p> Returns a list of attribute names defined inside this user session. </p>
   */
    public Enumeration getAttributeNames() {
        Vector list = new Vector();
        for (int n = 0; n < attributes.size(); n++) {
            UserSession.KeyValuePair entry = (UserSession.KeyValuePair) this.attributes.elementAt(n);
            list.addElement(entry.getKey().trim());
        }
        return new UserSession.JFTPEnumeration(list);
    }

    /**
   * <p> This is class is responsable to mantain the key and value defined
   * for an attribute. </p>
   *
   * @author  Jo�o Rios
   */
    protected static class KeyValuePair extends GenericServer {

        /**
     * <p> Default Constructor. </p>
     */
        KeyValuePair(String key, Object obj) {
            super("org.nilisoft.jftp4i.UserSession.KeyValue");
            this.key = key;
            this.value = obj;
        }

        /**
     * <p> Returns the attribute key. </p>
     */
        String getKey() {
            return this.key;
        }

        /**
     * <p> Returns the attribute value. </p>
     */
        Object getValue() {
            return this.value;
        }

        /**
     * <p> Verifys if it is the same object by the attribute key inside of
     * each object, the current object and the object passed as parameter. </p>
     *
     * @param obj The object to be compare
     */
        public boolean equals(Object obj) {
            if (!(obj instanceof UserSession.KeyValuePair)) return false; else return ((UserSession.KeyValuePair) obj).getKey().trim().equals(getKey().trim());
        }

        private String key;

        private Object value;
    }

    /**
   * <p> This class implements an enumeration algor�tm to enumerate all
   * the values used by the session object. </p>
   */
    protected static class JFTPEnumeration implements Enumeration {

        /**
     * <p> Default Constructor. </p>
     */
        public JFTPEnumeration(Vector list) {
            this.vector = list;
            this.count = 0;
        }

        /**
     * <p> Verifys if it has more elements. </p>
     */
        public boolean hasMoreElements() {
            return (vector.size() > count);
        }

        /**
     * <p> Returns the next Element in the list. </p>
     */
        public Object nextElement() {
            return vector.elementAt(count++);
        }

        private Vector vector;

        private int count;
    }

    /**
   * <p> Returns the linked user to this session context. </p>
   */
    public UserBean getLinkedUser() {
        return this.user;
    }

    private static Hashtable inner_hash = new Hashtable();

    private boolean initiated;

    private SessionContext sessionCtx;

    private UserBean user;

    private Vector attributes;
}
