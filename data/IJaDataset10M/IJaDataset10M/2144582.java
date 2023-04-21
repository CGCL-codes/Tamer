package uk.org.ogsadai.resource.dataresource.xmldb;

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Exception thrown when attempting to access an unknown XMLDB collection.
 * <p>
 * Associated with error code:
 * <code>uk.org.ogsadai.XMLDB_UNKNOWN_COLLECTION_ERROR</code>.
 * 
 * @author The OGSA-DAI Project Team
 */
public class XMLDBUnknownCollectionException extends DAIException {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2007.";

    /**
     * Constructor.
     * 
     * @param name
     *            Name of unknown collection.
     * @param user
     *            User name used to try to access collection.
     */
    public XMLDBUnknownCollectionException(String name, String user) {
        super(ErrorID.XMLDB_UNKNOWN_COLLECTION_ERROR, new Object[] { name, user });
    }
}
