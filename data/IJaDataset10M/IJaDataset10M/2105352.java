package uk.org.ogsadai.activity;

import uk.org.ogsadai.activity.event.ActivityEventDispatcher;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.persistence.Persistable;
import uk.org.ogsadai.persistence.event.UpdatePolicyController;

/**
 * This interface provides information about an individual 
 * activity available on the OGSA-DAI server.
 *
 * Classes implementing this interface can be persisted by OGSA-DAI
 * persistence components.
 * 
 * @author The OGSA-DAI Project Team
 */
public interface ActivitySpec extends Persistable, ActivityEventDispatcher, UpdatePolicyController {

    /**
     * Return the activity ID.
     * 
     * @return activity ID
     */
    public ActivityID getActivityID();

    /**
     * Set the activity ID
     * 
     * @param activityID
     *     Activity ID
     */
    public void setActivityID(ActivityID activityID);

    /**
     * Return the name of the activity implementation class.
     * 
     * @return activity class name
     */
    public String getActivityClass();

    /**
     * Set the name of the activity implementation class.
     * 
     * @param activityClass
     *     Class name
     */
    public void setActivityClass(String activityClass);

    /**
     * Return the activity implementation-specific configuration.
     * 
     * @return configuration properties
     */
    public KeyValueProperties getConfiguration();

    /**
     * Return a description of the activity.
     * 
     * @return description
     */
    public String getDescription();

    /**
     * Set a description of the activity
     * 
     * @param description
     *     String description.
     */
    public void setDescription(String description);
}
