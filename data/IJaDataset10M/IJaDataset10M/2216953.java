package com.manning.junitbook.ch14.servlets;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * A sample SecurityFilter used to intercept requests and check whether the
 * command parameter is allowed SQL (i.e. only UPDATE queries).
 */
public class SecurityFilter implements Filter {

    /**
	 * The error page to redirect to, in case not allowed SQL was set.
	 */
    private String securityErrorPage;

    /**
	 * Filter's init method.
	 */
    public void init(FilterConfig theConfig) throws ServletException {
        this.securityErrorPage = theConfig.getInitParameter("securityErrorPage");
    }

    /**
	 * Filter's doFilter method. Checks whether the command parameter was a
	 * valid SQL.
	 */
    public void doFilter(ServletRequest theRequest, ServletResponse theResponse, FilterChain theChain) throws IOException, ServletException {
        String sqlCommand = theRequest.getParameter(AdminServlet.COMMAND_PARAM);
        if (!sqlCommand.startsWith("SELECT")) {
            RequestDispatcher dispatcher = theRequest.getRequestDispatcher(this.securityErrorPage);
            dispatcher.forward(theRequest, theResponse);
        } else {
            theChain.doFilter(theRequest, theResponse);
        }
    }

    /**
	 * Filter's destroy() method.
	 */
    public void destroy() {
    }
}
