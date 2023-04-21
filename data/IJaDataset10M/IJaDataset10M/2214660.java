package org.kopsox.services.security;

import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

/**
 * This interface extends the java.security.Principal interface with some 'Role'-functionality
 * 
 * @author Konrad Renner
 *
 */
public interface KopsoxPrincipal extends Principal, Serializable {

    /**
	 * Checks if the user has the given role
	 * 
	 * @param role
	 * @return boolean
	 * @author rpri182 - RenK
	 */
    public boolean hasRole(Role role);

    /**
	 * Gets the physical/real name of the user
	 * 
	 * @return String
	 * @author rpri182 - RenK
	 */
    public String getPhysicalName();

    /**
	 * Adds a role. Returns true if the role is added
	 * 
	 * @param role
	 * @return boolean
	 * @author rpri182 - RenK
	 */
    public boolean addRole(Role role);

    /**
	 * Adds roles. Returns true if the roles are added
	 * 
	 * @param roles
	 * @return boolean
	 * @author rpri182 - RenK
	 */
    public boolean addRoles(Set<Role> roles);

    /**
	 * Removes a role. Returns true if the role is removed
	 * 
	 * @param role
	 * @return boolean
	 * @author rpri182 - RenK
	 */
    public boolean removeRole(Role role);

    /**
	 * Removes roles. Returns true if the roles are removed
	 * 
	 * @param roles
	 * @return boolean
	 * @author rpri182 - RenK
	 */
    public boolean removeRoles(Set<Role> roles);
}
