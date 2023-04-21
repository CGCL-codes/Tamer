package org.actioncenters.core.activitylog;

/**
 * Maintains the activity information for a thread of activity.
 * 
 * @author dougk
 */
public class ActivityInfo {

    /** The Constant threadLocal. */
    private static final ThreadLocal<ActivityInfo> THREAD_LOCAL = new ThreadLocal<ActivityInfo>();

    /** The ID of the user that generated the activity. */
    private String userId;

    /** The request ID of this activity. */
    private String requestId;

    /** The client ID for this activity. */
    private String clientId;

    /**
     * Constructor to create the ActivityInfo.
     * 
     * @param userId
     *            The ID of the user that generated this activity.
     * @param requestId
     *            The ID of the request that generated this activity.
     * @param clientId
     *            The ID of the client that generated this activity.
     */
    private ActivityInfo(String userId, String requestId, String clientId) {
        setUserId(userId);
        setRequestId(requestId);
        setClientId(clientId);
    }

    /**
     * Gets the user id.
     * 
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     * 
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the request id.
     * 
     * @return the requestId
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the request id.
     * 
     * @param requestId
     *            the requestId to set
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the client id.
     * 
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the client id.
     * 
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Sets the activity for this thread.
     * 
     * @param userId
     *            The ID of the user that generated this activity.
     * @param requestId
     *            The ID of the request that generated this activity.
     * @param clientId
     *            The ID of the client that generated this activity.
     */
    public static void setActivityInfo(String userId, String requestId, String clientId) {
        THREAD_LOCAL.set(new ActivityInfo(userId, requestId, clientId));
    }

    /**
     * Gets the ActivityInfo for this thread.
     * 
     * @return The ActivityInfo for this thread.
     */
    public static ActivityInfo getActivityInfo() {
        return THREAD_LOCAL.get();
    }
}
