package org.snipsnap.user;

import org.radeox.util.logging.Logger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Permissions holds a ACL list with permissions and
 * roles
 *
 * @author stephan
 * @version $Id: Permissions.java 1609 2004-05-17 10:56:18Z leo $
 */
public class Permissions {

    public static final String EDIT_SNIP = "Edit";

    public static final String ATTACH_TO_SNIP = "Attach";

    public static final String POST_TO_SNIP = "Post";

    private Map permissions;

    public Permissions() {
    }

    public Permissions(Map permissions) {
        this.permissions = permissions;
    }

    public Permissions(String permissions) {
        this.permissions = deserialize(permissions);
    }

    private void init() {
        if (null == permissions) {
            permissions = new HashMap();
        }
    }

    public String toString() {
        return serialize();
    }

    public boolean empty() {
        return null == permissions || permissions.isEmpty();
    }

    public void remove(String permission, String role) {
        init();
        if (permissions.containsKey(permission)) {
            Roles roles = (Roles) permissions.get(permission);
            roles.remove(role);
            if (roles.isEmpty()) {
                Logger.debug("Empty.");
                permissions.remove(permission);
            } else {
                Logger.debug("not Empty." + permissions.toString());
            }
        }
    }

    public void add(String permission) {
        init();
        if (!permissions.containsKey(permission)) {
            permissions.put(permission, new Roles());
        }
    }

    public void add(String permission, String role) {
        init();
        if (!permissions.containsKey(permission)) {
            permissions.put(permission, new Roles());
        }
        ((Roles) permissions.get(permission)).add(role);
        return;
    }

    public void add(String permission, Roles roles) {
        init();
        if (!permissions.containsKey(permission)) {
            permissions.put(permission, new Roles());
        }
        ((Roles) permissions.get(permission)).addAll(roles);
        return;
    }

    public boolean exists(String permission, Roles roles) {
        if (null == permissions || !permissions.containsKey(permission)) {
            return false;
        }
        Roles permRoles = (Roles) permissions.get(permission);
        return permRoles.containsAny(roles);
    }

    public boolean check(String permission, Roles roles) {
        if (null == permissions || !permissions.containsKey(permission)) {
            return true;
        }
        Roles permRoles = (Roles) permissions.get(permission);
        return permRoles.containsAny(roles);
    }

    public Map deserialize(String permissions) {
        Map perms = new HashMap();
        if (permissions == null || "".equals(permissions)) {
            return perms;
        }
        StringTokenizer tokenizer = new StringTokenizer(permissions, "|");
        while (tokenizer.hasMoreTokens()) {
            String permission = tokenizer.nextToken();
            Roles roles = getRoles(permission);
            permission = getPermission(permission);
            perms.put(permission, roles);
        }
        return perms;
    }

    private String serialize() {
        if (null == permissions || permissions.isEmpty()) {
            return "";
        }
        StringBuffer permBuffer = new StringBuffer();
        Iterator iterator = permissions.keySet().iterator();
        while (iterator.hasNext()) {
            String permission = (String) iterator.next();
            permBuffer.append(permission);
            permBuffer.append(":");
            Roles roles = (Roles) permissions.get(permission);
            Iterator rolesIterator = roles.iterator();
            while (rolesIterator.hasNext()) {
                String role = (String) rolesIterator.next();
                permBuffer.append(role);
                if (rolesIterator.hasNext()) {
                    permBuffer.append(",");
                }
            }
            if (iterator.hasNext()) {
                permBuffer.append("|");
            }
        }
        return permBuffer.toString();
    }

    private String after(String string, String delimiter) {
        return string.substring(string.indexOf(delimiter) + 1);
    }

    private String before(String string, String delimiter) {
        return string.substring(0, string.indexOf(delimiter));
    }

    private String getPermission(String rolesString) {
        return before(rolesString, ":");
    }

    private Roles getRoles(String rolesString) {
        Roles roles = new Roles();
        StringTokenizer tokenizer = new StringTokenizer(after(rolesString, ":"), ",");
        while (tokenizer.hasMoreTokens()) {
            String role = tokenizer.nextToken();
            roles.add(role);
        }
        return roles;
    }
}
