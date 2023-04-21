package com.liferay.portal.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.PermissionServiceUtil;
import java.rmi.RemoteException;

/**
 * <a href="PermissionServiceSoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides a SOAP utility for the <code>com.liferay.portal.service.PermissionServiceUtil</code>
 * service utility. The static methods of this class calls the same methods of the
 * service utility. However, the signatures are different because it is difficult
 * for SOAP to support certain types.
 * </p>
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a <code>java.util.List</code>, that
 * is translated to an array of <code>com.liferay.portal.model.PermissionSoap</code>.
 * If the method in the service utility returns a <code>com.liferay.portal.model.Permission</code>,
 * that is translated to a <code>com.liferay.portal.model.PermissionSoap</code>.
 * Methods that SOAP cannot safely wire are skipped.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform compatible.
 * SOAP allows different languages like Java, .NET, C++, PHP, and even Perl, to
 * call the generated services. One drawback of SOAP is that it is slow because
 * it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/tunnel-web/secure/axis.
 * Set the property <code>tunnel.servlet.hosts.allowed</code> in portal.properties
 * to configure security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.PermissionServiceUtil
 * @see com.liferay.portal.service.http.PermissionServiceHttp
 * @see com.liferay.portal.service.model.PermissionSoap
 *
 */
public class PermissionServiceSoap {

    public static void checkPermission(long groupId, java.lang.String name, java.lang.String primKey) throws RemoteException {
        try {
            PermissionServiceUtil.checkPermission(groupId, name, primKey);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static boolean hasGroupPermission(long groupId, java.lang.String actionId, long resourceId) throws RemoteException {
        try {
            boolean returnValue = PermissionServiceUtil.hasGroupPermission(groupId, actionId, resourceId);
            return returnValue;
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static boolean hasUserPermission(long userId, java.lang.String actionId, long resourceId) throws RemoteException {
        try {
            boolean returnValue = PermissionServiceUtil.hasUserPermission(userId, actionId, resourceId);
            return returnValue;
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static boolean hasUserPermissions(long userId, long groupId, java.lang.String actionId, long[] resourceIds, com.liferay.portal.kernel.security.permission.PermissionCheckerBag permissionCheckerBag) throws RemoteException {
        try {
            boolean returnValue = PermissionServiceUtil.hasUserPermissions(userId, groupId, actionId, resourceIds, permissionCheckerBag);
            return returnValue;
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void setGroupPermissions(long groupId, java.lang.String[] actionIds, long resourceId) throws RemoteException {
        try {
            PermissionServiceUtil.setGroupPermissions(groupId, actionIds, resourceId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void setGroupPermissions(java.lang.String className, java.lang.String classPK, long groupId, java.lang.String[] actionIds, long resourceId) throws RemoteException {
        try {
            PermissionServiceUtil.setGroupPermissions(className, classPK, groupId, actionIds, resourceId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void setOrgGroupPermissions(long organizationId, long groupId, java.lang.String[] actionIds, long resourceId) throws RemoteException {
        try {
            PermissionServiceUtil.setOrgGroupPermissions(organizationId, groupId, actionIds, resourceId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void setRolePermission(long roleId, long groupId, java.lang.String name, int scope, java.lang.String primKey, java.lang.String actionId) throws RemoteException {
        try {
            PermissionServiceUtil.setRolePermission(roleId, groupId, name, scope, primKey, actionId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void setRolePermissions(long roleId, long groupId, java.lang.String[] actionIds, long resourceId) throws RemoteException {
        try {
            PermissionServiceUtil.setRolePermissions(roleId, groupId, actionIds, resourceId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void setUserPermissions(long userId, long groupId, java.lang.String[] actionIds, long resourceId) throws RemoteException {
        try {
            PermissionServiceUtil.setUserPermissions(userId, groupId, actionIds, resourceId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void unsetRolePermission(long roleId, long groupId, long permissionId) throws RemoteException {
        try {
            PermissionServiceUtil.unsetRolePermission(roleId, groupId, permissionId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void unsetRolePermission(long roleId, long groupId, java.lang.String name, int scope, java.lang.String primKey, java.lang.String actionId) throws RemoteException {
        try {
            PermissionServiceUtil.unsetRolePermission(roleId, groupId, name, scope, primKey, actionId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void unsetRolePermissions(long roleId, long groupId, java.lang.String name, int scope, java.lang.String actionId) throws RemoteException {
        try {
            PermissionServiceUtil.unsetRolePermissions(roleId, groupId, name, scope, actionId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void unsetUserPermissions(long userId, long groupId, java.lang.String[] actionIds, long resourceId) throws RemoteException {
        try {
            PermissionServiceUtil.unsetUserPermissions(userId, groupId, actionIds, resourceId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    private static Log _log = LogFactoryUtil.getLog(PermissionServiceSoap.class);
}
