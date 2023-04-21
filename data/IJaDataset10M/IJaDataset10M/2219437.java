package com.liferay.portal.service.permission;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.bean.BeanLocatorUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.model.Account;

/**
 * <a href="AccountPermissionUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AccountPermissionUtil {

    public static void check(PermissionChecker permissionChecker, long accountId, String actionId) throws PortalException, SystemException {
        getAccountPermission().check(permissionChecker, accountId, actionId);
    }

    public static void check(PermissionChecker permissionChecker, Account account, String actionId) throws PortalException, SystemException {
        getAccountPermission().check(permissionChecker, account, actionId);
    }

    public static boolean contains(PermissionChecker permissionChecker, long accountId, String actionId) throws PortalException, SystemException {
        return getAccountPermission().contains(permissionChecker, accountId, actionId);
    }

    public static boolean contains(PermissionChecker permissionChecker, Account account, String actionId) throws PortalException, SystemException {
        return getAccountPermission().contains(permissionChecker, account, actionId);
    }

    public static AccountPermission getAccountPermission() {
        return _getUtil()._accountPermission;
    }

    public void setAccountPermission(AccountPermission accountPermission) {
        _accountPermission = accountPermission;
    }

    private static AccountPermissionUtil _getUtil() {
        if (_util == null) {
            _util = (AccountPermissionUtil) BeanLocatorUtil.locate(_UTIL);
        }
        return _util;
    }

    private static final String _UTIL = AccountPermissionUtil.class.getName();

    private static AccountPermissionUtil _util;

    private AccountPermission _accountPermission;
}
