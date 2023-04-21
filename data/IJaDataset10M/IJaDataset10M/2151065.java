package org.tagunit.controller;

import java.io.*;
import java.util.*;
import java.security.Principal;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

/**
 * This is a simple wrapper, used to wrap up the actual ServletResponse so that
 * content never gets written to the output stream, and headers are never
 * set. This is necessary because we don't want tags actually outputting
 * content back to the browser. Instead we want to capture it, run the tests
 * and then output the results, without all of the content that would
 * otherwise be output.
 *
 * @author      Simon Brown
 */
public class TestHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String user;

    private String role;

    public TestHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public String getRemoteUser() {
        if (this.user != null) {
            return this.user;
        } else {
            return super.getRemoteUser();
        }
    }

    public String getAuthType() {
        return super.getAuthType();
    }

    public boolean isUserInRole(String s) {
        if (this.role != null) {
            return s.equals(this.role);
        } else {
            return super.isUserInRole(s);
        }
    }

    public Principal getUserPrincipal() {
        return super.getUserPrincipal();
    }

    /**
   * Sets the user attribute.
   *
   * @param user    the name of the user
   */
    public void setUser(String user) {
        this.user = user;
    }

    /**
   * Sets the role attribute.
   *
   * @param role    the name of the role
   */
    public void setRole(String role) {
        this.role = role;
    }
}
