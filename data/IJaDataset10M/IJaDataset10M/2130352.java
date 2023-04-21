package com.liferay.portal.service.permission;

/**
 * <a href="PortletPermission_IW.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class PortletPermission_IW {

    public static PortletPermission_IW getInstance() {
        return _instance;
    }

    public void check(com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker, java.lang.String portletId, java.lang.String actionId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException {
        PortletPermission.check(permissionChecker, portletId, actionId);
    }

    public void check(com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker, java.lang.String plid, java.lang.String portletId, java.lang.String actionId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException {
        PortletPermission.check(permissionChecker, plid, portletId, actionId);
    }

    public boolean contains(com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker, java.lang.String portletId, java.lang.String actionId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException {
        return PortletPermission.contains(permissionChecker, portletId, actionId);
    }

    public boolean contains(com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker, java.lang.String plid, java.lang.String portletId, java.lang.String actionId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException {
        return PortletPermission.contains(permissionChecker, plid, portletId, actionId);
    }

    public boolean contains(com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker, java.lang.String plid, com.liferay.portal.model.Portlet portlet, java.lang.String actionId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException {
        return PortletPermission.contains(permissionChecker, plid, portlet, actionId);
    }

    public java.lang.String getPrimaryKey(java.lang.String plid, java.lang.String portletId) {
        return PortletPermission.getPrimaryKey(plid, portletId);
    }

    private PortletPermission_IW() {
    }

    private static PortletPermission_IW _instance = new PortletPermission_IW();
}
