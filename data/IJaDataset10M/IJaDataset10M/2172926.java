package uk.org.ogsadai.resource;

import java.util.Calendar;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.config.ConfigurationValueIllegalException;
import uk.org.ogsadai.context.OGSADAIConstants;
import uk.org.ogsadai.exception.DAIClassCreateException;
import uk.org.ogsadai.exception.DAIClassMissingInterfaceException;
import uk.org.ogsadai.exception.DAIClassNotFoundException;
import uk.org.ogsadai.resource.dataresource.DataResource;
import uk.org.ogsadai.resource.dataresource.DataResourceState;
import uk.org.ogsadai.resource.datasink.DataSinkResource;
import uk.org.ogsadai.resource.datasink.DataSinkResourceState;
import uk.org.ogsadai.resource.datasink.SimpleDataSinkResource;
import uk.org.ogsadai.resource.datasource.DataSourceResource;
import uk.org.ogsadai.resource.datasource.DataSourceResourceState;
import uk.org.ogsadai.resource.datasource.SimpleDataSourceResource;

/**
 * A simple implementation of a resource factory which provides utility methods
 * to assist in resource creation and registration. An instance of this object
 * is created for each request.
 * 
 * @author The OGSA-DAI Project Team
 */
public class SimpleResourceFactory implements ResourceFactory {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2009.";

    /** Logger for this class. */
    private static final DAILogger LOG = DAILogger.getLogger(SimpleResourceFactory.class);

    /** Security context for the request. */
    private final SecurityContext mSecurityContext;

    /** Resource manager. */
    private final ResourceManager mResourceManager;

    /**
     * Constructor.
     * 
     * @param securityContext  
     *     Security context for this request.
     * @param resourceManager  
     *     Resource manager.
     */
    public SimpleResourceFactory(final SecurityContext securityContext, final ResourceManager resourceManager) {
        mSecurityContext = securityContext;
        mResourceManager = resourceManager;
    }

    /**
     * {@inheritDoc}
     */
    public DataResource createDataResource(final ResourceID templateResourceID) throws ResourceTypeException, ResourceUnknownException, ResourceCreationException {
        DataResourceState state = createDataResourceState(templateResourceID);
        String resourceClassName = state.getDataResourceClass();
        try {
            DataResource resource = createDataResource(resourceClassName);
            resource.initialize(state);
            return resource;
        } catch (Throwable e) {
            throw new ResourceCreationException(ResourceType.DATA_RESOURCE, templateResourceID, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public DataResourceState createDataResourceState(final ResourceID templateResourceID) throws ResourceUnknownException, ResourceTypeException {
        DataResourceState state = (DataResourceState) mResourceManager.getResourceStateTemplate(templateResourceID, ResourceType.DATA_RESOURCE);
        state.getResourceLifetime().setCreationTime(Calendar.getInstance());
        return state;
    }

    /**
     * {@inheritDoc}
     * @throws ConfigurationValueIllegalException
     *             if there was a problem loading or instantiating the data
     *             resource class
     */
    public DataResource createDataResource(final String resourceClassName) {
        DataResource resource = null;
        try {
            Class resourceClass = Class.forName(resourceClassName);
            resource = (DataResource) resourceClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new ConfigurationValueIllegalException(resourceClassName, new DAIClassNotFoundException(resourceClassName));
        } catch (ClassCastException e) {
            throw new ConfigurationValueIllegalException(resourceClassName, new DAIClassMissingInterfaceException(resourceClassName, DataResource.class.getName()));
        } catch (Exception e) {
            throw new ConfigurationValueIllegalException(resourceClassName, new DAIClassCreateException(resourceClassName, e));
        }
        return resource;
    }

    /**
     * {@inheritDoc}
     */
    public DataSinkResource createDataSinkResource() throws ResourceCreationException {
        DataSinkResourceState state = null;
        try {
            state = (DataSinkResourceState) mResourceManager.getResourceStateTemplate(OGSADAIConstants.DATA_SINK_TEMPLATE, ResourceType.DATA_SINK);
        } catch (Throwable e) {
            throw new ResourceCreationException(ResourceType.DATA_SINK, OGSADAIConstants.DATA_SINK_TEMPLATE, e);
        }
        state.getResourceLifetime().setCreationTime(Calendar.getInstance());
        DataSinkResource dataSinkResource = new SimpleDataSinkResource();
        dataSinkResource.initialize(state);
        return dataSinkResource;
    }

    /**
     * {@inheritDoc}
     */
    public DataSourceResource createDataSourceResource() throws ResourceCreationException {
        DataSourceResourceState state = null;
        try {
            state = (DataSourceResourceState) mResourceManager.getResourceStateTemplate(OGSADAIConstants.DATA_SOURCE_TEMPLATE, ResourceType.DATA_SOURCE);
        } catch (Throwable e) {
            throw new ResourceCreationException(ResourceType.DATA_SOURCE, OGSADAIConstants.DATA_SOURCE_TEMPLATE, e);
        }
        state.getResourceLifetime().setCreationTime(Calendar.getInstance());
        DataSourceResource dataSourceResource = new SimpleDataSourceResource();
        dataSourceResource.initialize(state);
        return dataSourceResource;
    }

    /**
     * {@inheritDoc}
     */
    public ResourceID addResource(final DataResource resource) {
        ResourceID resourceID = mResourceManager.createUniqueID();
        resource.getState().setResourceID(resourceID);
        try {
            mResourceManager.insertResource(resource, mSecurityContext);
        } catch (ResourceIDAlreadyAssignedException e) {
        }
        return resourceID;
    }

    /**
     * {@inheritDoc}
     */
    public ResourceID addResource(final DataSinkResource resource) {
        ResourceID resourceID = mResourceManager.createUniqueID();
        resource.getState().setResourceID(resourceID);
        try {
            mResourceManager.insertResource(resource, mSecurityContext);
        } catch (ResourceIDAlreadyAssignedException e) {
        }
        return resourceID;
    }

    /**
     * {@inheritDoc}
     */
    public ResourceID addResource(final DataSourceResource resource) {
        ResourceID resourceID = mResourceManager.createUniqueID();
        resource.getState().setResourceID(resourceID);
        try {
            mResourceManager.insertResource(resource, mSecurityContext);
        } catch (ResourceIDAlreadyAssignedException e) {
        }
        return resourceID;
    }

    /**
     * {@inheritDoc}
     */
    public void addResource(final ResourceID resourceID, final DataResource resource) throws ResourceIDAlreadyAssignedException {
        resource.getState().setResourceID(resourceID);
        mResourceManager.insertResource(resource, mSecurityContext);
    }

    /**
     * {@inheritDoc}
     */
    public void addResource(final ResourceID resourceID, final DataSinkResource resource) throws ResourceIDAlreadyAssignedException {
        resource.getState().setResourceID(resourceID);
        mResourceManager.insertResource(resource, mSecurityContext);
    }

    /**
     * {@inheritDoc}
     */
    public void addResource(final ResourceID resourceID, final DataSourceResource resource) throws ResourceIDAlreadyAssignedException {
        resource.getState().setResourceID(resourceID);
        mResourceManager.insertResource(resource, mSecurityContext);
    }
}
