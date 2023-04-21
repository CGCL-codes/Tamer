package uk.org.ogsadai.monitoring;

import java.util.EventObject;

/**
 * The base class for all OGSA-DAI monitoring events. It records a timestamp at
 * construction time and provides access to it via the
 * <code>getCreationTime()</code> method.
 * 
 * @author The OGSA-DAI Project Team
 */
public abstract class MonitoringEvent extends EventObject {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Version ID for serialisation purposes. */
    private static final long serialVersionUID = 1L;

    /** Timestamp recorded during construction. */
    private final long mCreationTime;

    /**
     * Constructs a <code>MonitoringEvent</code>.
     * 
     * @param eventSource
     *            object that generated the event
     */
    public MonitoringEvent(final Object eventSource) {
        super(eventSource);
        mCreationTime = System.currentTimeMillis();
    }

    /**
     * Gets the time in milliseconds at which the event was constructed.
     * 
     * @return the timestamp of this object
     */
    public long getCreationTime() {
        return mCreationTime;
    }
}
