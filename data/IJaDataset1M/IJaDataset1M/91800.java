package com.ashridgetech.riabase.login;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

public class LoginCheckFilterTest extends TestCase {

    static class MockFilterChain implements FilterChain {

        public ServletRequest request;

        public ServletResponse response;

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            this.request = request;
            this.response = response;
        }
    }

    private static final Object ORGANISATION_NAME = "abc";

    private static final Object USER_NAME = "jon";

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private LoginCheckFilter filter;

    private MockFilterChain filterChain;

    protected String organisationNameDuringDoFilter;

    protected String userNameDuringDoFilter;

    protected void setUp() throws Exception {
        super.setUp();
        filterChain = new MockFilterChain() {

            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                organisationNameDuringDoFilter = CurrentRequest.getOrganisationName();
                userNameDuringDoFilter = CurrentRequest.getUserName();
                super.doFilter(request, response);
            }
        };
        filter = new LoginCheckFilter();
        request = new MockHttpServletRequest();
        request.setMethod("GET");
        response = new MockHttpServletResponse();
    }

    public void testPassesThroughLoginRequestIfNotLoggedIn() throws Exception {
        checkLoginRequest(false, false);
    }

    public void testPassesThroughLoginRequestIfAlreadyLoggedIn() throws Exception {
        checkLoginRequest(true, false);
    }

    public void testPassesThroughLogoutRequestIfNotLoggedIn() throws Exception {
        checkLoginRequest(false, true);
    }

    public void testPassesThroughLogoutRequestIfAlreadyLoggedIn() throws Exception {
        checkCreateRequest(true);
    }

    public void testPassesThroughCreateRequestIfNotLoggedIn() throws Exception {
        checkCreateRequest(false);
    }

    public void testPassesThroughCreateRequestIfAlreadyLoggedIn() throws Exception {
        checkLoginRequest(true, true);
    }

    private void checkLoginRequest(boolean loggedIn, boolean isLogout) throws IOException, ServletException {
        HttpSession session = loggedIn ? new MockHttpSession() : null;
        request.setSession(session);
        request.setServerName("riabase");
        request.setServletPath("/login/abc");
        if (isLogout) {
            request.setServletPath("/logout");
        }
        filter.doFilter(request, response, filterChain);
        assertSame(request, filterChain.request);
        assertSame(response, filterChain.response);
    }

    private void checkCreateRequest(boolean loggedIn) throws IOException, ServletException {
        HttpSession session = loggedIn ? new MockHttpSession() : null;
        request.setSession(session);
        request.setServerName("riabase");
        request.setServletPath("/create");
        filter.doFilter(request, response, filterChain);
        assertSame(request, filterChain.request);
        assertSame(response, filterChain.response);
    }

    public void testPassesThroughNonDataRequestIfNotLoggedIn() throws Exception {
        checkPassesThroughRequestWithExtension("css");
        checkPassesThroughRequestWithExtension("gif");
        checkPassesThroughRequestWithExtension("png");
    }

    private void checkPassesThroughRequestWithExtension(String extension) throws IOException, ServletException {
        request.setServerName("riabase");
        request.setServletPath("/splash." + extension);
        filter.doFilter(request, response, filterChain);
        assertSame(request, filterChain.request);
        assertSame(response, filterChain.response);
    }

    public void testRedirectsToLoginIfNotLoggedIn() throws Exception {
        request.setSession(null);
        request.setServerName("riabase");
        request.setRequestURI("/report");
        filter.doFilter(request, response, filterChain);
        assertNull(filterChain.request);
        assertNull(filterChain.response);
        assertEquals("http://riabase:80/login", response.getRedirectedUrl());
        request.setRequestURI("");
        response.setCommitted(false);
        filter.doFilter(request, response, filterChain);
        assertNull(filterChain.request);
        assertNull(filterChain.response);
        assertEquals("http://riabase:80/login", response.getRedirectedUrl());
    }

    public void testPassesThroughRequestIfLoggedInWithCurrentOrganisationAndUserSetAndRemovesAfterwards() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(LoginServlet.ORGANISATION_NAME_ATTR, ORGANISATION_NAME);
        session.setAttribute(LoginServlet.USER_NAME_ATTR, USER_NAME);
        request.setSession(session);
        filter.doFilter(request, response, filterChain);
        assertSame(request, filterChain.request);
        assertSame(response, filterChain.response);
        assertEquals(ORGANISATION_NAME, organisationNameDuringDoFilter);
        assertEquals(USER_NAME, userNameDuringDoFilter);
        assertNull(CurrentRequest.getOrganisationName());
        assertNull(CurrentRequest.getUserName());
    }
}
