package com.liferay.portal.service.permission;

/**
 * <a href="UserPermission_IW.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class UserPermission_IW {

    public static UserPermission_IW getInstance() {
        return _instance;
    }

    public void check(com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker, java.lang.String userId, java.lang.String organizationId, java.lang.String locationId, java.lang.String actionId) throws com.liferay.portal.security.auth.PrincipalException {
        UserPermission.check(permissionChecker, userId, organizationId, locationId, actionId);
    }

    public boolean contains(com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker, java.lang.String userId, java.lang.String organizationId, java.lang.String locationId, java.lang.String actionId) {
        return UserPermission.contains(permissionChecker, userId, organizationId, locationId, actionId);
    }

    private UserPermission_IW() {
    }

    private static UserPermission_IW _instance = new UserPermission_IW();
}
