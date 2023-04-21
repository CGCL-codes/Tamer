package com.jcorporate.expresso.core.jsdkapi;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet API 2.0 does not have the RequestDispatcher interface.
 * Apache-JServ is based on Servlet API 2.0.  For those users
 * that are using JServ, this interface allows the use of the
 * RequestDispatcher forward() and include() methods by
 * hiding the API differences between the Servlet API 2.0 and 2.1
 * This class is used in conjunction with the
 * com.jcorporate.expresso.core.jsdkapi.GenericDispatcher class.
 *
 * @version: $Revision: 3 $ $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 * @author  Shash Chatterjee
 */
public interface APIAwareDispatcher {

    /**
     * This method is used to forward to a new URL
     * @version: $Revision: 3 $ $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
     * @param   srv javax.servlet.Servlet - The servlet which is using this method,
     *          usually the "this" attribute. Used to extract the PrintWriter.
     * @param   ctx javax.servlet.ServletContext - The servlet context of the calling
     *          servlet.
     * @param   req javax.servlet.http.HttpServletRequest - The request object
     *          associated with this service call
     * @param   res javax.servlet.http.HttpServletResponse - The response object
     *          associated with this service call.
     * @param   URL java.lang.String - The URL to forward to
     * @throws  javax.servlet.ServletException The exception description.
     * @throws  java.io.IOException The exception description.
     */
    public void forward(Servlet srv, ServletContext ctx, HttpServletRequest req, HttpServletResponse res, String URL) throws javax.servlet.ServletException, java.io.IOException;

    /**
     * Insert the method's description here.
     * Creation date: (04/16/00 %r)
     * @param   srv javax.servlet.Servlet - The servlet which is using this method,
     *          usually the "this" attribute. Used to extract the PrintWriter.
     * @param   param javax.servlet.ServletContext - The servlet context of the calling
     *          servlet.
     * @param   req javax.servlet.http.ServletRequest - The request object associated
     *          with this service call
     * @param   res javax.servlet.http.ServletResponse - The response object associated
     *          with this service call.
     * @param   URL java.lang.String - The URL to include
     * @throws  javax.servlet.ServletException The exception description.
     * @throws  java.io.IOException The exception description.
     */
    public void include(Servlet srv, ServletContext param, HttpServletRequest req, HttpServletResponse res, String URL) throws javax.servlet.ServletException, java.io.IOException;

    /**
     * Retrieve the context path.
     *
     * @param   req the HttpServletRequest object
     * @return String for the context path
     */
    public String getContextPath(HttpServletRequest req);
}
