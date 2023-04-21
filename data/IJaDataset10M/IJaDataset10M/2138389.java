package net.jforum.dao;

import net.jforum.entities.User;
import net.jforum.security.Role;
import net.jforum.security.RoleCollection;
import net.jforum.security.RoleValueCollection;

/**
 * @author Rafael Steil
 * @version $Id: GroupSecurityDAO.java,v 1.2 2007/08/24 23:11:35 rafaelsteil Exp $
 */
public interface GroupSecurityDAO {

    /**
	 * Deletes all roles related to a forum
	 * @param forumId
	 */
    public void deleteForumRoles(int forumId);

    /**
	 * Delete all roles from a specific group
	 * @param groupId ID of the group
	 **/
    public void deleteAllRoles(int groupId);

    /**
	 * Adds a new role
	 * @param groupId Group id the role should be associated
	 * */
    public void addRole(int groupId, Role role);

    /**
	 * @param id
	 * @param role
	 * @param rvc
	 */
    public void addRoleValue(int id, Role role, RoleValueCollection rvc);

    /**
	 * @param id
	 * @param roleName
	 * @param roleValues
	 */
    public void addRole(int id, Role role, RoleValueCollection roleValues);

    /**
	 * @param groupId int
	 * @return RoleCollection
	 */
    public RoleCollection loadRoles(int groupId);

    public RoleCollection loadRolesByUserGroups(User user);
}
