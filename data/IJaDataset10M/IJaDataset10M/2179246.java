package simple.http.load;

import java.security.BasicPermission;

/**
 * The <code>LoaderPermission</code> is used to provide access to
 * the functions of the <code>LoaderManager</code>. This will
 * grant permission to use the methods provided the correct
 * actions strings are given. The permissions that can be granted
 * are the "load", "update", and "link" actions. 
 * <p>
 * This is required because the <code>LoaderManager</code> is 
 * given to the <code>Service</code> instances on creation. This
 * ensures that loaded code can be restricted from changing the
 * configuration of the <code>LoaderManager</code>.
 *
 * @author Niall Gallagher
 */
public final class LoaderPermission extends BasicPermission {

    /**
    * Constructor fot the <code>LoaderPermission</code> requires
    * an action string. The actions that can be granted are the 
    * "load", "update" and, "link" actions. This can be used 
    * within security policy files as it follows the same naming
    * scheme as the <code>BasicPermission</code>. 
    *
    * @param action this is the action that is to be granted
    */
    public LoaderPermission(String action) {
        super(action);
    }
}
