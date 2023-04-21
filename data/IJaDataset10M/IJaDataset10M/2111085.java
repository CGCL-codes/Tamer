package net.sf.jlue.service;

import java.util.List;
import net.sf.jlue.security.Role;

/** 
 * Service logic level Role processing interface definition.
 * 
 * @see net.sf.jlue.service
 * 
 * @author Sun Yat-ton (Mail:PubTlk@Hotmail.com)
 * @version 1.00 2007-2-4
 */
public interface RoleService extends Service {

    /**
	 * Get a Role object by <code>id</code>.
	 * 
	 * @param id Role id.
	 * @return Return Role object.
	 */
    public Role getRole(String id);

    /**
	 * Get the list of Roles.
	 * 
	 * @return Return list of Roles.
	 */
    public List getRoles();

    /**
	 * Get a role list of an organization by <code>organ</code>.
	 * 
	 * @param organ Organization id.
	 * @return Return Roles of organization.
	 */
    public List getRolesByOwner(String organ);

    /**
	 * Get Actions of Role.
	 * 
	 * @param id Role id.
	 * @return Return Actions list of Role.
	 */
    public List getActions(String id);
}
