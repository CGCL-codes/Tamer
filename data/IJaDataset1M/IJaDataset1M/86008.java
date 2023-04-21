package uk.org.ogsadai.test.dao.resource;

import java.util.List;
import java.util.Vector;
import uk.org.ogsadai.activity.ActivityID;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.context.OGSADAIConstants;
import uk.org.ogsadai.persistence.PersistenceException;
import uk.org.ogsadai.persistence.resource.ResourceStateDAO;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceState;
import uk.org.ogsadai.resource.ResourceUnknownException;
import uk.org.ogsadai.resource.SupportedActivities;
import uk.org.ogsadai.resource.datasink.DataSinkResourceState;
import uk.org.ogsadai.resource.datasink.SimpleDataSinkResourceState;
import uk.org.ogsadai.resource.datasource.DataSourceResourceState;
import uk.org.ogsadai.resource.datasource.SimpleDataSourceResourceState;
import uk.org.ogsadai.resource.request.RequestResourceState;
import uk.org.ogsadai.resource.request.SimpleRequestResourceState;
import uk.org.ogsadai.resource.session.SessionResourceState;
import uk.org.ogsadai.resource.session.SimpleSessionResourceState;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A simple resource DAO that caches resource templates only.
 * By default the cache contains default templates for data
 * sources, data sinks, sessions and requests.
 *
 * @author The OGSA-DAI Project Team
 */
public class TestResourceStateDAO implements ResourceStateDAO {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Resource templates mapped by resource name */
    private ConcurrentMap mTemplates;

    /**
     * Constructor
     */
    public TestResourceStateDAO() {
        mTemplates = new ConcurrentHashMap();
        DataSinkResourceState dataSinkResourceState = new SimpleDataSinkResourceState();
        dataSinkResourceState.setResourceID(OGSADAIConstants.DATA_SINK_TEMPLATE);
        SupportedActivities supportedActivities = dataSinkResourceState.getSupportedActivities();
        supportedActivities.addActivity(new ActivityName("uk.org.ogsadai.ReadFromDataSink"), new ActivityID("uk.org.ogsadai.ReadFromDataSink"));
        mTemplates.put(OGSADAIConstants.DATA_SINK_TEMPLATE, dataSinkResourceState);
        DataSourceResourceState dataSourceResourceState = new SimpleDataSourceResourceState();
        dataSourceResourceState.setResourceID(OGSADAIConstants.DATA_SOURCE_TEMPLATE);
        supportedActivities = dataSourceResourceState.getSupportedActivities();
        supportedActivities.addActivity(new ActivityName("uk.org.ogsadai.WriteToDataSource"), new ActivityID("uk.org.ogsadai.WriteToDataSource"));
        mTemplates.put(OGSADAIConstants.DATA_SOURCE_TEMPLATE, dataSourceResourceState);
        SessionResourceState sessionResourceState = new SimpleSessionResourceState();
        sessionResourceState.setResourceID(OGSADAIConstants.SESSION_TEMPLATE);
        supportedActivities = sessionResourceState.getSupportedActivities();
        supportedActivities.addActivity(new ActivityName("uk.org.ogsadai.ObtainFromSessionActivity"), new ActivityID("uk.org.ogsadai.ObtainFromSessionActivity"));
        supportedActivities.addActivity(new ActivityName("uk.org.ogsadai.DeliverToSessionActivity"), new ActivityID("uk.org.ogsadai.DeliverToSessionActivity"));
        mTemplates.put(OGSADAIConstants.SESSION_TEMPLATE, sessionResourceState);
        RequestResourceState requestResourceState = new SimpleRequestResourceState();
        requestResourceState.setResourceID(OGSADAIConstants.REQUEST_TEMPLATE);
        mTemplates.put(OGSADAIConstants.REQUEST_TEMPLATE, requestResourceState);
    }

    /**
     * No-op
     *
     * @return empty list always.
     */
    public List listResources() throws PersistenceException {
        return new Vector();
    }

    /**
     * No-op
     *
     * @return empty list always.
     */
    public List listResourceStateTemplates() throws PersistenceException {
        List ids = new Vector();
        ids.addAll(mTemplates.keySet());
        return ids;
    }

    /**
     * No-op
     *
     * @return <tt>false</tt> always.
     */
    public boolean isResourceKnown(ResourceID resourceID) throws PersistenceException {
        return false;
    }

    /**
     * No-op
     *
     * @return <tt>false</tt> always.
     */
    public boolean isResourceStateTemplateKnown(ResourceID resourceID) throws PersistenceException {
        return mTemplates.containsKey(resourceID);
    }

    /**
     * No-op
     *
     * @throws ResourceUnknownException
     *     Always.
     */
    public ResourceState getResourceState(ResourceID resourceID) throws ResourceUnknownException, PersistenceException {
        throw new ResourceUnknownException(resourceID);
    }

    /**
     * Return a named template from the cache.
     *
     * @throws IllegalArgumentException
     *    If <tt>resourceID</tt> is <tt>null</tt>.
     * @throws ResourceUnknownException
     *     If there is no template with the given ID.
     */
    public ResourceState getResourceStateTemplate(ResourceID resourceID) throws ResourceUnknownException, PersistenceException {
        if (resourceID == null) {
            throw new IllegalArgumentException("resourceID must not be null");
        }
        ResourceState state = null;
        if (!mTemplates.containsKey(resourceID)) {
            throw new ResourceUnknownException(resourceID);
        }
        state = (ResourceState) mTemplates.get(resourceID);
        return state;
    }

    /**
     * Insert a template into the cache.
     *
     * @throws IllegalArgumentException
     *    If <tt>resourceTemplate</tt> is <tt>null</tt>.
     */
    public void insertResourceStateTemplate(ResourceState resourceTemplate) {
        if (resourceTemplate == null) {
            throw new IllegalArgumentException("resourceTemplate must not be null");
        }
        mTemplates.put(resourceTemplate.getResourceID(), resourceTemplate);
    }

    /**
     * No-op
     */
    public void deleteResourceState(ResourceID resourceID) throws PersistenceException {
    }

    /**
     * @throws IllegalArgumentException
     *    If <tt>resourceID</tt> is <tt>null</tt>.
     */
    public void deleteResourceStateTemplate(ResourceID resourceID) throws PersistenceException {
        if (resourceID == null) {
            throw new IllegalArgumentException("resourceID must not be null");
        }
        mTemplates.remove(resourceID);
    }

    /**
     * No-op
     *
     * @return <tt>null</tt>
     */
    public void insertResourceState(ResourceState resourceState) throws PersistenceException {
    }

    /**
     * This class does not support auto-detection of changes to
     * configuration files.
     *
     * @return <tt>false</tt> always.
     */
    public boolean hasPersistedStateChanged(ResourceID resourceID) throws PersistenceException {
        return false;
    }
}
