package org.bhf.security.authorization;

import java.security.Permission;
import java.util.Map;

/**
 * Interface to be implemented by all permissions that can have parameterized
 * names or actions. Principals that support parameters, such as
 * <code>Role</code>, have one or more named parameters. The value of each of
 * these parameters may be singular (type of <code>String</code>) or plural
 * (type of <code></code>String[]). A expression of ${<name>} will substitute
 * the value of the parameter <name> in the permission name or actions if the
 * value is singular. If a permission refers to a plural parameter, that
 * permission is cloned for each element of the element of the plural value and
 * that element is substituted within the expression. Note that a permission
 * may refer to only one plural paramter - all other parameter references must
 * be to single parameters.
 */
public interface ParameterizablePermission {

    /**
     * Apply parameters to the permission.
     *
     * @param parameters The substitution <code>String</code>s:
     *      key (<code>String</code>) to value (<code>String</code>).
     * @return A clone of the original <code>Permission</code> with the
     *      paramters applied to the name and actions expressions.
     */
    Permission[] parameterize(final Map<String, String[]> parameters);
}
