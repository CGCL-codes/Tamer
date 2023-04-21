package org.acegisecurity.ui.switchuser;

import junit.framework.TestCase;
import org.acegisecurity.AccountExpiredException;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.CredentialsExpiredException;
import org.acegisecurity.DisabledException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.util.MockFilterChain;
import org.springframework.dao.DataAccessException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Tests {@link org.acegisecurity.ui.switchuser.SwitchUserProcessingFilter}.
 *
 * @author Mark St.Godard
 * @version $Id: SwitchUserProcessingFilterTests.java,v 1.8 2005/11/30 01:23:34 benalex Exp $
 */
public class SwitchUserProcessingFilterTests extends TestCase {

    public SwitchUserProcessingFilterTests() {
        super();
    }

    public SwitchUserProcessingFilterTests(String arg0) {
        super(arg0);
    }

    public final void setUp() throws Exception {
        super.setUp();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SwitchUserProcessingFilterTests.class);
    }

    public void testAttemptSwitchToUnknownUser() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("dano", "hawaii50");
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(SwitchUserProcessingFilter.ACEGI_SECURITY_SWITCH_USERNAME_KEY, "user-that-doesnt-exist");
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setUserDetailsService(new MockAuthenticationDaoUserJackLord());
        try {
            Authentication result = filter.attemptSwitchUser(request);
            fail("Should not be able to switch to unknown user");
        } catch (UsernameNotFoundException expected) {
        }
    }

    public void testAttemptSwitchToUserThatIsDisabled() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("dano", "hawaii50");
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(SwitchUserProcessingFilter.ACEGI_SECURITY_SWITCH_USERNAME_KEY, "mcgarrett");
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setUserDetailsService(new MockAuthenticationDaoUserJackLord());
        try {
            Authentication result = filter.attemptSwitchUser(request);
            fail("Should not be able to switch to disabled user");
        } catch (DisabledException expected) {
        }
    }

    public void testAttemptSwitchToUserWithAccountExpired() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("dano", "hawaii50");
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(SwitchUserProcessingFilter.ACEGI_SECURITY_SWITCH_USERNAME_KEY, "wofat");
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setUserDetailsService(new MockAuthenticationDaoUserJackLord());
        try {
            Authentication result = filter.attemptSwitchUser(request);
            fail("Should not be able to switch to user with expired account");
        } catch (AccountExpiredException expected) {
        }
    }

    public void testAttemptSwitchToUserWithExpiredCredentials() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("dano", "hawaii50");
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(SwitchUserProcessingFilter.ACEGI_SECURITY_SWITCH_USERNAME_KEY, "steve");
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setUserDetailsService(new MockAuthenticationDaoUserJackLord());
        try {
            Authentication result = filter.attemptSwitchUser(request);
            fail("Should not be able to switch to user with expired account");
        } catch (CredentialsExpiredException expected) {
        }
    }

    public void testAttemptSwitchUser() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("dano", "hawaii50");
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(SwitchUserProcessingFilter.ACEGI_SECURITY_SWITCH_USERNAME_KEY, "jacklord");
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setUserDetailsService(new MockAuthenticationDaoUserJackLord());
        Authentication result = filter.attemptSwitchUser(request);
        assertTrue(result != null);
    }

    public void testBadConfigMissingAuthenticationDao() {
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setSwitchUserUrl("/j_acegi_switch_user");
        filter.setExitUserUrl("/j_acegi_exit_user");
        filter.setTargetUrl("/main.jsp");
        try {
            filter.afterPropertiesSet();
            fail("Expect to fail due to missing 'authenticationDao'");
        } catch (Exception expected) {
        }
    }

    public void testBadConfigMissingTargetUrl() {
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setUserDetailsService(new MockAuthenticationDaoUserJackLord());
        filter.setSwitchUserUrl("/j_acegi_switch_user");
        filter.setExitUserUrl("/j_acegi_exit_user");
        try {
            filter.afterPropertiesSet();
            fail("Expect to fail due to missing 'targetUrl'");
        } catch (Exception expected) {
        }
    }

    public void testDefaultProcessesFilterUrlWithPathParameter() {
        MockHttpServletRequest request = createMockSwitchRequest();
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setSwitchUserUrl("/j_acegi_switch_user");
        request.setRequestURI("/webapp/j_acegi_switch_user;jsessionid=8JHDUD723J8");
        assertTrue(filter.requiresSwitchUser(request));
    }

    public void testExitRequestUserJackLordToDano() throws Exception {
        GrantedAuthority[] auths = { new GrantedAuthorityImpl("ROLE_ONE"), new GrantedAuthorityImpl("ROLE_TWO") };
        UsernamePasswordAuthenticationToken source = new UsernamePasswordAuthenticationToken("dano", "hawaii50", auths);
        GrantedAuthority[] adminAuths = { new GrantedAuthorityImpl("ROLE_ONE"), new GrantedAuthorityImpl("ROLE_TWO"), new SwitchUserGrantedAuthority("PREVIOUS_ADMINISTRATOR", source) };
        UsernamePasswordAuthenticationToken admin = new UsernamePasswordAuthenticationToken("jacklord", "hawaii50", adminAuths);
        SecurityContextHolder.getContext().setAuthentication(admin);
        MockHttpServletRequest request = createMockSwitchRequest();
        request.setRequestURI("/j_acegi_exit_user");
        MockHttpServletResponse response = new MockHttpServletResponse();
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setUserDetailsService(new MockAuthenticationDaoUserJackLord());
        filter.setExitUserUrl("/j_acegi_exit_user");
        MockFilterChain chain = new MockFilterChain(true);
        filter.doFilter(request, response, chain);
        Authentication targetAuth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(targetAuth);
        assertEquals("dano", targetAuth.getPrincipal());
    }

    public void testExitUserWithNoCurrentUser() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(null);
        MockHttpServletRequest request = createMockSwitchRequest();
        request.setRequestURI("/j_acegi_exit_user");
        MockHttpServletResponse response = new MockHttpServletResponse();
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setUserDetailsService(new MockAuthenticationDaoUserJackLord());
        filter.setExitUserUrl("/j_acegi_exit_user");
        MockFilterChain chain = new MockFilterChain(true);
        try {
            filter.doFilter(request, response, chain);
            fail("Cannot exit from a user with no current user set!");
        } catch (AuthenticationException expected) {
        }
    }

    public void testRedirectToTargetUrl() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("dano", "hawaii50");
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = createMockSwitchRequest();
        request.addParameter(SwitchUserProcessingFilter.ACEGI_SECURITY_SWITCH_USERNAME_KEY, "jacklord");
        request.setRequestURI("/webapp/j_acegi_switch_user");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain(true);
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setSwitchUserUrl("/j_acegi_switch_user");
        filter.setTargetUrl("/webapp/someOtherUrl");
        filter.setUserDetailsService(new MockAuthenticationDaoUserJackLord());
        filter.doFilter(request, response, chain);
        assertEquals("/webapp/someOtherUrl", response.getRedirectedUrl());
    }

    public void testRequiresExitUser() {
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setExitUserUrl("/j_acegi_exit_user");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/j_acegi_exit_user");
        assertTrue(filter.requiresExitUser(request));
    }

    public void testRequiresSwitch() {
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setSwitchUserUrl("/j_acegi_switch_user");
        MockHttpServletRequest request = createMockSwitchRequest();
        assertTrue(filter.requiresSwitchUser(request));
    }

    public void testSwitchRequestFromDanoToJackLord() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("dano", "hawaii50");
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/webapp/j_acegi_switch_user");
        request.addParameter(SwitchUserProcessingFilter.ACEGI_SECURITY_SWITCH_USERNAME_KEY, "jacklord");
        MockHttpServletResponse response = new MockHttpServletResponse();
        SwitchUserProcessingFilter filter = new SwitchUserProcessingFilter();
        filter.setUserDetailsService(new MockAuthenticationDaoUserJackLord());
        filter.setSwitchUserUrl("/j_acegi_switch_user");
        MockFilterChain chain = new MockFilterChain(true);
        filter.doFilter(request, response, chain);
        Authentication targetAuth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(targetAuth);
        assertTrue(targetAuth.getPrincipal() instanceof UserDetails);
        assertEquals("jacklord", ((User) targetAuth.getPrincipal()).getUsername());
    }

    private MockHttpServletRequest createMockSwitchRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("http");
        request.setServerName("localhost");
        request.setRequestURI("/j_acegi_switch_user");
        return request;
    }

    private class MockAuthenticationDaoUserJackLord implements UserDetailsService {

        private String password = "hawaii50";

        public void setPassword(String password) {
            this.password = password;
        }

        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
            if ("jacklord".equals(username) || "dano".equals(username)) {
                return new User(username, password, true, true, true, true, new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_ONE"), new GrantedAuthorityImpl("ROLE_TWO") });
            } else if ("mcgarrett".equals(username)) {
                return new User(username, password, false, true, true, true, new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_ONE"), new GrantedAuthorityImpl("ROLE_TWO") });
            } else if ("wofat".equals(username)) {
                return new User(username, password, true, false, true, true, new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_ONE"), new GrantedAuthorityImpl("ROLE_TWO") });
            } else if ("steve".equals(username)) {
                return new User(username, password, true, true, false, true, new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_ONE"), new GrantedAuthorityImpl("ROLE_TWO") });
            } else {
                throw new UsernameNotFoundException("Could not find: " + username);
            }
        }
    }
}
