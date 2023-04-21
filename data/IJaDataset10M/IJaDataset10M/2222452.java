package org.bhf.providers.security.authorization.xmlpolicy;

import junit.framework.TestCase;
import org.bhf.security.authorization.ServicePermission;
import org.bhf.security.common.Role;
import org.bhf.security.common.UserID;
import java.util.HashMap;
import java.util.Map;

/**
 * Test the <code>XMLPolicy</code> class.
 */
public class TestXMLPolicy extends TestCase {

    /**
     * Test role aggregation by giving a user two roles and verifying that the permissions assigned to
     * both roles is assigned to the user.
     * @throws Exception error
     */
    public void testMultipleRoles() throws Exception {
        XMLPolicy policy = readPolicy("policy.xml");
        assertTrue(policy.getPermissions(new ServicePermission("", ""), new Role("p1")).implies(new ServicePermission("/path/granted/to/p1", "read")));
        assertTrue(policy.getPermissions(new ServicePermission("", ""), new Role("p2")).implies(new ServicePermission("/path/granted/to/p2", "read")));
    }

    /**
     * Test role parameterization by grant a permission with the path /foo/${p1} and
     * verifying that the permission /foo/bar is only granted when the role has
     * a paramter of foo
     * @throws Exception error
     */
    public void testRoleParameterization() throws Exception {
        XMLPolicy policy = readPolicy("policy.xml");
        assertFalse(policy.getPermissions(new ServicePermission("", ""), new Role("admin")).implies(new ServicePermission("/foo/bar", "read")));
        assertTrue(policy.getPermissions(new ServicePermission("", ""), new Role("admin", map("p1", "bar"))).implies(new ServicePermission("/foo/bar", "read")));
    }

    /**
     * This test uses a policy that denies a permission to a specific role and verifies
     * that a Subject with this role has been denied the permission.
     * @throws Exception error
     */
    public void testDenyToRole() throws Exception {
        XMLPolicy policy = readPolicy("policy.xml");
        assertFalse(policy.getPermissions(new ServicePermission("", ""), new Role("admin")).implies(new ServicePermission("/path/denied/to/admin", "read")));
    }

    /**
     * This test uses a policy that grants a permission to a specific userID and verifies
     * that a Subject with this userID has the permission.
     * @throws Exception error
     */
    public void testGrantToUser() throws Exception {
        XMLPolicy policy = readPolicy("policy.xml");
        assertTrue(policy.getPermissions(new ServicePermission("", ""), new UserID("bob")).implies(new ServicePermission("/path/granted/to/bob", "read")));
    }

    /**
     * This test uses a policy that denies a permission to a specific userID and verifies
     * that a Subject with this userID has been denied the permission.
     * @throws Exception error
     */
    public void testDenyToUser() throws Exception {
        XMLPolicy policy = readPolicy("policy.xml");
        assertFalse(policy.getPermissions(new ServicePermission("", ""), new UserID("bob")).implies(new ServicePermission("/path/denied/to/bob", "read")));
    }

    private static Map<String, String[]> map(final String key, final String value) {
        final Map<String, String[]> m = new HashMap<String, String[]>();
        m.put(key, new String[] { value });
        return m;
    }

    private static XMLPolicy readPolicy(final String name) throws Exception {
        return new XMLPolicy(TestXMLPolicy.class.getResourceAsStream(name), true);
    }
}
