package edu.mit.osidimpl.authorization.shared;

import org.osidx.registry.TypeElement;
import org.osidx.registry.OsidTypeRegistry;
import org.osidx.registry.oid.OsidTypes;

/**
 *  <p>
 *  Defines a convenience class for managing Authorization Qualifier types.
 *  </p><p>
 *  CVS $Id: AuthorizationQualifierType.java,v 1.3 2006/04/13 20:15:00 tom Exp $
 *  </p>
 *  
 *  @author Tom Coppeto
 *  @version $OSID: 2.0$ $Revision: 1.3 $
 *  @see org.osid.shared.Type
 */
public class AuthorizationQualifierType {

    /** default authentication type */
    public static final TypeElement ANY = OsidTypeRegistry.getTypeElement(OsidTypes.ANY_AUTHORIZATION_QUALIFIER_TYPE);

    /**
     *  Gets the list of format types standardized in this implementation
     *  suite.
     *
     *  @return a TypeIterator containg the format Types
     */
    public static org.osid.shared.TypeIterator getTypes() throws org.osid.shared.SharedException {
        java.util.Vector vector = new java.util.Vector();
        vector.addElement(ANY.getType());
        return (TypeIterator) (new TypeIterator(vector));
    }
}
