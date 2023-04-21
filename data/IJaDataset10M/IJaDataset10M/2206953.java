package com.jvantage.ce.session;

import com.jvantage.ce.common.Constants;
import com.jvantage.ce.logging.LogConstants;
import com.jvantage.ce.logging.LogHelper;
import org.apache.commons.lang.*;
import org.apache.log4j.*;
import java.io.*;
import java.util.*;

/**
 * @author  Brent Clay
 */
public class BaseSession implements Serializable {

    private static Logger logger = null;

    static {
        if (logger == null) {
            logger = Logger.getLogger(LogConstants.sfLoggerName_SessionStore);
        }
    }

    private Vector checkoutTable = new Vector();

    private Calendar creationTimeStamp;

    private int expungeExpiredObjectsIntervalInMinutes = 3;

    private long lastExpunged = System.currentTimeMillis();

    private Calendar lastTouchTimeStamp;

    private Hashtable sessionDataHashtable;

    private SessionID sessionID;

    private int timeToLive = 30;

    public BaseSession(SessionID sessionID) {
        sessionDataHashtable = new Hashtable();
        creationTimeStamp = Calendar.getInstance();
        setSessionID(sessionID);
        touch();
    }

    /**
     * Checks a previously checked-out object back in.
     *
     * @throws SessionException if the key was not checked out.
     */
    public void checkin(SessionKey key, Object value) throws SessionException {
        touch();
        if (checkoutTable.contains(key.toString())) {
            checkoutTable.remove(key.toString());
            sessionDataHashtable.put(key.toString(), value);
        } else {
            throw new SessionException(key + " was not checked out first.", SessionException.NOT_CHECKEDOUT);
        }
    }

    /**
     * Checks out an object from <code>this</code> session, thereby locking
     * it.
     */
    public Object checkout(SessionKey key) throws SessionException {
        Object o = null;
        touch();
        if (!checkoutTable.contains(key.toString())) {
            checkoutTable.addElement(key.toString());
            o = sessionDataHashtable.get(key.toString());
        } else {
            throw new SessionException(key + " is already checked out.", SessionException.ALREADY_CHECKEDOUT);
        }
        return o;
    }

    /**
     *  Returns true if the argument key is in <code>this</code> session.
     */
    public boolean containsKey(SessionKey key) {
        if (key == null) {
            return false;
        }
        return sessionDataHashtable.containsKey(key.toString());
    }

    /**
     *  Iterates over the contents of 'this' session and expunges expired
     *  objects.  That is, objects that have been unused for at least the amount
     *  of time specified in the SessionValue.expiresInMinutes value.  Note
     *  that only instances of the SessionValue wrapper class can expire.
     */
    public int expungeExpiredObjects() {
        if ((System.currentTimeMillis() - lastExpunged) < (expungeExpiredObjectsIntervalInMinutes * Constants.sfOneMinuteInMilliseconds)) {
            return 0;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(LogHelper.msg(sessionID, "Expunging expired values.  Expunge interval is [" + expungeExpiredObjectsIntervalInMinutes + "] minutes."));
        }
        SessionKey[] keys = new SessionKey[sessionDataHashtable.size()];
        SessionValue sessionValue = null;
        Set keySet = sessionDataHashtable.keySet();
        int i = 0;
        Enumeration keyEn = sessionDataHashtable.keys();
        while (keyEn.hasMoreElements()) {
            String keyValue = keyEn.nextElement().toString();
            if (keyValue != null) {
                Object obj = sessionDataHashtable.get(keyValue);
                if (obj != null) {
                    if (obj instanceof SessionValue) {
                        sessionValue = (SessionValue) obj;
                        if (sessionValue.isExpired()) {
                            if (logger.isDebugEnabled()) {
                                logger.debug(LogHelper.msg(sessionID, Constants.sfIndent + "SessionValue is expired - expunging [" + keyValue + "]."));
                            }
                            i++;
                            sessionDataHashtable.remove(keyValue);
                        }
                    }
                }
            }
        }
        lastExpunged = System.currentTimeMillis();
        return i;
    }

    /**
     *  Returns the time that this session was created.
     */
    public Calendar getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public int getIdleTime() {
        long diff = 0;
        Calendar now = Calendar.getInstance();
        if (getLastTouchTimeStamp().before(now)) {
            diff = now.getTime().getTime() - getLastTouchTimeStamp().getTime().getTime();
        }
        diff = diff / (1000 * 60);
        return (int) diff;
    }

    /**
     *  Returns the number of key/value pairs are in the session.
     */
    public int getKeyCount() {
        return getKeyCount(true);
    }

    /**
     *  Returns the number of key/value pairs are in the session, and updates
     *  the session last touched time.
     */
    public int getKeyCount(boolean updateTimeStamp) {
        if (updateTimeStamp) {
            touch();
        }
        return sessionDataHashtable.size();
    }

    /**
     *  Returns the array of keys in <code>this</code> session.
     */
    public SessionKey[] getKeys() throws SessionException {
        return getKeys(true);
    }

    /**
     *  Returns the array of keys in <code>this</code> session.
     *
     *  @param boolean updateTimeStamp -- true if the session's last time touched value
     *                                    should be updated.
     */
    public SessionKey[] getKeys(boolean updateTimeStamp) throws SessionException {
        SessionKey[] keys = new SessionKey[sessionDataHashtable.size()];
        if (updateTimeStamp) {
            touch();
        }
        Enumeration keyEn = sessionDataHashtable.keys();
        if (keys == null) {
            throw new SessionException("Unable to fetch keys from sessionDataHashtable.");
        }
        int i = 0;
        while (keyEn.hasMoreElements()) {
            String keyValue = keyEn.nextElement().toString();
            keys[i++] = new SessionKey(keyValue);
        }
        return keys;
    }

