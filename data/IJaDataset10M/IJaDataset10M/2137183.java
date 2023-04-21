package it.hotel.model.role.manager;

import it.hotel.model.abstrakt.manager.AbstractService;
import it.hotel.model.permission.Permission;
import it.hotel.model.permission.manager.IPermissionDAO;
import it.hotel.model.role.Role;
import it.hotel.system.SystemUtils;
import it.hotel.system.exception.SystemException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Servizio di gestione dei ruoli.
 * @version 1.0 
 */
public class RoleManager extends AbstractService implements IRoleManager {

    /**
	 * @throws
	 */
    public void init() throws Exception {
        this.loadAuthConfiguration();
        SystemUtils.getLogger().config(this.getClass().getName() + ": inizializzati " + _roles.size() + " ruoli e " + _permissions.size() + " permessi.");
    }

    /**
	 * 
	 * @throws SystemException
	 */
    private void loadAuthConfiguration() throws SystemException {
        try {
            _roles = this.getRoleDAO().loadRoles();
            _permissions = this.getPermissionDAO().loadPermissions();
        } catch (Throwable t) {
            throw new SystemException("Errore in caricamento ruoli e permessi", t);
        }
    }

    public Map getRoles() {
        return _roles;
    }

    public Role getRole(String roleName) {
        return (Role) this._roles.get(roleName);
    }

    /**
	 * @throws
	 */
    public void removeRole(Role role) throws SystemException {
        try {
            this.getRoleDAO().deleteRole(role);
            _roles.remove(role.getName());
        } catch (Throwable t) {
            SystemUtils.logThrowable(t, this, "removeRole");
            throw new SystemException("Errore in rimozione Ruolo", t);
        }
    }

    /**
	 * @throws
	 */
    public void updateRole(Role role) throws SystemException {
        try {
            this.getRoleDAO().updateRole(role);
            _roles.put(role.getName(), role);
        } catch (Throwable t) {
            SystemUtils.logThrowable(t, this, "updateRole");
            throw new SystemException("Errore in aggiornamento Ruolo", t);
        }
    }

    /**
	 * @throws
	 */
    public void addRole(Role role) throws SystemException {
        SystemUtils.getLogger().finest("Invocato");
        try {
            this.getRoleDAO().addRole(role);
            _roles.put(role.getName(), role);
        } catch (Throwable t) {
            SystemUtils.logThrowable(t, this, "addRole");
            throw new SystemException("Errore in aggiunta Ruolo", t);
        }
    }

    public Set getPermissions() {
        return _permissions;
    }

    /**
	 * @param
	 * @throws
	 */
    public void removePermission(String permissionName) throws SystemException {
        SystemUtils.getLogger().finest("Invocato");
        try {
            this.getPermissionDAO().deletePermission(permissionName);
            List permissions = new ArrayList(_permissions);
            for (int i = 0; i < permissions.size(); i++) {
                Permission currentPerm = (Permission) permissions.get(i);
                if (permissionName.equalsIgnoreCase(currentPerm.getName())) {
                    _permissions.remove(currentPerm);
                    break;
                }
            }
            Iterator roleIt = _roles.values().iterator();
            while (roleIt.hasNext()) {
                Role role = (Role) roleIt.next();
                role.removePermission(permissionName);
            }
        } catch (Throwable t) {
            SystemUtils.logThrowable(t, this, "removePermission");
            throw new SystemException("Errore in rimozione permesso", t);
        }
    }

    /**
	 * @param
	 * @throws
	 */
    public void updatePermission(Permission permission) throws SystemException {
        try {
            this.getPermissionDAO().updatePermission(permission);
            List permissions = new ArrayList(_permissions);
            for (int i = 0; i < permissions.size(); i++) {
                Permission currentPerm = (Permission) permissions.get(i);
                if (permission.getName().equalsIgnoreCase(currentPerm.getName())) {
                    _permissions.remove(currentPerm);
                    break;
                }
            }
            _permissions.add(permission);
        } catch (Throwable t) {
            SystemUtils.logThrowable(t, this, "updatePermission");
            throw new SystemException("Errore in aggiornamento permesso", t);
        }
    }

    /**
	 * @param
	 * @throws
	 */
    public void addPermission(Permission permission) throws SystemException {
        SystemUtils.getLogger().finest("Invocato");
        try {
            this.getPermissionDAO().addPermission(permission);
            _permissions.add(permission);
        } catch (Throwable t) {
            SystemUtils.logThrowable(t, this, "addPermission");
            throw new SystemException("Errore in aggiunta permesso", t);
        }
    }

    /**
	 * @throws
	 */
    public int getRoleUses(Role role) throws SystemException {
        SystemUtils.getLogger().finest("Invocato");
        int number = 0;
        try {
            number = this.getRoleDAO().getRoleUses(role);
        } catch (Throwable t) {
            throw new SystemException("Errore in numero utenti utilizzanti ruolo", t);
        }
        return number;
    }

    public List getRolesWithPermission(String permissionName) {
        List rolesWithPerm = new ArrayList();
        Collection roles = this.getRoles().values();
        Iterator iter = roles.iterator();
        while (iter.hasNext()) {
            Role role = (Role) iter.next();
            if (role.hasPermission(permissionName)) {
                rolesWithPerm.add(role);
            }
        }
        return rolesWithPerm;
    }

    protected IPermissionDAO getPermissionDAO() {
        return permissionDao;
    }

    public void setPermissionDAO(IPermissionDAO permissionDao) {
        this.permissionDao = permissionDao;
    }

    protected IRoleDAO getRoleDAO() {
        return roleDao;
    }

    public void setRoleDAO(IRoleDAO roleDao) {
        this.roleDao = roleDao;
    }

    private Map _roles;

    private Set _permissions;

    private IRoleDAO roleDao;

    private IPermissionDAO permissionDao;
}
