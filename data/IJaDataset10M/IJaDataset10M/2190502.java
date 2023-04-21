package com.volantis.map.operation.impl;

import com.volantis.synergetics.descriptorstore.ResourceDescriptor;
import com.volantis.map.operation.ObjectParameters;
import java.util.Date;

/**
 * Simply delegate to the "real" resource descriptor
 */
public class DelegatingResourceDescriptor implements com.volantis.map.operation.ResourceDescriptor {

    /**
     * The underlying resource descriptor we delegate to.
     */
    private final ResourceDescriptor descriptor;

    private final ObjectParameters inputParameters;

    /**
     * Construct the delegating resource descriptor
     * @param descriptor
     */
    public DelegatingResourceDescriptor(ResourceDescriptor descriptor) {
        this.descriptor = descriptor;
        this.inputParameters = new ProxiedParameters(descriptor.getInputParameters());
    }

    public ObjectParameters getInputParameters() {
        return inputParameters;
    }

    public String getExternalID() {
        return descriptor.getExternalID();
    }

    public com.volantis.map.common.param.Parameters getOutputParameters() {
        return new ProxiedParameters(descriptor.getOutputParameters());
    }

    public Date getLastAccess() {
        return descriptor.getLastAccess();
    }

    public Date getLastGenerated() {
        return descriptor.getLastGenerated();
    }

    public String getResourceType() {
        return descriptor.getResourceType();
    }

    public long getTimeToLive() {
        return descriptor.getTimeToLive();
    }

    public void setTimeToLive(long l) {
        descriptor.setTimeToLive(l);
    }
}
