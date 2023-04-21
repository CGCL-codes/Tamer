package com.sitescape.team.liferay;

import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.impl.RoleImpl;
import com.liferay.portal.service.RoleService;
import com.liferay.portal.util.RoleNames;

/**
 * @author dml
 *
 */
public class MockRoleService implements RoleService {

    private Map<Long, Role> rolesById = new HashMap<Long, Role>();

    private Map<String, Role> rolesByName = new HashMap<String, Role>();

    private SecureRandom random = new SecureRandom();

    public MockRoleService() {
        RoleImpl administrator = new RoleImpl();
        administrator.setName(RoleNames.ADMINISTRATOR);
        addRole(administrator);
    }

    private synchronized void addRole(Role role) {
        role.setRoleId(random.nextLong());
        Role r0 = rolesById.put(role.getRoleId(), role);
        while (r0 != null) {
            rolesById.put(r0.getRoleId(), r0);
            role.setRoleId(random.nextLong());
            r0 = rolesById.put(role.getRoleId(), role);
        }
        rolesByName.put(role.getName(), role);
    }

    private synchronized void deleteRole(Long roleId) {
        Role r = rolesById.remove(roleId);
        rolesByName.remove(r.getName());
    }

    public Role addRole(String name, int type) throws SystemException, PortalException, RemoteException {
        return null;
    }

    public void deleteRole(long roleId) throws SystemException, PortalException, RemoteException {
        deleteRole(new Long(roleId));
    }

    public Role getGroupRole(long companyId, long groupId) throws SystemException, PortalException, RemoteException {
        return null;
    }

    public Role getRole(long roleId) throws SystemException, PortalException, RemoteException {
        return rolesById.get(roleId);
    }

    public Role getRole(long companyId, String name) throws SystemException, PortalException, RemoteException {
        return rolesByName.get(name);
    }

    public List<?> getUserGroupRoles(long userId, long groupId) throws SystemException, PortalException, RemoteException {
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<?> getUserRelatedRoles(long userId, List groups) throws SystemException, PortalException, RemoteException {
        return null;
    }

    public List<?> getUserRoles(long userId) throws SystemException, PortalException, RemoteException {
        return null;
    }

    public boolean hasUserRole(long userId, long companyId, String name, boolean inherited) throws SystemException, PortalException, RemoteException {
        return false;
    }

    public boolean hasUserRoles(long userId, long companyId, String[] names, boolean inherited) throws SystemException, PortalException, RemoteException {
        return false;
    }

    public Role updateRole(long roleId, String name) throws SystemException, PortalException, RemoteException {
        return null;
    }
}
