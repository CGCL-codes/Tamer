package org.apache.harmony.security.tests.java.security;

import java.security.*;
import junit.framework.TestCase;

/**
 * Unit test for AccessControlException.
 */
public class AccessControlExceptionTest extends TestCase {

    /**
     * Entry point for standalone run.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AccessControlExceptionTest.class);
    }

    /**
     * Tests AccessControlException(String)
     */
    public void testAccessControlExceptionString() {
        new AccessControlException(null);
        new AccessControlException("Failure");
    }

    /**
     * Tests AccessControlException(String, Permission)
     */
    public void testAccessControlExceptionStringPermission() {
        Permission perm = new AllPermission();
        new AccessControlException("001", perm);
    }

    /**
     * 
     * Tests AccessControlException.getPermission()
     */
    public void testGetPermission() {
        Permission perm = new UnresolvedPermission("unresolvedType", "unresolvedName", "unresolvedActions", null);
        AccessControlException ex = new AccessControlException("001", perm);
        assertSame(ex.getPermission(), perm);
    }
}
