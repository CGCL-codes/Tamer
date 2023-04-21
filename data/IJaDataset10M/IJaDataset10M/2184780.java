package com.netx.eap.R1.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import com.netx.basic.R1.eh.Checker;
import com.netx.bl.R1.core.*;

public class Users extends HolderEntity<UsersMetaData, User> {

    private static final String _POSSIBLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static Users getInstance() {
        return EAP.getUsers();
    }

    public static String generatePassword(int length) {
        char[] buffer = new char[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            buffer[i] = _POSSIBLE.charAt(random.nextInt(_POSSIBLE.length()));
        }
        return new String(buffer);
    }

    private Select _qSelectUserByUsername;

    Users() {
        super(new UsersMetaData());
    }

    protected void onLoad() {
        _qSelectUserByUsername = createSelect("select-user-by-username", "SELECT * FROM eap_users WHERE username = ?");
        _qSelectUserByUsername.setUpdatesCache(true);
        _qSelectUserByUsername.setUpdatesCache(true);
    }

    public void create(Connection c, User u, AssociationMap<UserRole> roles, AssociationMap<UserPermission> permissions) throws BLException, ReadOnlyFieldException {
        Checker.checkNull(u, "u");
        u.resetPassword();
        _validate(c, roles, permissions);
        synchronized (c) {
            c.startTransaction();
            insert(c, u);
            UserRoles.getInstance().save(c, roles);
            if (permissions != null) {
                UserPermissions.getInstance().save(c, permissions);
            }
            c.commit();
        }
    }

    public void save(Connection c, User u, AssociationMap<UserRole> roles, AssociationMap<UserPermission> permissions) throws BLException, ReadOnlyFieldException {
        Checker.checkNull(u, "u");
        if (roles != null) {
            _validate(c, roles, permissions);
        }
        if (roles == null && permissions == null) {
            updateInstance(c, u);
        } else {
            synchronized (c) {
                c.startTransaction();
                updateInstance(c, u);
                if (roles != null) {
                    UserRoles.getInstance().save(c, roles);
                    if (permissions != null) {
                        UserPermissions.getInstance().save(c, permissions);
                    }
                }
                c.commit();
            }
        }
    }

    public User getUserByUsername(Connection c, String username) throws BLException {
        Checker.checkEmpty(username, "username");
        return selectInstance(c, _qSelectUserByUsername, username);
    }

    public List<User> listUsersByRole(Connection c, Long roleId) throws BLException {
        Checker.checkNull(roleId, "roleId");
        Checker.checkNull(roleId, "roleId");
        List<User> users = new ArrayList<User>();
        for (User u : selectAll(c)) {
            AssociationMap<UserRole> uRoles = u.getUserRoles(c);
            for (UserRole ur : uRoles) {
                if (ur.getRoleId().equals(roleId)) {
                    users.add(u);
                    break;
                }
            }
        }
        return users;
    }

    private void _validate(Connection c, AssociationMap<UserRole> roles, AssociationMap<UserPermission> permissions) throws BLException {
        if (roles.size() == 0) {
            throw new FunctionalValidationException(L10n.EAP_VAL_ONE_ROLE_REQUIRED);
        }
        int primaryCount = 0;
        Map<String, Long> permissionMap = new HashMap<String, Long>();
        for (UserRole ur : roles) {
            if (ur.getPrimaryRole()) {
                primaryCount++;
            }
            Role r = ur.getRole(c);
            for (RolePermission rp : r.getRolePermissions(c)) {
                permissionMap.put(rp.getPermissionId(), r.getRoleId());
            }
        }
        if (primaryCount != 1) {
            throw new FunctionalValidationException(L10n.EAP_VAL_ONE_PRIMARY_ROLE);
        }
        if (permissions != null) {
            for (UserPermission up : permissions) {
                Long roleId = permissionMap.get(up.getPermissionId());
                if (roleId != null) {
                    throw new FunctionalValidationException(L10n.EAP_VAL_DUPLICATE_USER_PERMISSION, up.getPermission(c).getName(), Roles.getInstance().get(c, roleId).getName());
                }
            }
        }
    }
}
