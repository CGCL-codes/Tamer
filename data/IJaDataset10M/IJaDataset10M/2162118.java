package com.liferay.portal.service.persistence;

/**
 * <a href="PermissionFinderUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PermissionFinderUtil {

    public static boolean containsPermissions_2(java.util.List<com.liferay.portal.model.Permission> permissions, long userId, java.util.List<com.liferay.portal.model.Group> groups, long groupId) throws com.liferay.portal.SystemException {
        return getFinder().containsPermissions_2(permissions, userId, groups, groupId);
    }

    public static boolean containsPermissions_4(java.util.List<com.liferay.portal.model.Permission> permissions, long userId, java.util.List<com.liferay.portal.model.Group> groups, java.util.List<com.liferay.portal.model.Role> roles) throws com.liferay.portal.SystemException {
        return getFinder().containsPermissions_4(permissions, userId, groups, roles);
    }

    public static int countByGroupsPermissions(java.util.List<com.liferay.portal.model.Permission> permissions, java.util.List<com.liferay.portal.model.Group> groups) throws com.liferay.portal.SystemException {
        return getFinder().countByGroupsPermissions(permissions, groups);
    }

    public static int countByGroupsRoles(java.util.List<com.liferay.portal.model.Permission> permissions, java.util.List<com.liferay.portal.model.Group> groups) throws com.liferay.portal.SystemException {
        return getFinder().countByGroupsRoles(permissions, groups);
    }

    public static int countByRolesPermissions(java.util.List<com.liferay.portal.model.Permission> permissions, java.util.List<com.liferay.portal.model.Role> roles) throws com.liferay.portal.SystemException {
        return getFinder().countByRolesPermissions(permissions, roles);
    }

    public static int countByUserGroupRole(java.util.List<com.liferay.portal.model.Permission> permissions, long userId, long groupId) throws com.liferay.portal.SystemException {
        return getFinder().countByUserGroupRole(permissions, userId, groupId);
    }

    public static int countByUsersPermissions(java.util.List<com.liferay.portal.model.Permission> permissions, long userId) throws com.liferay.portal.SystemException {
        return getFinder().countByUsersPermissions(permissions, userId);
    }

    public static int countByUsersRoles(java.util.List<com.liferay.portal.model.Permission> permissions, long userId) throws com.liferay.portal.SystemException {
        return getFinder().countByUsersRoles(permissions, userId);
    }

    public static java.util.List<com.liferay.portal.model.Permission> findByA_R(java.lang.String actionId, long[] resourceIds) throws com.liferay.portal.SystemException {
        return getFinder().findByA_R(actionId, resourceIds);
    }

    public static java.util.List<com.liferay.portal.model.Permission> findByG_R(long groupId, long resourceId) throws com.liferay.portal.SystemException {
        return getFinder().findByG_R(groupId, resourceId);
    }

    public static java.util.List<com.liferay.portal.model.Permission> findByR_R(long roleId, long resourceId) throws com.liferay.portal.SystemException {
        return getFinder().findByR_R(roleId, resourceId);
    }

    public static java.util.List<com.liferay.portal.model.Permission> findByU_R(long userId, long resourceId) throws com.liferay.portal.SystemException {
        return getFinder().findByU_R(userId, resourceId);
    }

    public static java.util.List<com.liferay.portal.model.Permission> findByO_G_R(long organizationId, long groupId, long resourceId) throws com.liferay.portal.SystemException {
        return getFinder().findByO_G_R(organizationId, groupId, resourceId);
    }

    public static java.util.List<com.liferay.portal.model.Permission> findByU_A_R(long userId, java.lang.String[] actionIds, long resourceId) throws com.liferay.portal.SystemException {
        return getFinder().findByU_A_R(userId, actionIds, resourceId);
    }

    public static java.util.List<com.liferay.portal.model.Permission> findByG_C_N_S_P(long groupId, long companyId, java.lang.String name, int scope, java.lang.String primKey) throws com.liferay.portal.SystemException {
        return getFinder().findByG_C_N_S_P(groupId, companyId, name, scope, primKey);
    }

    public static java.util.List<com.liferay.portal.model.Permission> findByU_C_N_S_P(long userId, long companyId, java.lang.String name, int scope, java.lang.String primKey) throws com.liferay.portal.SystemException {
        return getFinder().findByU_C_N_S_P(userId, companyId, name, scope, primKey);
    }

    public static PermissionFinder getFinder() {
        return _finder;
    }

    public void setFinder(PermissionFinder finder) {
        _finder = finder;
    }

    private static PermissionFinder _finder;
}
