package com.liferay.portal.ejb;

import java.util.List;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Role;

/**
 * <a href="RoleManagerUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.99 $
 *
 */
public class RoleManagerUtil {

    public static List<Role> findAll() throws SystemException {
        RoleManager roleManager = RoleManagerFactory.getManager();
        return roleManager.findAll();
    }

    public static boolean addGroup(java.lang.String roleId, com.liferay.portal.model.Group group) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.addGroup(roleId, group);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean addGroups(java.lang.String roleId, java.util.List groups) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.addGroups(roleId, groups);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portal.model.Role addRole(java.lang.String name) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.addRole(name);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean addUser(java.lang.String roleId, com.liferay.portal.model.User user) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.addUser(roleId, user);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean addUsers(java.lang.String roleId, java.util.List users) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.addUsers(roleId, users);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void checkSystemRoles(java.lang.String companyId) throws com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            roleManager.checkSystemRoles(companyId);
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean deleteGroup(java.lang.String roleId, com.liferay.portal.model.Group group) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.deleteGroup(roleId, group);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void deleteRole(java.lang.String roleId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            roleManager.deleteRole(roleId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean deleteUser(java.lang.String roleId, com.liferay.portal.model.User user) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.deleteUser(roleId, user);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean exists(java.lang.String name) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.exists(name);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean exists(java.lang.String[] names) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.exists(names);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portal.model.Role update(com.liferay.portal.model.Role role) throws com.liferay.portal.SystemException {
        RoleManager roleManager = RoleManagerFactory.getManager();
        return roleManager.update(role);
    }

    public static java.util.List getGroups(java.lang.String roleId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.getGroups(roleId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static java.util.List getGroups(java.lang.String roleId, int begin, int end) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.getGroups(roleId, begin, end);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static int getGroupsSize(java.lang.String roleId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.getGroupsSize(roleId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portal.model.Role getRoleById(java.lang.String roleId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.getRoleById(roleId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portal.model.Role getRoleByName(java.lang.String name) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.getRoleByName(name);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static java.util.List getUsers(java.lang.String roleId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.getUsers(roleId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static java.util.List getUsers(java.lang.String roleId, int begin, int end) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.getUsers(roleId, begin, end);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static int getUsersSize(java.lang.String roleId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.getUsersSize(roleId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean hasRole(java.lang.String userId, java.lang.String roleName) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.hasRole(userId, roleName);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean hasRoles(java.lang.String userId, java.lang.String[] roleNames) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.hasRoles(userId, roleNames);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean isAdministrator(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.isAdministrator(userId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean isGuest(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.isGuest(userId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean isPowerUser(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.isPowerUser(userId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean isUser(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.isUser(userId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean removeGroups(java.lang.String roleId, java.lang.String[] groupIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.removeGroups(roleId, groupIds);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean removeUsers(java.lang.String roleId, java.lang.String[] userIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.removeUsers(roleId, userIds);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portal.model.Role renameRoleById(java.lang.String roleId, java.lang.String newName) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.renameRoleById(roleId, newName);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portal.model.Role renameRoleByName(java.lang.String oldName, java.lang.String newName) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.renameRoleByName(oldName, newName);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void setGroups(java.lang.String roleId, java.lang.String[] groupIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            roleManager.setGroups(roleId, groupIds);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void setUsers(java.lang.String roleId, java.lang.String[] userIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            roleManager.setUsers(roleId, userIds);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean hasAdmin(java.lang.String roleId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            RoleManager roleManager = RoleManagerFactory.getManager();
            return roleManager.hasAdmin(roleId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }
}
