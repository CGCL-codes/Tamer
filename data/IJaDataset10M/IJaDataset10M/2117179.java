package com.dotmarketing.factories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.cms.factories.PublicCompanyFactory;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.liferay.portal.NoSuchRoleException;
import com.liferay.portal.ejb.GroupLocalManagerUtil;
import com.liferay.portal.ejb.RoleLocalManagerUtil;
import com.liferay.portal.ejb.UserLocalManagerUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;

/**
 * 
 * @author maria
 */
public class RoleFactory {

    private static Role _cmsAdministratorRole = null;

    private static Role _cmsAnonymousRole = null;

    private static Role _cmsOwnerRole = null;

    private static Role _loggedInSiteUser = null;

    public static Role getRoleByName(String roleName) {
        Role role = new Role();
        try {
            role = RoleLocalManagerUtil.getRoleByName(com.dotmarketing.cms.factories.PublicCompanyFactory.getDefaultCompany().getCompanyId(), roleName);
        } catch (Exception e) {
            Logger.warn(RoleFactory.class, "getRoleByName: Errors getting role: " + roleName);
        }
        return role;
    }

    public static Role getRoleById(long roleId) throws NoSuchRoleException {
        String sRoleId = new Long(roleId).toString();
        return getRoleById(sRoleId);
    }

    public static Role getRoleById(String roleId) throws NoSuchRoleException {
        Role role = new Role();
        try {
            role = RoleLocalManagerUtil.getRoleById(roleId);
        } catch (NoSuchRoleException e) {
            Logger.warn(RoleFactory.class, "getRoleById: Errors getting role: " + roleId, e);
            throw e;
        } catch (Exception e) {
            Logger.warn(RoleFactory.class, "getRoleById: Errors getting role: " + roleId, e);
        }
        return role;
    }

    @SuppressWarnings("unchecked")
    public static List<Role> getAllRolesForUser(String userId) {
        RoleCacheImpl rc = (RoleCacheImpl) CacheLocator.getRoleCache();
        List<Role> myRoles = rc.getRolesFromCache(userId);
        if (myRoles != null) {
            return myRoles;
        }
        List<Group> myGroups = null;
        try {
            myRoles = UserLocalManagerUtil.getRoles(userId);
        } catch (Exception e) {
        }
        try {
            myGroups = UserLocalManagerUtil.getGroups(userId);
        } catch (Exception e) {
        }
        Iterator i = myGroups.iterator();
        while (i.hasNext()) {
            Group g = (Group) i.next();
            try {
                List<Role> groupRoles = GroupLocalManagerUtil.getRoles(g.getGroupId());
                myRoles.addAll(groupRoles);
            } catch (Exception e) {
            }
        }
        rc.addToRoleCache(userId, myRoles);
        return myRoles;
    }

    @SuppressWarnings("unchecked")
    public static Set<User> getUsers(String roleId) {
        Set<User> users = new HashSet<User>();
        try {
            users.addAll(RoleLocalManagerUtil.getUsers(roleId));
        } catch (Exception e) {
        }
        try {
            List<Group> groups = (List<Group>) RoleLocalManagerUtil.getGroups(roleId);
            for (Group group : groups) {
                users.addAll(GroupLocalManagerUtil.getUsers(group.getGroupId()));
            }
        } catch (Exception e) {
        }
        return users;
    }

    /**
	 * The method will look at a Set<Role> and return all the distinct users who have those roles.   
	 * @param roles to iterate over
	 * @return
	 */
    public static Set<User> getUsers(Set<Role> roles) {
        Set<User> users = new HashSet<User>();
        ArrayList<String> userIDs = new ArrayList<String>();
        for (Role role : roles) {
            Set<User> usersWithRole = RoleFactory.getUsers(role.getRoleId());
            for (User user : usersWithRole) {
                if (!userIDs.contains(user.getUserId())) {
                    userIDs.add(user.getUserId());
                    users.add(user);
                }
            }
        }
        return users;
    }

