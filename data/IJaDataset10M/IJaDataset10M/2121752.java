package edu.mit.osidimpl.authorization.acl;

import edu.mit.osidimpl.authorization.shared.*;

/**
 *  <p>
 *  Implements Function for this bully-ing implementation.
 *  </p><p>
 *  CVS $Id: Function.java,v 1.1 2005/08/25 15:45:53 tom Exp $
 *  </p>
 *  
 *  @author  Tom Coppeto
 *  @version $OSID: 2.0$ $Revision: 1.1 $
 *  @see     org.osid.authorization.Function
 */
public class Function implements org.osid.authorization.Function {

    private org.osid.shared.Id id;

    private org.osid.shared.Type type;

    private String referenceName;

    private String description;

    /**
     *  Constructor.
     */
    public Function(String referenceName, org.osid.shared.Type type, String description) throws org.osid.authorization.AuthorizationException {
        if ((referenceName == null) || (type == null)) {
            throw new org.osid.authorization.AuthorizationException(org.osid.authorization.AuthorizationException.NULL_ARGUMENT);
        }
        this.referenceName = referenceName;
        this.type = type;
        this.description = description;
        id = new edu.mit.osidimpl.shared.Id(referenceName);
    }

    /**
     *  Get the Id for this Function.
     *
     *  @return org.osid.shared.Id
     *  @throws org.osid.authorization.AuthorizationException 
     */
    public org.osid.shared.Id getId() throws org.osid.authorization.AuthorizationException {
        return (this.id);
    }

    /**
     *  Get the permanent reference name for this Function.
     *
     *  @return String
     *  @throws org.osid.authorization.AuthorizationException
     */
    public String getReferenceName() throws org.osid.authorization.AuthorizationException {
        return (this.referenceName);
    }

    /**
     *  Get the description for this Function.
     *
     *  @return String
     *  @throws org.osid.authorization.AuthorizationException 
     */
    public String getDescription() throws org.osid.authorization.AuthorizationException {
        return (this.description);
    }

    /**
     *  Get the FunctionType for this Function.
     *
     *  @return org.osid.shared.Type
     *  @throws org.osid.authorization.AuthorizationException 
     */
    public org.osid.shared.Type getFunctionType() throws org.osid.authorization.AuthorizationException {
        return (this.type);
    }

    /**
     *  Get the QualifierHierarchyId for this Function.
     *
     *  @return org.osid.shared.Id
     *  @throws org.osid.authorization.AuthorizationException
     */
    public org.osid.shared.Id getQualifierHierarchyId() throws org.osid.authorization.AuthorizationException {
        return (null);
    }

    /**
     *  Update the description for this Function.
     *
     *  @param description
     *  @throws org.osid.authorization.AuthorizationException
     */
    public void updateDescription(String description) throws org.osid.authorization.AuthorizationException {
        this.description = description;
    }
}
