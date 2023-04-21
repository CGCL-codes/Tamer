package org.netxilia.api.user;

import java.security.AccessController;

/**
 * This class allows the calling code to execute privileged actions on the protected objects. When the calling code sets
 * the priveleged mode the permissions on the accessed sheets are no longer checked.
 * 
 * TODO Should have a look to {@link AccessController#doPrivileged(java.security.PrivilegedAction)} for a possible
 * replacement.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class AclPrivilegedMode {

    private static ThreadLocal<Boolean> privilegedMode = new ThreadLocal<Boolean>();

    /**
	 * allows the code in the current thread to access the objects. For example access to users sheet only to get the
	 * user's credentials.
	 * 
	 * @return true if the privileged mode was set before. If was set the clear method should not be called.
	 */
    public static boolean set() {
        boolean isSet = isSet();
        privilegedMode.set(Boolean.TRUE);
        return isSet;
    }

    /**
	 * clears the privileged mode
	 */
    public static void clear() {
        privilegedMode.set(Boolean.FALSE);
    }

    public static boolean isSet() {
        return (Boolean.TRUE.equals(privilegedMode.get()));
    }
}
