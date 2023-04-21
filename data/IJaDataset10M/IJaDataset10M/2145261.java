package uk.org.ogsadai.monitoring;

import java.util.Iterator;
import uk.org.ogsadai.activity.RequestDescriptor;
import uk.org.ogsadai.activity.event.ActivityListener;
import uk.org.ogsadai.activity.event.CompositeActivityListener;
import uk.org.ogsadai.activity.event.CompositePipeListener;
import uk.org.ogsadai.activity.event.PipeListener;
import uk.org.ogsadai.activity.request.OGSADAIRequestConfiguration;

/**
 * A simple, thread-safe monitoring framework that can be configured
 * programmatically.
 * 
 * @author The OGSA-DAI Project Team
 */
public class SimpleMonitoringFramework implements MonitoringFramework {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2007";

    /** Handles all activity events. */
    private final CompositeActivityListener mActivityListener = new CompositeActivityListener();

    /** Handles all pipe events. */
    private final CompositePipeListener mPipeListener = new CompositePipeListener();

    public synchronized void addActivityListener(ActivityListener listener) {
        mActivityListener.addActivityListener(listener);
    }

    public synchronized void removeActivityListener(ActivityListener listener) {
        mActivityListener.removeActivityListener(listener);
    }

    public synchronized void addPipeListener(final PipeListener listener) {
        mPipeListener.addPipeListener(listener);
    }

    public synchronized void removePipeListener(PipeListener listener) {
        mPipeListener.removePipeListener(listener);
    }

    public synchronized void clear() {
        for (Iterator i = mActivityListener.getActivityListeners().iterator(); i.hasNext(); ) {
            mActivityListener.removeActivityListener((ActivityListener) i.next());
        }
        for (Iterator i = mPipeListener.getPipeListeners().iterator(); i.hasNext(); ) {
            mPipeListener.removePipeListener((PipeListener) i.next());
        }
    }

    public void registerListeners(RequestDescriptor requestDescriptor, OGSADAIRequestConfiguration requestContext) {
        requestContext.registerActivityListener(mActivityListener);
        requestContext.registerPipeListener(mPipeListener);
    }
}