    /**
     *  Returns the last time this session was <code>touched</code>.
     */
    public Calendar getLastTouchTimeStamp() {
        return lastTouchTimeStamp;
    }

    /**
     * Returns the SessionID of <code>this</code> session.
     */
    public SessionID getSessionID() {
        return sessionID;
    }

    /**
     * Returns the number of minutes this session has before it expires.
     */
    public int getTimeToLive() {
        return timeToLive;
    }

    /**
     *  Returns the value associated with they key.
     */
    public Serializable getValue(SessionKey key) {
        return getValue(key, true);
    }

    /**
     *  Returns the value associated with the key.  If updateTimeStamp is false,
     *  the value is returned without updating the Session's timestamp, meaning
     *  the time to live for this session is not updated.
     *
     *  @param String key -- The key.
     *  @param boolean updateTimeStamp
     */
    public Serializable getValue(SessionKey key, boolean updateTimeStamp) {
        if (updateTimeStamp) {
            touch();
        }
        expungeExpiredObjects();
        return (Serializable) sessionDataHashtable.get(key.toString());
    }

    /**
     *  Returns the size in bytes of the objects associated with the
     *  argument key.  Returns -1 if the key cannot be found.
     */
    public long getValueSizeInBytes(SessionKey key) {
        Serializable value = getValue(key, false);
        if (value == null) {
            return -1L;
        }
        byte[] byteArray = SerializationUtils.serialize(value);
        if (byteArray == null) {
            return -1;
        }
        return byteArray.length;
    }

    /**
     * Returns true if this session has been idle for longer than the
     * getTimeToLive() value.
     */
    public boolean isExpired() {
        if (SessionID.SYSTEM_SESSION_ID.equals(this.getSessionID())) {
            return false;
        }
        return getIdleTime() > getTimeToLive();
    }

    /**
     *  Adds a value to the session.
     *
     *  key = value
     */
    public void putValue(SessionKey key, Object value) throws SessionException {
        touch();
        if (checkoutTable.contains(key.toString())) {
            throw new SessionException(key + " is checked out.", SessionException.ALREADY_CHECKEDOUT);
        } else {
            sessionDataHashtable.put(key.toString(), value);
        }
    }

    /**
     *  Removes a key/value pair from the session.
     */
    public Serializable removeValue(SessionKey key) {
        return (Serializable) sessionDataHashtable.remove(key.toString());
    }

    /**
     * Sets the SessionID of <code>this</code> session.
     */
    public void setSessionID(SessionID sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * Sets the number of minutes that a session can remain idle before
     * expiring.
     */
    public void setTimeToLive(int minutes) {
        timeToLive = minutes;
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        String dateFormat = "MMMM dd, yyyy  hh:mm:ss a";
        java.text.DateFormat f = new java.text.SimpleDateFormat(dateFormat, Locale.US);
        pw.println();
        pw.println("//======= Session State =======//");
        pw.println("        sessionID [" + sessionID + "]");
        pw.println("creationTimeStamp [" + f.format(creationTimeStamp.getTime()) + "]");
        pw.println("        lastTouchTimeStamp [" + f.format(lastTouchTimeStamp.getTime()) + "]");
        pw.println("       timeToLive [" + timeToLive + "]");
        pw.flush();
        pw.println();
        if (sessionDataHashtable != null) {
            pw.println();
            pw.println("    //======= Session Data =======//");
            pw.flush();
            if (sessionDataHashtable.size() < 1) {
                pw.println("    -- No Session Data --");
            } else {
                Set keySet = sessionDataHashtable.keySet();
                if (keySet == null) {
                    pw.println("Unable to fetch keySet from sessionDataHashtable.");
                    pw.flush();
                    pw.close();
                    return sw.toString();
                }
                int i = 0;
                Iterator it = keySet.iterator();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    Object obj = sessionDataHashtable.get(key);
                    if (obj == null) {
                        pw.println("    [" + key + "] = [null]");
                    } else {
                        pw.println("    [" + key + "] = [" + sessionDataHashtable.get(key).toString() + "]");
                    }
                    pw.flush();
                }
            }
        }
        if (checkoutTable != null) {
            pw.println();
            pw.println("    //====== Checkout Table ======//");
            if (checkoutTable.size() < 1) {
                pw.println("    -- Nothing Checked Out --");
            } else {
                Enumeration en = checkoutTable.elements();
                while (en.hasMoreElements()) {
                    String value = en.nextElement().toString();
                    pw.println("    [" + value + "]");
                }
            }
        }
        pw.println();
        pw.flush();
        pw.close();
        return sw.toString();
    }

    /**
     *  Touches <code>this</code> session's last time touched clock,
     *  thereby extending its life.
     */
    public void touch() {
        lastTouchTimeStamp = Calendar.getInstance();
    }

    /**
     *  Unlocks a previously checked-out object.  The value of the
     *  object is not changed.
     *
     *  @throws SessionException if the key was not checked out.
     */
    public Object undoCheckout(SessionKey key) throws SessionException {
        if (checkoutTable.contains(key.toString())) {
            checkoutTable.remove(key.toString());
        } else {
            throw new SessionException(key + " was not checked out first.", SessionException.NOT_CHECKEDOUT);
        }
        return getValue(key);
    }
}
