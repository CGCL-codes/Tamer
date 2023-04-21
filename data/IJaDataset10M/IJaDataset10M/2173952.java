package krico.arara.model;

/**
 * This is the exception thrown when a resource is accessed with a user 
 * that does not have permission to access that resource
 * @author @arara-author@
 * @version @arara-version@
 * @sf $Header: /cvsroot/arara/arara/sources/krico/arara/model/PermissionException.java,v 1.3 2002/01/11 01:37:30 krico Exp $
 */
public class PermissionException extends Exception {

    Permission permission;

    public PermissionException(Permission p) {
        super();
        permission = p;
    }

    public Permission getPermission() {
        return permission;
    }
}
