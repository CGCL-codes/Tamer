package uk.org.ogsadai.resource;

import java.util.List;
import uk.org.ogsadai.activity.ActivityID;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.activity.event.ActivityEventDispatcher;
import uk.org.ogsadai.persistence.Persistable;

/**
 * This interface provides information on the activities supported by a
 * specific resource. Activities are identified by activity IDs - which
 * identify an activity class plus a specific configuration of that
 * class. Activities are exposed to clients via activity names.
 * This interface provides the mapping from activity names to activity
 * IDs for a resource.
 *
 * Classes implementing this interface can be persisted by OGSA-DAI
 * persistence components.

 * @author The OGSA-DAI Project Team
 */
public interface SupportedActivities extends Persistable, ActivityEventDispatcher {

    /**
     * Return the list of activity names - as exposed to clients - and
     * supported by the resource. 
     * Changes to the list are local and are not reflected in this class.
     * Only calls to <tt>addActivity</tt> and <tt>removeActivity</tt>
     * can do that.
     * 
     * @return List&lt;ActivityName&gt;
     */
    public List getActivityNames();

    /**
     * Does the resource support the activity with the given name?
     * 
     * @param activityName
     *     The name of the activity
     * @return <code>true</code> if the activity is supported
     */
    public boolean isSupported(ActivityName activityName);

    /**
     * Add an activity to the supported activities.
     * 
     * @param name
     *     The name of the activity as exposed to clients.
     * @param id
     *     The ID of the activity.
     */
    public void addActivity(ActivityName name, ActivityID id);

    /**
     * Remove an activity from the supported activities.
     * 
     * @param name
     *     The activity to be removed
     * @throws ResourceActivityUnknownException
     *     If the activity is unknown.
     */
    public void removeActivity(ActivityName name) throws ResourceActivityUnknownException;

    /**
     * Get the activity ID that corresponds to the given activity name.
     * 
     * @param activityName
     *     The name of the activity as exposed to clients.
     * @return the activity ID corresponding to the given activity name
     * @throws ResourceActivityUnknownException
     *     If the activity is unknown.
     */
    public ActivityID getActivityID(ActivityName activityName) throws ResourceActivityUnknownException;
}
