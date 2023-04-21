package com.liferay.portal.service;

/**
 * <a href="UserGroupRoleServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the <code>com.liferay.portal.service.UserGroupRoleService</code>
 * bean. The static methods of this class calls the same methods of the bean instance.
 * It's convenient to be able to just write one line to call a method on a bean
 * instead of writing a lookup call and a method call.
 * </p>
 *
 * <p>
 * <code>com.liferay.portal.service.UserGroupRoleServiceFactory</code> is responsible
 * for the lookup of the bean.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.UserGroupRoleService
 * @see com.liferay.portal.service.UserGroupRoleServiceFactory
 *
 */
public class UserGroupRoleServiceUtil {

    public static void addUserGroupRoles(long userId, long groupId, long[] roleIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        UserGroupRoleService userGroupRoleService = UserGroupRoleServiceFactory.getService();
        userGroupRoleService.addUserGroupRoles(userId, groupId, roleIds);
    }

    public static void addUserGroupRoles(long[] userIds, long groupId, long roleId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        UserGroupRoleService userGroupRoleService = UserGroupRoleServiceFactory.getService();
        userGroupRoleService.addUserGroupRoles(userIds, groupId, roleId);
    }

    public static void deleteUserGroupRoles(long userId, long groupId, long[] roleIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        UserGroupRoleService userGroupRoleService = UserGroupRoleServiceFactory.getService();
        userGroupRoleService.deleteUserGroupRoles(userId, groupId, roleIds);
    }

    public static void deleteUserGroupRoles(long[] userIds, long groupId, long roleId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        UserGroupRoleService userGroupRoleService = UserGroupRoleServiceFactory.getService();
        userGroupRoleService.deleteUserGroupRoles(userIds, groupId, roleId);
    }
}
