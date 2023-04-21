package com.googlecode.projecteleanor.test;

import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;

/**
 * Create Date: 2009/8/6
 *
 * @author Alan She
 */
public abstract class BaseServletTestCase extends BaseTestCase {

    private MockServletContext servletContext;

    private MockHttpSession httpSession;

    private MockHttpServletRequest httpServletRequest;

    private MockHttpServletResponse httpServletResponse;

    @Before
    public void startServletContext() {
        newServletContext();
    }

    protected void newRequest() {
        httpServletRequest = new MockHttpServletRequest(servletContext);
        httpServletResponse = new MockHttpServletResponse();
        httpServletRequest.setSession(httpSession);
    }

    protected void newSession() {
        httpSession = new MockHttpSession(servletContext);
        newRequest();
    }

    protected void newServletContext() {
        servletContext = new MockServletContext();
        newSession();
        newRequest();
    }

    protected MockServletContext getServletContext() {
        return servletContext;
    }

    protected MockHttpSession getHttpSession() {
        return httpSession;
    }

    protected MockHttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    protected MockHttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }
}
