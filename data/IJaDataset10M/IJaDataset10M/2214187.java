package uk.org.ogsadai.persistence.resource;

import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.persistence.PersistenceException;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceType;

/**
 * Exception for when a persistence component is given a
 * resource type it cannot handle.
 * 
 * @author The OGSA-DAI Project Team
 */
public class UnsupportedResourceTypeException extends PersistenceException {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2007.";

    /**
     * Constructor.
     *
     * @param resourceType
     *     Resource type.
     * @param resourceID
     *     Resource ID.
     */
    public UnsupportedResourceTypeException(ResourceType resourceType, ResourceID resourceID) {
        super(ErrorID.PERSISTENCE_UNSUPPORTED_RESOURCE_TYPE, new Object[] { resourceType, resourceID });
    }
}
