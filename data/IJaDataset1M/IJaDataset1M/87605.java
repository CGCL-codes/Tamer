package de.secpod.springsecurity.sample;

import java.util.Arrays;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class TestingProviderAuthenticationTest extends AbstractDependencyInjectionSpringContextTests {

    private static final GrantedAuthority[] GRANTED_AUTHORITIES_USER = new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_USER") };

    private static final GrantedAuthority[] GRANTED_AUTHORITIES_ADMIN = new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_USER"), new GrantedAuthorityImpl("ROLE_ADMIN") };

    private AuthenticationManager authenticationManager;

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "spring.xml", "spring_test.xml" };
    }

    public void testLogInOk() throws Exception {
        Authentication input = new TestingAuthenticationToken("no_user", "no_pw", null);
        assertFalse("Should not be authenticated", input.isAuthenticated());
        input.setAuthenticated(true);
        Authentication authentication = authenticationManager.authenticate(input);
        assertTrue("Should be authenticated", authentication.isAuthenticated());
        assertTrue("Should be of type TestingAuthenticationToken", authentication instanceof TestingAuthenticationToken);
        assertEquals("no_user", authentication.getName());
        assertEquals("no_pw", authentication.getCredentials().toString());
    }

    public void testGrantedAuthoritiesAdmin() throws Exception {
        Authentication input = new TestingAuthenticationToken("admin", "pwadmin", GRANTED_AUTHORITIES_ADMIN);
        Authentication authentication = authenticationManager.authenticate(input);
        assertEquals(Arrays.toString(GRANTED_AUTHORITIES_ADMIN), Arrays.toString(authentication.getAuthorities()));
    }

    public void testGrantedAuthoritiesUser() throws Exception {
        Authentication input = new TestingAuthenticationToken("admin", "pwadmin", GRANTED_AUTHORITIES_USER);
        Authentication authentication = authenticationManager.authenticate(input);
        assertEquals(Arrays.toString(GRANTED_AUTHORITIES_USER), Arrays.toString(authentication.getAuthorities()));
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
