/**
 * This file is part of ${artifactId}
 */

package ${srcPackage};

import ${srcPackage}.pm.DMOrganizationBean;
import org.equanda.client.EquandaException;
import org.equanda.persistence.SelectorsState;
import org.apache.log4j.Logger;

/**
 * Mediator for User object
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class UserMediator
    extends UserMediatorBase
{
    private static final Logger log = Logger.getLogger( UserMediator.class );
    
    /**
     * Log in to organization
     *
     * @throws EquandaException oops
     */
    public void login()
        throws EquandaException
    {
        DMOrganizationBean org = entity.getOrganization();
        SelectorsState.setFilter( "Organization", org == null ? null : org.getReference() );
        SelectorsState.setFilter( "OrganizationOne", org == null ? null : "o.organization.reference='"+org.getReference()+"'" );
        SelectorsState.setFilter( "OrganizationMul", org == null ? null : "'"+org.getReference()+"' in o.organizations.reference" );
    }

    /**
     * Log out from organization
     * 
     * @throws EquandaException oops
     */
    public void logout()
        throws EquandaException
    {
        SelectorsState.setFilter( "Organization", null );
    }
}

