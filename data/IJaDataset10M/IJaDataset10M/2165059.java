package org.actioncenters.core.activitylog;

import java.io.Serializable;
import java.util.Calendar;

/**
 * The Interface IActivity.
 * 
 * @author dougk
 */
public interface IActivity extends Serializable {

    /**
     * Gets the user id.
     * 
     * @return The ID of the user who initiated the activity.
     */
    String getUserId();

    /**
     * Sets the user id.
     * 
     * @param userId
     *            The ID of the user who initiated the activity.
     */
    void setUserId(String userId);

    /**
     * Gets the sub system.
     * 
     * @return The sub-system that logged this activity.
     */
    String getSubSystem();

    /**
     * Sets the sub system.
     * 
     * @param subSystem
     *            The sub-system that logged this activity.
     */
    void setSubSystem(String subSystem);

    /**
     * Gets the timestamp.
     * 
     * @return The timestamp of the activity.
     */
    Calendar getTimestamp();

    /**
     * Sets the timestamp.
     * 
     * @param timestamp
     *            The timestamp of the activity.
     */
    void setTimestamp(Calendar timestamp);

    /**
     * Gets the activity description.
     * 
     * @return Description of the activity.
     */
    String getActivityDescription();

    /**
     * Sets the activity description.
     * 
     * @param activityDescription
     *            Description of the activity.
     */
    void setActivityDescription(String activityDescription);

    /**
     * Gets the requestid.
     * 
     * @return id of the request.
     */
    String getRequestid();

    /**
     * Sets the requestid.
     * 
     * @param requestid
     *            id of the request
     */
    void setRequestid(String requestid);

    /**
     * Gets the client id.
     * 
     * @return the clientId
     */
    String getClientId();

    /**
     * Sets the client id.
     * 
     * @param clientId the clientId to set
     */
    void setClientId(String clientId);
}