    /**
     * Caches and Returns the CMS Administrator Role. The method gets the role name from the
     * dotmarketing-config.properties variable CMS_ADMINISTRATOR_ROLE.
     * This method cache the Role so subsequent called to the method 
     * will not go to the database 
     *
     * @return  The CMS Administrator Liferay Role
     * @see Role
     */
    public static Role getCMSAdministratorRole() {
        if (_cmsAdministratorRole == null) {
            try {
                String companyId = PublicCompanyFactory.getDefaultCompanyId();
                _cmsAdministratorRole = RoleLocalManagerUtil.getRoleByName(companyId, Config.getStringProperty("CMS_ADMINISTRATOR_ROLE"));
            } catch (Exception e) {
                Logger.error(RoleFactory.class, "Can't find CMS_ADMINISTRATOR_ROLE !!!", e);
            }
        }
        return _cmsAdministratorRole;
    }

    /**
     * Caches and Returns the CMS Anonymous Role. The method gets the role name from the
     * dotmarketing-config.properties variable CMS_ANONYMOUS_ROLE.
     * This method cache the Role so subsequent called to the method 
     * will not go to the database 
     *
     * @return  The CMS Anonymous cms role
     * @see Role
     */
    public static Role getCMSAnonymousRole() {
        if (_cmsAnonymousRole == null) {
            try {
                String companyId = PublicCompanyFactory.getDefaultCompanyId();
                _cmsAnonymousRole = RoleLocalManagerUtil.getRoleByName(companyId, Config.getStringProperty("CMS_ANONYMOUS_ROLE"));
            } catch (Exception e) {
                Logger.error(RoleFactory.class, "Can't find CMS_ANONYMOUS_ROLE !!!", e);
            }
        }
        return _cmsAnonymousRole;
    }

    /**
     * Caches and Returns the LoggedIn Site User Role. The method gets the role name from the
     * dotmarketing-config.properties variable CMS_LOGGED_IN_SITE_USER_ROLE.
     * This method cache the Role so subsequent called to the method 
     * will not go to the database 
     *
     * @return  Logged in site user cms role
     * @see Role
     */
    public static Role getLoggedInSiteUserRole() {
        if (_loggedInSiteUser == null) {
            try {
                String companyId = PublicCompanyFactory.getDefaultCompanyId();
                _loggedInSiteUser = RoleLocalManagerUtil.getRoleByName(companyId, Config.getStringProperty("CMS_LOGGED_IN_SITE_USER_ROLE"));
            } catch (Exception e) {
                Logger.error(RoleFactory.class, "Can't find CMS_LOGGED_IN_SITE_USER_ROLE !!!", e);
            }
        }
        return _loggedInSiteUser;
    }

    /**
     * Caches and Returns the CMS Anonymous Role. The method gets the role name from the
     * dotmarketing-config.properties variable CMS_ANONYMOUS_ROLE.
     * This method cache the Role so subsequent called to the method 
     * will not go to the database 
     *
     * @return  The CMS Owner cms role
     * @see Role
     */
    public static Role getCMSOwnerRole() {
        if (_cmsOwnerRole == null) {
            try {
                String companyId = PublicCompanyFactory.getDefaultCompanyId();
                _cmsOwnerRole = RoleLocalManagerUtil.getRoleByName(companyId, Config.getStringProperty("CMS_OWNER_ROLE"));
            } catch (Exception e) {
                Logger.error(RoleFactory.class, "Can't find CMS_Owner_ROLE !!!", e);
            }
        }
        return _cmsOwnerRole;
    }

    public static boolean userHasRole(User user, String roleName) {
        if (user != null) {
            Iterator rolesIt = getAllRolesForUser(user.getUserId()).iterator();
            while (rolesIt.hasNext()) {
                Role role = (Role) rolesIt.next();
                if (role.getName().equals(roleName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
