package com.beanstalktech.servlet.client;

import com.beanstalktech.servlet.client.HttpServletClient;
import com.beanstalktech.common.context.Application;
import com.beanstalktech.common.utility.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Enumeration;
import java.io.IOException;

/**
 * HTTPSimple protocol. Extends class
 * HttpServletClient to provide a servlet client with an HTTPSimple
 * protocol payload.
 *
 * @see com.beanstalktech.servlet.client.HttpServletClient
 */
public class SimpleServlet extends HttpServletClient {

    public void init(ServletConfig config) throws ServletException {
        try {
            m_application = new Application("SimpleServlet", new String[] { "SimpleServlet.Default.properties", "SimpleServlet.Custom.properties", "SimpleServlet.Environment.properties" });
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    /**
	 * Processes a POST request from an applet client. Overrides parent
	 * method to set the protocol identifier to "HTTPSimple".
	 *
	 * @param request The HTTPServletRequest object provided by the HTTP server. It
	 * contains all FORM/QueryString values and a reference to any
	 * cookie sent from the browser.
	 * @param response The HTTPServletResponse object provided by the HTTP server or
	 * HTTP server extension. Provides access to a writer for the
	 * response.
	 * @exception javax.servlet.ServletException
	 * @exception java.io.IOException
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response, "HTTPSimple");
    }

    /**
	 * Processes a GET request from an applet client. Overrides parent
	 * method to set the protocol identifier to "HTTPSimple".
	 *
	 * @param request The HTTPServletRequest object provided by the HTTP server. It
	 * contains all FORM/QueryString values and a reference to any
	 * cookie sent from the browser.
	 * @param response The HTTPServletResponse object provided by the HTTP server or
	 * HTTP server extension. Provides access to a writer for the
	 * response.
	 * @exception javax.servlet.ServletException
	 * @exception java.io.IOException
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response, "HTTPSimple");
    }
}
