package uk.org.ogsadai.resource;

import uk.org.ogsadai.exception.DAIUncheckedException;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceType;

/**
 * Raised when there is a problem creating a resource 
 *
 * @author The OGSA-DAI Team.
 */
public class ResourceCreationException extends DAIUncheckedException {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007";

    /**
     * A problem was encountered when creating a resource
     * of a specific type from a template resource.
     *
     * @param resourceID
     *     ID of resource being created.
     * @param templateID
     *     ID of resource template.
     * @param resourceType
     *     Resource type.
     * @param cause
     *     Cause of the problem.
     */
    public ResourceCreationException(ResourceID resourceID, ResourceID templateID, ResourceType resourceType, Throwable cause) {
        super(ErrorID.RESOURCE_FROM_TEMPLATE_CREATION_EXCEPTION, new Object[] { resourceID, templateID, resourceType });
        super.initCause(cause);
    }

    /**
     * A problem was encountered when creating a resource
     * of a specific type from a template resource.
     *
     * @param templateID
     *     ID of resource template.
     * @param resourceType
     *     Resource type.
     * @param cause
     *     Cause of the problem.
     */
    public ResourceCreationException(ResourceType resourceType, ResourceID templateID, Throwable cause) {
        super(ErrorID.RESOURCE_TEMPLATE_CREATION_EXCEPTION, new Object[] { templateID, resourceType });
        super.initCause(cause);
    }

    /**
     * A problem was encountered when creating a resource
     * of a specific type.
     *
     * @param resourceID
     *     ID of resource being created.
     * @param resourceType
     *     Resource type.
     * @param cause
     *     Cause of the problem.
     */
    public ResourceCreationException(ResourceID resourceID, ResourceType resourceType, Throwable cause) {
        super(ErrorID.RESOURCE_CREATION_EXCEPTION, new Object[] { resourceID, resourceType });
        super.initCause(cause);
    }
}
