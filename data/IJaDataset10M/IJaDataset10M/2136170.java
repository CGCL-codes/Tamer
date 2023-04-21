package br.com.linkcom.neo.authorization.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import br.com.linkcom.neo.authorization.AuthorizationDAO;
import br.com.linkcom.neo.authorization.AuthorizationItem;
import br.com.linkcom.neo.authorization.AuthorizationModule;
import br.com.linkcom.neo.authorization.Permission;
import br.com.linkcom.neo.authorization.PermissionLocator;
import br.com.linkcom.neo.authorization.Role;
import br.com.linkcom.neo.authorization.User;
import br.com.linkcom.neo.controller.ControlMapping;
import br.com.linkcom.neo.core.standard.RequestContext;

/**
 * Implementacao da interface para localizar as Permissions
 * @author rogelgarcia
 */
public class PermissionLocatorImpl implements PermissionLocator {

    static Log log = LogFactory.getLog(PermissionLocator.class);

    private static final String CACHE_ROLES = "CACHE_ROLES";

    protected AuthorizationDAO authorizationDAO;

    public void setAuthorizationDAO(AuthorizationDAO authorizationDAO) {
        this.authorizationDAO = authorizationDAO;
    }

    Map<ControlMapping, Map<Role, Permission>> cache = new HashMap<ControlMapping, Map<Role, Permission>>();

    public synchronized Permission[] getPermissions(RequestContext request, User user, ControlMapping control) {
        Map<User, Role[]> cacheRoles = getCacheRoles(request);
        Role[] userRoles = cacheRoles.get(user);
        if (userRoles == null) {
            userRoles = authorizationDAO.findUserRoles(user);
            cacheRoles.put(user, userRoles);
        }
        Permission[] permissions = new Permission[userRoles.length];
        for (int i = 0; i < userRoles.length; i++) {
            String controlName = control.getName();
            Role role = userRoles[i];
            Map<Role, Permission> mapRolePermission;
            mapRolePermission = cache.get(control);
            if (mapRolePermission != null) {
                Permission permission = mapRolePermission.get(role);
                if (permission != null) {
                    log.debug("Using cached permission: " + permission);
                    permissions[i] = permission;
                    continue;
                }
            } else {
                mapRolePermission = new HashMap<Role, Permission>();
                cache.put(control, mapRolePermission);
            }
            permissions[i] = authorizationDAO.findPermission(role, controlName);
            if (permissions[i] == null) {
                AuthorizationModule authorizationModule = control.getAuthorizationModule();
                Map<String, String> defaultPermissionMap = getDefaultPermissionMap(authorizationModule);
                log.debug("Criando permissao... control=" + controlName + ", role=" + role.getName());
                permissions[i] = authorizationDAO.savePermission(controlName, role, defaultPermissionMap);
            }
            cache.get(control).put(role, permissions[i]);
        }
        return permissions;
    }

    @SuppressWarnings("unchecked")
    private Map<User, Role[]> getCacheRoles(RequestContext request) {
        Map<User, Role[]> cache = (Map<User, Role[]>) request.getUserAttribute(CACHE_ROLES);
        if (cache == null) {
            cache = new HashMap<User, Role[]>();
            request.setUserAttribute(CACHE_ROLES, cache);
        }
        return cache;
    }

    private Map<String, String> getDefaultPermissionMap(AuthorizationModule authorizationModule) {
        AuthorizationItem[] authorizationItens = authorizationModule.getAuthorizationItens();
        Map<String, String> defaultPermissionMap = new HashMap<String, String>();
        for (AuthorizationItem item : authorizationItens) {
            String id = item.getId();
            if (item.getValores() == null || item.getValores().length == 0) throw new IllegalArgumentException("Os valores poss�veis de um item de autoriza��o n�o pode ser um array vazio ou null");
            String valorMaisRestritivo = item.getValores()[item.getValores().length - 1];
            defaultPermissionMap.put(id, valorMaisRestritivo);
        }
        return defaultPermissionMap;
    }

    public synchronized void reset() {
        cache = new HashMap<ControlMapping, Map<Role, Permission>>();
    }

    public void clearCache() {
        reset();
    }
}
