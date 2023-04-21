package org.gaixie.micrite.security.service.impl;

import java.util.List;
import org.gaixie.micrite.beans.Authority;
import org.gaixie.micrite.beans.Role;
import org.gaixie.micrite.beans.User;
import org.gaixie.micrite.security.dao.IAuthorityDAO;
import org.gaixie.micrite.security.dao.IRoleDAO;
import org.gaixie.micrite.security.dao.IUserDAO;
import org.gaixie.micrite.security.filter.FilterSecurityInterceptor;
import org.gaixie.micrite.security.filter.MethodSecurityInterceptor;
import org.gaixie.micrite.security.SecurityException;
import org.gaixie.micrite.security.service.IRoleService;
import org.gaixie.micrite.security.service.ISecurityAclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.MutableAclService;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.security.acls.objectidentity.ObjectIdentityImpl;
import org.springframework.security.acls.sid.GrantedAuthoritySid;
import org.springframework.security.providers.dao.UserCache;

/**
 * 接口 <code>IRoleService</code> 的实现类。
 * 
 */
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private IRoleDAO roleDAO;

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IAuthorityDAO authorityDAO;

    @Autowired
    private UserCache userCache;

    @Autowired
    private ISecurityAclService securityAclService;

    public List<Role> findByNameVaguePerPage(String name, int start, int limit) {
        return roleDAO.findByNameVaguePerPage(name, start, limit);
    }

    public int findByNameVagueCount(String name) {
        return roleDAO.findByNameVagueCount(name);
    }

    public void deleteRoles(int[] roleIds) throws SecurityException {
        for (int i = 0; i < roleIds.length; i++) {
            int roleId = roleIds[i];
            if (userDAO.findByRoleIdCount(roleId) > 0) {
                throw new SecurityException("error.role.delete.userNotEmptyInRole");
            }
            if (authorityDAO.findByRoleIdCount(roleId) > 0) {
                throw new SecurityException("error.role.delete.authNotEmptyInRole");
            }
            Role role = roleDAO.get(roleId);
            delete(role);
        }
    }

    public void delete(Role role) {
        roleDAO.delete(role);
        ObjectIdentity oid = new ObjectIdentityImpl(Role.class, (long) role.getId());
        securityAclService.deleteAcl(oid, false);
    }

    public void add(Role role) throws SecurityException {
        if (isExistedByRolename(role.getName())) {
            throw new SecurityException("error.role.add.roleNameInUse");
        }
        roleDAO.save(role);
        securityAclService.addPermission(role, new GrantedAuthoritySid(role.getName()), BasePermission.READ, Role.class);
        securityAclService.addPermission(role, new GrantedAuthoritySid("ROLE_ADMIN"), BasePermission.ADMINISTRATION, Role.class);
    }

    public boolean isExistedByRolename(String rolename) {
        Role role = roleDAO.findByRolename(rolename);
        if (role != null) {
            return true;
        }
        return false;
    }

    public void update(Role role) throws SecurityException {
        Role crole = roleDAO.get(role.getId());
        crole.setDescription(role.getDescription());
    }

    public List<Role> findByUserIdPerPage(int userId, int start, int limit) {
        return roleDAO.findByUserIdPerPage(userId, start, limit);
    }

    public int findByUserIdCount(int userId) {
        return roleDAO.findByUserIdCount(userId);
    }

    public void bindRolesToUser(int[] roleIds, int userId) {
        User user = userDAO.get(userId);
        for (int i = 0; i < roleIds.length; i++) {
            Role role = roleDAO.get(roleIds[i]);
            user.getRoles().add(role);
        }
        if (userCache != null) {
            userCache.removeUserFromCache(user.getLoginname());
        }
    }

    public void unBindRolesFromUser(int[] roleIds, int userId) {
        User user = userDAO.get(userId);
        for (int i = 0; i < roleIds.length; i++) {
            Role role = roleDAO.get(roleIds[i]);
            user.getRoles().remove(role);
        }
        if (userCache != null) {
            userCache.removeUserFromCache(user.getLoginname());
        }
    }

    public List<Role> findByAuthorityIdPerPage(int authorityId, int start, int limit) {
        return roleDAO.findByAuthorityIdPerPage(authorityId, start, limit);
    }

    public int findByAuthorityIdCount(int authorityId) {
        return roleDAO.findByAuthorityIdCount(authorityId);
    }

    public void bindRolesToAuthority(int[] roleIds, int authorityId) {
        Authority authority = authorityDAO.get(authorityId);
        for (int i = 0; i < roleIds.length; i++) {
            Role role = roleDAO.get(roleIds[i]);
            authority.getRoles().add(role);
        }
        FilterSecurityInterceptor.refresh();
        MethodSecurityInterceptor.refresh();
    }

    public void unBindRolesFromAuthority(int[] roleIds, int authorityId) {
        Authority authority = authorityDAO.get(authorityId);
        for (int i = 0; i < roleIds.length; i++) {
            Role role = roleDAO.get(roleIds[i]);
            authority.getRoles().remove(role);
        }
        FilterSecurityInterceptor.refresh();
        MethodSecurityInterceptor.refresh();
    }
}
