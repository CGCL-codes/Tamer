package edu.mit.osidimpl.agent.shared;

import org.osidx.registry.TypeElement;
import org.osidx.registry.OsidTypeRegistry;
import org.osidx.registry.oid.OsidTypes;

/**
 *  <p>
 *  Defines a convenience class for managing Group Types.
 *  </p><p>
 *  CVS $Id: GroupType.java,v 1.3 2006/04/13 20:14:59 tom Exp $
 *  </p>
 *  
 *  @author Tom Coppeto
 *  @version $OSID: 2.0$ $Revision: 1.3 $
 *  @see org.osid.shared.Type
 */
public class GroupType {

    public static final TypeElement DEFAULT = OsidTypeRegistry.getTypeElement(OsidTypes.DEFAULT_GROUP_TYPE);

    public static final TypeElement PROPERTY = OsidTypeRegistry.getTypeElement(OsidTypes.DEFAULT_GROUP_PROPERTY_TYPE);

    public static final TypeElement SEARCH = OsidTypeRegistry.getTypeElement(OsidTypes.CQL_SEARCH_TYPE);

    /**
     *  Gets the list of group types standardized in this implementation
     *  suite.
     *
     *  @return a TypeIterator containg the group Types
     */
    public static org.osid.shared.TypeIterator getTypes() throws org.osid.shared.SharedException {
        java.util.Vector vector = new java.util.Vector();
        vector.addElement(DEFAULT.getType());
        return (TypeIterator) (new TypeIterator(vector));
    }

    /**
     *  Gets the list of group Property types standardized in this implementation
     *  suite.
     *
     *  @return a TypeIterator containg the Property Types
     */
    public static org.osid.shared.TypeIterator getPropertyTypes() throws org.osid.shared.SharedException {
        java.util.Vector vector = new java.util.Vector();
        vector.addElement(PROPERTY.getType());
        return (TypeIterator) (new TypeIterator(vector));
    }

    /**
     *  Gets the list of group search types standardized in this implementation
     *  suite.
     *
     *  @return a TypeIterator containg the search Types
     */
    public static org.osid.shared.TypeIterator getSearchTypes() throws org.osid.shared.SharedException {
        java.util.Vector vector = new java.util.Vector();
        vector.addElement(SEARCH.getType());
        return (TypeIterator) (new TypeIterator(vector));
    }
}
