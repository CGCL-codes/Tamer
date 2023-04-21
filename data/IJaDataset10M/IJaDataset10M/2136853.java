package org.acegisecurity.providers.jaas;

import junit.framework.TestCase;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

/**
 * Tests SecurityContextLoginModule
 *
 * @author Ray Krueger
 */
public class SecurityContextLoginModuleTests extends TestCase {

    private SecurityContextLoginModule module = null;

    private Subject subject = new Subject(false, new HashSet(), new HashSet(), new HashSet());

    private UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("principal", "credentials");

    protected void setUp() throws Exception {
        module = new SecurityContextLoginModule();
        module.initialize(subject, null, null, null);
        SecurityContextHolder.clearContext();
    }

    protected void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        module = null;
    }

    public void testAbort() throws Exception {
        assertFalse("Should return false, no auth is set", module.abort());
        SecurityContextHolder.getContext().setAuthentication(auth);
        module.login();
        module.commit();
        assertTrue(module.abort());
    }

    public void testLoginException() throws Exception {
        try {
            module.login();
            fail("LoginException expected, there is no Authentication in the SecurityContext");
        } catch (LoginException e) {
        }
    }

    public void testLoginSuccess() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(auth);
        assertTrue("Login should succeed, there is an authentication set", module.login());
        assertTrue("The authentication is not null, this should return true", module.commit());
        assertTrue("Principals should contain the authentication", subject.getPrincipals().contains(auth));
    }

    public void testLogout() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(auth);
        module.login();
        assertTrue("Should return true as it succeeds", module.logout());
        assertEquals("Authentication should be null", null, module.getAuthentication());
        assertFalse("Principals should not contain the authentication after logout", subject.getPrincipals().contains(auth));
    }

    public void testNullAuthenticationInSecurityContext() throws Exception {
        try {
            SecurityContextHolder.getContext().setAuthentication(null);
            module.login();
            fail("LoginException expected, the authentication is null in the SecurityContext");
        } catch (Exception e) {
        }
    }

    public void testNullAuthenticationInSecurityContextIgnored() throws Exception {
        module = new SecurityContextLoginModule();
        Map options = new HashMap();
        options.put("ignoreMissingAuthentication", "true");
        module.initialize(subject, null, null, options);
        SecurityContextHolder.getContext().setAuthentication(null);
        assertFalse("Should return false and ask to be ignored", module.login());
    }

    public void testNullLogout() throws Exception {
        assertFalse(module.logout());
    }
}
