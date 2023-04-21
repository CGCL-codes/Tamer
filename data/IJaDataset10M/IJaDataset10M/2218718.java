package uk.org.ogsadai.client.toolkit.exception;

import uk.org.ogsadai.client.toolkit.Resource;
import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Exception thrown when a resource has not be successfully destroyed.
 *
 * @author The OGSA-DAI Project team
 */
public class ResourceNotDestroyedException extends DAIException {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** The resource. */
    private Resource mResource;

    /**
     * The Constructor.
     * 
     * @param cause
     *            the cause
     * @param resource
     *            the resource
     */
    public ResourceNotDestroyedException(final Resource resource, final Throwable cause) {
        super(ErrorID.CTK_RESOURCE_NOT_DESTROYED, new Object[] { resource.toString() });
        initCause(cause);
        mResource = resource;
    }

    /**
     * Gets the resource.
     * 
     * @return the resource
     */
    public Resource getResource() {
        return mResource;
    }
}
