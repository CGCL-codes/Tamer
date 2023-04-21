package uk.org.ogsadai.monitoring.activity;

import uk.org.ogsadai.activity.RequestDescriptor;
import uk.org.ogsadai.activity.event.ActivityListener;
import uk.org.ogsadai.activity.event.BlockReaderListener;
import uk.org.ogsadai.activity.event.PipeListener;
import uk.org.ogsadai.activity.request.OGSADAIRequestConfiguration;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.monitoring.MonitoringFramework;
import uk.org.ogsadai.resource.OnDemandResourceProperty;
import uk.org.ogsadai.resource.OnDemandResourcePropertyCallback;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceUnknownException;
import uk.org.ogsadai.resource.SimpleOnDemandResourceProperty;
import uk.org.ogsadai.resource.request.RequestResource;

/**
 * A monitoring framework which listens to activity status updates and pipe and 
 * block reader events. It counts the number of blocks that are read and written
 * by an activity. This progress status is made available as a resource property
 * on the request resource.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ActivityProgressMonitoringFramework implements MonitoringFramework {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009.";

    /** Logger for this class. */
    private static final DAILogger LOG = DAILogger.getLogger(ActivityProgressMonitoringFramework.class);

    /**
     * {@inheritDoc}
     */
    public void registerListeners(RequestDescriptor requestDescriptor, OGSADAIRequestConfiguration requestContext) {
        LOG.debug("Registering listeners.");
        ActivityStatusTracker statusListener = new ActivityStatusTracker();
        ActivityProgressListener activityListener = new ActivityProgressListener(statusListener);
        requestContext.registerActivityListener(activityListener);
        PipeProgressListener pipeListener = new PipeProgressListener(statusListener);
        requestContext.registerPipeListener(pipeListener);
        BlockReaderProgressListener blockReaderListener = new BlockReaderProgressListener(statusListener);
        requestContext.registerBlockReaderListeners(new BlockReaderListener[] { blockReaderListener });
        LOG.debug("Registered PipeProgressListener, BlockReaderProgressListener and ActivityProgressListener.");
        ResourceID id = requestContext.getRequestID();
        try {
            RequestResource request = (RequestResource) OGSADAIContext.getInstance().getResourceManager().getResource(id);
            OnDemandResourcePropertyCallback callback = new ActivityStatusPropertyCallback(id, statusListener);
            OnDemandResourceProperty property = new SimpleOnDemandResourceProperty(ActivityStatusPropertyCallback.PROPERTY_NAME, callback);
            request.getState().getResourcePropertySet().addProperty(property);
            LOG.debug("Added new resource property " + ActivityStatusPropertyCallback.PROPERTY_NAME);
        } catch (ResourceUnknownException e) {
            LOG.error(e);
        }
    }

    public void removeActivityListener(ActivityListener listener) {
        throw new UnsupportedOperationException();
    }

    public void removePipeListener(PipeListener listener) {
        throw new UnsupportedOperationException();
    }

    public void addActivityListener(ActivityListener listener) {
        throw new UnsupportedOperationException();
    }

    public void addPipeListener(PipeListener listener) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }
}
