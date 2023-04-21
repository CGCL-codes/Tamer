package javax.security.auth;

import java.security.CodeSource;
import java.security.PermissionCollection;

/**
 * @deprecated The classes java.security.Policy and
 * java.security.ProtectionDomain provide the functionality of this class.
 */
public abstract class Policy {

    private static Policy policy;

    protected Policy() {
    }

    public static synchronized Policy getPolicy() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AuthPermission("getPolicy"));
        }
        return policy;
    }

    public static synchronized void setPolicy(Policy p) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AuthPermission("setPolicy"));
        }
        policy = p;
    }

    public abstract PermissionCollection getPermissions(Subject subject, CodeSource source);

    public abstract void refresh();
}
