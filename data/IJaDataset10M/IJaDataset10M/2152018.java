package org.bhf.security.authorization;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import junit.framework.TestCase;
import org.bhf.providers.security.authorization.PolicyProvider;
import org.bhf.providers.security.authorization.xmlpolicy.XMLURLPolicyProvider;
import org.bhf.security.common.ContextSubject;
import org.bhf.security.common.Role;
import org.bhf.security.common.UserID;
import javax.security.auth.Subject;
import java.io.IOException;

/**
 * Test the <code>RoleBasedSecurityManager</code> class.
 */
public class TestRoleBasedSecurityManager extends TestCase {

    @Inject
    private RoleBasedSecurityManager roleBasedSecurityManager;

    public void setUp() throws IOException {
        final Injector injector = Guice.createInjector(new Module() {

            public void configure(final Binder binder) {
                try {
                    final XMLURLPolicyProvider p = new XMLURLPolicyProvider();
                    p.setPolicySource("classloader://org/bhf/security/authorization/policy.xml");
                    binder.bind(PolicyProvider.class).toInstance(p);
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
            }
        });
        injector.injectMembers(this);
    }

    public void tearDown() throws IOException {
    }

    /**
     * This test uses a policy that grants a specific permission to a role and verifies that the a Subject
     * with the role can pass a check permission operation.
     * @throws Exception Error
     */
    public void testPolicyGrantToRole() throws Exception {
        try {
            roleBasedSecurityManager.check(newSubject("foo", "admin"), new ServicePermission("/path/granted/to/admin", "read"));
        } catch (SecurityException e) {
            fail("Permission not granted");
        }
    }

    /**
     * This test uses a policy that denies a specific permission to a role and verifies that the a Subject
     * with the role can fails a check permission operation.
     * @throws Exception ERror
     */
    public void testPolicyDenyToRole() throws Exception {
        try {
            roleBasedSecurityManager.check(newSubject("foo", "admin"), new ServicePermission("/path/denied/to/admin", "read"));
            fail("Permission granted");
        } catch (SecurityException e) {
        }
    }

    /**
     * This test uses a policy that denies a specific permission to a UserID and verifies that the a Subject
     * with the UserID can fails a check permission operation.
     * @throws Exception Error
     */
    public void testPolicyDenyToUserID() throws Exception {
        try {
            roleBasedSecurityManager.check(newSubject("bob", "norole"), new ServicePermission("/path/denied/to/bob", "read"));
            fail("Permission granted");
        } catch (SecurityException e) {
        }
    }

    /**
     * This test uses a policy that grants a specific permission to a userID and verifies that the a Subject
     * with the userID can pass a check permission operation.
     * @throws Exception Error
     */
    public void testPolicyGrantToUserID() throws Exception {
        try {
            roleBasedSecurityManager.check(newSubject("bob", "norole"), new ServicePermission("/path/granted/to/bob", "read"));
        } catch (SecurityException e) {
            fail("Permission not granted");
        }
    }

    /**
     * This test uses a policy that grants a specific permission to a role and verifies that the a Subject
     * with the role can pass a check permission operation when the project and Subject are passed
     * implicitly as contextual parameters rather than explicit arguments.
     * @throws Exception Error
     */
    public void testContextualProjectAndSubject() throws Exception {
        try {
            ContextSubject.setSubject(newSubject("foo", "admin"));
            roleBasedSecurityManager.check(new ServicePermission("/path/granted/to/admin", "read"));
        } catch (SecurityException e) {
            fail("Permission not granted");
        } finally {
            ContextSubject.setSubject(null);
        }
    }

    private static Subject newSubject(String userID, String role) {
        final Subject subject = new Subject();
        subject.getPrincipals().add(new UserID(userID));
        subject.getPrincipals().add(new Role(role));
        return subject;
    }
}
