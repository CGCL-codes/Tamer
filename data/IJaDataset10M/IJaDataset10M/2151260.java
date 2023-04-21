package uk.org.ogsadai.service.gt.resource;

import java.util.Calendar;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceIdentifier;
import org.globus.wsrf.ResourceLifetime;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourcePropertySet;

/**
 * A wrapper class that wraps OGSA-DAI resources into a GT-compliant
 * form.
 *
 * @author The OGSA-DAI Project Team.
 */
public class GTResourceAdapter implements Resource, ResourceIdentifier, ResourceLifetime, ResourceProperties {

    /** Copyright. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** OGSA-DAI resource being wrapped. */
    private final uk.org.ogsadai.resource.Resource mResource;

    /** GT-compliant resource property set. */
    private final GTResourceProperties mResourceProperties;

    /**
     * Creates a new adapter for the given OGSA-DAI resource.
     * 
     * @param resource
     *     OGSA-DAI resource being wrapped.
     */
    public GTResourceAdapter(final uk.org.ogsadai.resource.Resource resource) {
        mResource = resource;
        mResourceProperties = new GTResourceProperties(mResource);
    }

    public Object getID() {
        return mResource.getResourceID();
    }

    public Calendar getCurrentTime() {
        return Calendar.getInstance();
    }

    public Calendar getTerminationTime() {
        uk.org.ogsadai.resource.ResourceLifetime lifetime = mResource.getState().getResourceLifetime();
        return lifetime.getTerminationTime();
    }

    public void setTerminationTime(Calendar terminationTime) {
        uk.org.ogsadai.resource.ResourceLifetime resourceLifetime = mResource.getState().getResourceLifetime();
        resourceLifetime.setTerminationTime(terminationTime);
    }

    public ResourcePropertySet getResourcePropertySet() {
        return mResourceProperties.getResourcePropertySet();
    }

    /**
     * Returns the OGSA-DAI resource that is wrapped by this adapter. 
     * 
     * @return OGSA-DAI resource.
     */
    public uk.org.ogsadai.resource.Resource getResource() {
        return mResource;
    }
}
