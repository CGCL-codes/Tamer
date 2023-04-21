package net.cw.talos.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  Represents a role permissions.
 *
 * @author     <a href="mailto: nowicki@commworld.de">Pawel Nowicki</a>
 * @version    $Revision: 1.2 $
 */
public class Credential {

    private Role role;

    private Permissions permissions;

    /**
	 *  Constructor for the Credential object
	 *
	 * @param  role         Description of the Parameter
	 * @param  permissions  Description of the Parameter
	 */
    public Credential(Role role, Permissions permissions) {
        super();
        this.permissions = permissions;
        this.role = role;
    }

    /**
	 *  Gets the permissions attribute of the Credential object
	 *
	 * @return    The permissions value
	 */
    public Permissions getPermissions() {
        return permissions;
    }

    /**
	 *  Gets the role attribute of the Credential object
	 *
	 * @return    The role value
	 */
    public Role getRole() {
        return role;
    }

    /**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
    public String toString() {
        return new ToStringBuilder(this).append("role", role).append("permissions", permissions).toString();
    }
}
