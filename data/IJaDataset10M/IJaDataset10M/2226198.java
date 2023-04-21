package prisms.logging;

/** Represents a single log entry */
public class LogEntry {

    private int theID;

    private String theInstanceLocation;

    private long theLogTime;

    private String theApp;

    private String theClient;

    private prisms.arch.ds.User theUser;

    private String theSessionID;

    private String theTrackingData;

    private org.apache.log4j.Level theLevel;

    private String theLoggerName;

    private String theMessage;

    private String theStackTrace;

    private int theDuplicateRef;

    private long theSaveTime;

    private int theSize;

    /** Creates a log entry */
    public LogEntry() {
        theID = -1;
        theDuplicateRef = -1;
        theSaveTime = -1;
    }

    /** @return This entry's ID */
    public int getID() {
        return theID;
    }

    /** @param id The ID for this entry */
    public void setID(int id) {
        theID = id;
    }

    /** @return The instance at which this log entry occurred */
    public String getInstanceLocation() {
        return theInstanceLocation;
    }

    /** @param instance The instance at which this log entry occurred */
    public void setInstanceLocation(String instance) {
        theInstanceLocation = instance;
    }

    /** @return The time at which this log entry occurred */
    public long getLogTime() {
        return theLogTime;
    }

    /** @param logTime The time at which this log entry occurred */
    public void setLogTime(long logTime) {
        theLogTime = logTime;
    }

    /** @return The application under which this log entry occurred */
    public String getApp() {
        return theApp;
    }

    /** @param app The application under which this log entry occurred */
    public void setApp(String app) {
        theApp = app;
    }

    /** @return The client configuration under which this log entry occurred */
    public String getClient() {
        return theClient;
    }

    /** @param client The client configuration under which this log entry occurred */
    public void setClient(String client) {
        theClient = client;
    }

    /** @param user The user under whose session this log entry occurred */
    public void setUser(prisms.arch.ds.User user) {
        theUser = user;
    }

    /** @return The user under whose session this log entry occurred */
    public prisms.arch.ds.User getUser() {
        return theUser;
    }

    /** @return The ID of the session under which this log entry occurred */
    public String getSessionID() {
        return theSessionID;
    }

    /** @param sessionID The ID of the session under which this log entry occurred */
    public void setSessionID(String sessionID) {
        theSessionID = sessionID;
    }

    /** @return The tracking data associated with the thread in which this log entry occurred */
    public String getTrackingData() {
        return theTrackingData;
    }

    /**
	 * @param trackingData The tracking data associated with the thread in which this log entry
	 *        occurred
	 */
    public void setTrackingData(String trackingData) {
        theTrackingData = trackingData;
    }

    /** @return The severity level of this entry */
    public org.apache.log4j.Level getLevel() {
        return theLevel;
    }

    /** @param level The severity level for this entry */
    public void setLevel(org.apache.log4j.Level level) {
        theLevel = level;
    }

    /** @return The name of the logger that logged this entry */
    public String getLoggerName() {
        return theLoggerName;
    }

    /** @param loggerName The name of the logger that logged this entry */
    public void setLoggerName(String loggerName) {
        theLoggerName = loggerName;
    }

    /** @return This log entry's message */
    public String getMessage() {
        return theMessage;
    }

    /** @param message This log entry's message */
    public void setMessage(String message) {
        theMessage = message;
    }

    /** @return The stack trace information associated with this entry */
    public String getStackTrace() {
        return theStackTrace;
    }

    /** @param stackTrace The stack trace information to be associated with this entry */
    public void setStackTrace(String stackTrace) {
        theStackTrace = stackTrace;
    }

    /** @param duplicateRef The ID of the log entry of which this entry is a duplicate */
    public void setDuplicateRef(int duplicateRef) {
        theDuplicateRef = duplicateRef;
    }

    /**
	 * @return The ID of the entry of which this entry is a duplicate, meaning this entry has the
	 *         same message and stack trace as this reference
	 */
    public int getDuplicateRef() {
        return theDuplicateRef;
    }

    /** @return The time before which this entry will be protected from purging */
    public long getSaveTime() {
        return theSaveTime;
    }

    /** @param saveTime The time before which this entry should be protected from purging */
    public void setSaveTime(long saveTime) {
        theSaveTime = saveTime;
    }

    /** @return The approximate number of KB this entry takes up in the database */
    public int getSize() {
        return theSize;
    }

    /** @param size The approximate number of KB this entry takes up in the database */
    public void setSize(int size) {
        theSize = size;
    }

    /**
	 * @param entry The entry to compare this entry to
	 * @return Whether the two entries have the same header information
	 */
    public boolean headersSame(LogEntry entry) {
        return equal(theInstanceLocation, entry.theInstanceLocation) && equal(theApp, entry.theApp) && equal(theClient, entry.theClient) && equal(theSessionID, entry.theSessionID) && equal(theUser, entry.theUser) && equal(theTrackingData, entry.theTrackingData) && equal(theLevel, entry.theLevel) && equal(theLoggerName, entry.theLoggerName) && equal(theMessage, entry.theMessage) && equal(theStackTrace, entry.theStackTrace);
    }

    private static boolean equal(Object o1, Object o2) {
        if (o1 == o2) return true;
        return o1 != null && o1.equals(o2);
    }
}
