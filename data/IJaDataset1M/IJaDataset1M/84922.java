package hambo.app.core;

import hambo.util.Device;
import hambo.util.OID;
import hambo.config.ConfigManager;

/**
 * Session context contains information about the user executing the request,
 * which device he is using, the session key.
 */
public class SessionContext {

    /** Implementation class name */
    private static String implClassName = ConfigManager.getConfig("session_context").getProperty("class");

    /** The userId of the user of this session */
    private String userId = null;

    /** The OID of the user of this session */
    private OID oid = null;

    /** The device used by this session */
    private Device device = null;

    /** The session key that uniquely identify this session. */
    private String sessionKey = null;

    /**
     * Default constructor.
     */
    protected SessionContext() {
    }

    /**
     * Initialize session context object.
     *
     * @param userId      the current user's id
     * @param oid         the current user's oid
     * @param device      the user's device
     * @param sessionKey  the session key
     * @param sessionData object holding session data
     */
    protected void init(String userId, OID oid, Device device, String sessionKey, Object sessionData) {
        this.userId = (userId != null) ? userId : "-";
        this.oid = oid;
        this.device = device;
        this.sessionKey = sessionKey;
        initSessionData(sessionData);
    }

    /**
     * Factory method; Creates a new SessionContext object. <i>sessionData</i> are an implementation
     * dependent object for storing/retrieving session data. If set to null it will not be possible
     * to get/set session data through the <i>SessionContext</i> object.
     *
     * @param userId      the current user's id
     * @param oid         the current user's oid
     * @param device      the user's device
     * @param sessionKey  the session key
     * @param sessionData object holding session data
     */
    public static SessionContext createSessionContext(String userId, OID oid, Device device, String sessionKey, Object sessionData) {
        SessionContext impl = null;
        try {
            impl = (SessionContext) Class.forName(implClassName).newInstance();
        } catch (Throwable ex) {
            impl = new SessionContext();
        }
        impl.init(userId, oid, device, sessionKey, sessionData);
        return impl;
    }

    /**
     * Initializes the session data object.
     */
    protected void initSessionData(Object sessionData) {
    }

    /**
     * Retrieve the value of the session-data attribute with the given <i>name</i>.
     * Returns null if the attribute doesn't exist.
     */
    public Object getSessionAttribute(String name) {
        return null;
    }

    /**
     * Retrieve the value of the session-data attribute with the given <i>name</i>.
     * Returns the <i>defaultValue</i> if the attribute doesn't exist.
     */
    public Object getSessionAttribute(String name, Object defaultValue) {
        return defaultValue;
    }

    /**
     * Retrieve the value of the session-data attribute with the given <i>name</i>.
     * Returns null if the attribute doesn't exist.
     */
    public String getSessionAttributeAsString(String name) {
        return null;
    }

    /**
     * Retrieve the value of the session-data attribute with the given <i>name</i>.
     * Returns <i>defaultValue</i> if the attribute doesn't exist.
     */
    public String getSessionAttributeAsString(String name, String defaultValue) {
        return null;
    }

    /**
     * Remove the session attribute with the given <i>name</i>.
     */
    public void removeSessionAttribute(String name) {
    }

    /**
     * Sets the session attribute with the given <i>name</i>.
     */
    public void setSessionAttribute(String name, Object value) {
    }

    /**
     * Returns all session attributes' names as a <code>String</code> array.
     * If no session data is present, a zero length array is returned.
     */
    public String[] getSessionAttributeNames() {
        return new String[0];
    }

    /**
     * Returns the userId of the logged in user. If no user has logged in "-" is returned.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the users OID of the logged in user. If no user has logged in null is returned.
     */
    public OID getOId() {
        return oid;
    }

    /**
     * Returns a {@link hambo.util.Device} that contains 
     * information about the user's device.
     */
    public Device getDevice() {
        return device;
    }

    /**
     * Returns the session key.
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * Returns true if the user has been logged in.
     */
    public boolean isUserLoggedIn() {
        return (oid != null);
    }
}
