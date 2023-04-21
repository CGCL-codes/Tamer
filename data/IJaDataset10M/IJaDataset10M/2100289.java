package ca.sqlpower.architect.profile;

import ca.sqlpower.architect.profile.event.ProfileResultListener;
import ca.sqlpower.object.SPObject;
import ca.sqlpower.sqlobject.SQLObject;

/**
 * A ProfileResult is an interface for populating profile data about
 * database objects (for example, tables and columns), and for monitoring
 * the progress of the profiling operation, which can often take considerable
 * time to calculate.
 * 
 * @param T The type of SQLObject that this profile result calculates
 * and holds results for.
 */
public interface ProfileResult<T extends SQLObject> extends SPObject {

    /**
     * Returns the SQLObject that is profiled by this ProfileResult.
     */
    public abstract T getProfiledObject();

    /**
     * Returns the date and time that this ProfileResult started
     * profiling the profiled object.
     */
    public abstract long getCreateStartTime();

    /**
     * Returns the time it took to create this ProfileResult in milliseconds.
     */
    public abstract long getTimeToCreate();

    /**
     * Returns the date and time that this ProfileResult finished profiling
     * the profiled object.
     */
    public abstract long getCreateEndTime();

    /**
     * Returns the Exception that occured during the profiling of the
     * profiled object. If this method returns null then the profiled
     * object is not done populating yet or it has sucessfully populated 
     * without throwing an Exception.
     */
    public abstract Exception getException();

    /**
     * Add a ProfileResultListener that should be notified of changes in the
     * status of this ProfileResult's progress during a profile operation 
     */
    public void addProfileResultListener(ProfileResultListener listener);

    /**
     * Remove a ProfileResultListener from this ProfileResult's 
     * collection of ProfileResultListeners
     */
    public void removeProfileResultListener(ProfileResultListener listener);
}
