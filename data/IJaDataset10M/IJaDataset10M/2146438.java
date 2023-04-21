package uk.org.ogsadai.activity.event;

import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.monitoring.MonitoringEvent;

/**
 * An event which indicates that an occurrence involving an activity has taken
 * place.
 * 
 * @author The OGSA-DAI Project Team
 */
public class ActivityEvent extends MonitoringEvent {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Version ID for serialisation purposes. */
    private static final long serialVersionUID = 1L;

    /** The activity associated with the event. */
    private final Activity mActivity;

    /** The error that occurred, if one has occurred. */
    private Throwable mError;

    /**
     * Constructs an <code>ActivityEvent</code>.
     * 
     * @param activity
     *            the activity that generated the event
     * @throws IllegalArgumentException
     *             if the <code>source</code> argument is <code>null</code>
     */
    public ActivityEvent(final Activity activity) {
        super(activity);
        if (activity == null) {
            throw new IllegalArgumentException("The source argument must not be null.");
        }
        mActivity = activity;
    }

    /**
     * Constructs an <code>ActivityEvent</code> indicating that an error has
     * taken place during activity processing.
     * 
     * @param activity
     *            activity that generated the event
     * @param error
     *            the error that occurred
     * @throws IllegalArgumentException
     *             if the <code>source</code> or <code>error</code>
     *             arguments are <code>null</code>
     */
    public ActivityEvent(final Activity activity, final Throwable error) {
        this(activity);
        if (error == null) {
            throw new IllegalArgumentException("The error argument must not be null.");
        }
        mError = error;
    }

    /**
     * Returns a reference to the <code>Activity</code> that generated the
     * event.
     * 
     * @return the source activity
     */
    public final Activity getActivity() {
        return mActivity;
    }

    /**
     * Gets the error that arose during activity processing. This method should
     * only be invoked if <code>hasError</code> returns true.
     * 
     * @return the <code>Throwable</code> that caused the error
     * @throws IllegalStateException
     *             if this method is invoked for an event that did not involve
     *             an error
     */
    public Throwable getError() {
        if (mError == null) {
            throw new IllegalStateException("This activity event does not involve an error.");
        }
        return mError;
    }

    /**
     * Indicates whether or not the event is associated with an error that
     * occurred while processing an activity.
     * 
     * @return <code>true</code> if an error occurred, <code>false</code>
     *         otherwise
     */
    public boolean hasError() {
        return mError != null;
    }
}
