package com.liferay.portal.service.persistence;

/**
 * <a href="PermissionUserFinderUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PermissionUserFinderUtil {

    public static int countByOrgGroupPermissions(long companyId, java.lang.String name, java.lang.String primKey, java.lang.String actionId) throws com.liferay.portal.SystemException {
        return getFinder().countByOrgGroupPermissions(companyId, name, primKey, actionId);
    }

    public static int countByPermissionAndRole(long companyId, long groupId, java.lang.String name, java.lang.String primKey, java.lang.String actionId, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, java.lang.String emailAddress, boolean andOperator) throws com.liferay.portal.SystemException {
        return getFinder().countByPermissionAndRole(companyId, groupId, name, primKey, actionId, firstName, middleName, lastName, emailAddress, andOperator);
    }

    public static int countByUserAndOrgGroupPermission(long companyId, java.lang.String name, java.lang.String primKey, java.lang.String actionId, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, java.lang.String emailAddress, boolean andOperator) throws com.liferay.portal.SystemException {
        return getFinder().countByUserAndOrgGroupPermission(companyId, name, primKey, actionId, firstName, middleName, lastName, emailAddress, andOperator);
    }

    public static java.util.List<com.liferay.portal.model.User> findByPermissionAndRole(long companyId, long groupId, java.lang.String name, java.lang.String primKey, java.lang.String actionId, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, java.lang.String emailAddress, boolean andOperator, int start, int end) throws com.liferay.portal.SystemException {
        return getFinder().findByPermissionAndRole(companyId, groupId, name, primKey, actionId, firstName, middleName, lastName, emailAddress, andOperator, start, end);
    }

    public static java.util.List<com.liferay.portal.model.User> findByUserAndOrgGroupPermission(long companyId, java.lang.String name, java.lang.String primKey, java.lang.String actionId, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, java.lang.String emailAddress, boolean andOperator, int start, int end) throws com.liferay.portal.SystemException {
        return getFinder().findByUserAndOrgGroupPermission(companyId, name, primKey, actionId, firstName, middleName, lastName, emailAddress, andOperator, start, end);
    }

    public static PermissionUserFinder getFinder() {
        return _finder;
    }

    public void setFinder(PermissionUserFinder finder) {
        _finder = finder;
    }

    private static PermissionUserFinder _finder;
}
