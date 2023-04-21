package krico.javali.model;

import krico.javali.controller.JavaliController;
import javax.servlet.http.*;
import java.util.Date;

/**
 * This is the session used by the javali system.  When a user is loged in, all his information is stored on his session.
 */
public class JavaliSession implements HttpSessionBindingListener {

    public void valueBound(HttpSessionBindingEvent event) {
        JavaliController.debug(JavaliController.LG_VERBOSE, "Value BOUND");
    }

    public void valueUnbound(HttpSessionBindingEvent event) {
        JavaliController.debug(JavaliController.LG_VERBOSE, "Value UNBOUND");
        JavaliDataStore jds = getDataStore();
        if (jds != null) {
            JavaliMailStore jms = jds.getMailStore();
            if (jms != null && jms.isConnected()) {
                JavaliController.tell("Logout: " + getUser().getLogin() + "@" + getUser().getHost());
                try {
                    jms.disconnect();
                    jds.store();
                    if (!getUserLoggedOut()) jds.saveSession(this);
                } catch (Exception e) {
                    JavaliController.debug(JavaliController.LG_DEBUG, "Exception unbinding HttpSession", e);
                }
            } else if (getUser() != null) JavaliController.tell("Logout, but jsm was not conected: " + getUser().getLogin() + "@" + getUser().getHost());
        }
    }

    boolean userLoggedOut = false;

    JavaliDataStore dataStore = null;

    long creation;

    JavaliError lastError = null;

    JavaliClipBoard clipBoard = null;

    SystemConfig systemConfig = null;

    /**
     * The systemConfig of this JavaliSession
     */
    protected void setSystemConfig(SystemConfig conf) {
        systemConfig = conf;
    }

    /**
     * The systemConfig of this JavaliSession
     */
    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    /**
     * The clipBoard for this session
     * @return Never returns null, if there is no clipboard, a new one is created
     */
    public JavaliClipBoard getClipBoard() {
        if (clipBoard == null) clipBoard = new JavaliClipBoard();
        return clipBoard;
    }

    /**
     * The clipBoard for this session
     */
    public void setClipBoard(JavaliClipBoard clip) {
        clipBoard = clip;
    }

    /**
     * Get the mailstore from the data store<br>
     * <b>Note: </b> calls {@link JavaliDataStore#getMailStore getDataStore().getMailStore()}
     * @see #getDataStore
     */
    public JavaliMailStore getMailStore() {
        if (getDataStore() == null) return null;
        return getDataStore().getMailStore();
    }

    /**
     * A boolean indicating wether the user logdout or if this is a session expiration
     */
    public void setUserLoggedOut(boolean b) {
        userLoggedOut = b;
    }

    /**
     * A boolean indicating wether the user logdout or if this is a session expiration
     */
    public boolean getUserLoggedOut() {
        return userLoggedOut;
    }

    /**
     * The last error that occurred for this session
     */
    public JavaliError getLastError() {
        return lastError;
    }

    /**
     * The last error that occurred for this session
     */
    public void setLastError(JavaliError err) {
        lastError = err;
    }

    /**
     * Returns true if the user that owns this session is logged in
     */
    public boolean isConnected() {
        if (getDataStore() == null || getDataStore().getMailStore() == null) return false;
        return getDataStore().getMailStore().isConnected();
    }

    public JavaliDataStore getDataStore() {
        return dataStore;
    }

    public void setDataStore(JavaliDataStore data) {
        dataStore = data;
    }

    /**
     * Default constructor
     */
    public JavaliSession() {
        setCreation(System.currentTimeMillis());
    }

    /**
     * Construct a JavaliSession with a given SystemConfig
     */
    public JavaliSession(SystemConfig conf) {
        setCreation(System.currentTimeMillis());
        setSystemConfig(conf);
    }

    /**
     * The time when this session was created
     */
    public void setCreation(long l) {
        creation = l;
    }

    /**
     * The time when this session was created
     */
    public long getCreation() {
        return creation;
    }

    /**
     * The owner of this session
     */
    public User getUser() {
        if (getDataStore() == null) return null;
        return getDataStore().getUser();
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof JavaliSession) {
            JavaliSession other = (JavaliSession) o;
            if (other.getCreation() == getCreation() && (other.getSystemConfig() == null ? getSystemConfig() == null : other.getSystemConfig().equals(getSystemConfig()))) return true;
        }
        return true;
    }
}
