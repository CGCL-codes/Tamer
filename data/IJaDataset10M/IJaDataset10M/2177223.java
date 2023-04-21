package com.liferay.portal.service.permission;

/**
 * <a href="PortalPermission_IW.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class PortalPermission_IW {

    public static PortalPermission_IW getInstance() {
        return _instance;
    }

    public void check(com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker, java.lang.String actionId) throws com.liferay.portal.security.auth.PrincipalException {
        PortalPermission.check(permissionChecker, actionId);
    }

    public boolean contains(com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker, java.lang.String actionId) {
        return PortalPermission.contains(permissionChecker, actionId);
    }

    private PortalPermission_IW() {
    }

    private static PortalPermission_IW _instance = new PortalPermission_IW();
}
