package org.jxpfw.servlet;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.josef.science.physics.TimeConstants;
import org.josef.util.CDebug;

/**
 * Base class for all HTTP based servlets.
 * @author Kees Schotanus
 * @version 1.1 $Revision: 1.12 $
 */
public abstract class CHttpServlet extends HttpServlet {

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = -6257996556408644206L;

    /**
     * Number of times destruction of a servlet with active requests is
     * attempted.
     */
    public static final int DESTROY_ATTEMPTS = 5;

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(CHttpServlet.class);

    /**
     * Number of active servlet requests.
     */
    private int activeServletRequests;

    /**
     * Determines whether this servlet is being destroyed (true) or not (false).
     */
    private boolean beingDestroyed;

    /**
     * One time initialization of the servlet.
     * <br>This method will automatically be called by the servletrunner after
     * the servlet has been loaded.
     * @param servletConfig Servlet configuration object.
     * @exception ServletException If a servlet exception occurs.
     */
    @Override
    public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }

    /**
     * This method will automatically be called when a user requests an action
     * from a servlet.
     * <br>This method patches the request through to the proper do... method,
     * but only when the servlet is not in the process of being destroyed.
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws IOException When an error occurs while writing to the PrintStream
     *  of the supplied response object.
     * @exception ServletException When a subclass throws this exception.
     */
    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        if (beingDestroyed) {
            return;
        }
        final long startTime = System.currentTimeMillis();
        try {
            ++activeServletRequests;
            super.service(request, response);
        } finally {
            --activeServletRequests;
        }
        final long duration = System.currentTimeMillis() - startTime;
        LOGGER.debug(this.getClass().getName() + " executed in:" + duration + "ms");
    }

    /**
     * Called by the servletrunner when this servlet has to be destroyed.
     * <br>Prevents the servletrunner from destroying this servlet when it is
     * actively servicing requests.
     */
    @Override
    public void destroy() {
        beingDestroyed = true;
        int destroyAttempt = 1;
        for (; activeServletRequests != 0 && destroyAttempt <= DESTROY_ATTEMPTS; ++destroyAttempt) {
            log("should not be destroyed due to " + activeServletRequests + " active requests. Sleeping ...");
            try {
                Thread.sleep(TimeConstants.MILLISECONDS_IN_A_SECOND);
            } catch (final InterruptedException ignore) {
                assert true;
            }
        }
        if (destroyAttempt == DESTROY_ATTEMPTS) {
            log("being destroyed with active requests!");
        }
        super.destroy();
    }

    /**
     * Returns information about the servlet.
     * @return Servlet-info.
     */
    @Override
    public String getServletInfo() {
        return "jxpfw framework by Kees Schotanus";
    }

    /**
     * Gets a required initialization parameter.
     * <br>You can still use:
     * {@link HttpServlet#getInitParameter(String)} for optional init
     * parameters.
     * <br>Note: Preferably call this method from within an init(...) method
     * since the UnavailableException that could be thrown is used by the
     * init(...) method to notify the user that the servlet is not available.
     * @param parameterName Name of the required init parameter.
     * @return Value belonging to the parameter.
     * @exception UnavailableException When the init parameter is missing or
     *  when parameterName is null.
     */
    protected String getRequiredInitParameter(final String parameterName) throws UnavailableException {
        String parameterValue = null;
        if (parameterName != null) {
            parameterValue = getInitParameter(parameterName);
        }
        if (parameterValue == null) {
            throw new UnavailableException("Init parameter:" + parameterName + ", is missing!");
        }
        return parameterValue;
    }

    /**
     * Gets the session information from the supplied request and converts it to
     * a single string.
     * @param request The request.
     * @return Session information as key=...,value=...:key=...,value=...
     *  or an empty String when the session does not contain any data or
     *  "Invalid session!" when the session is invalid.
     * @throws NullPointerException When the supplied request is null.
     */
    @SuppressWarnings("unchecked")
    public static String getSessionInfo(final HttpServletRequest request) {
        CDebug.checkParameterNotNull(request, "request");
        final HttpSession session = request.getSession(false);
        if (session == null) {
            return "Invalid session!";
        }
        final StringBuffer result = new StringBuffer();
        final Enumeration<String> valueNames = (Enumeration<String>) session.getAttributeNames();
        while (valueNames.hasMoreElements()) {
            final String valueName = valueNames.nextElement();
            final Object value = session.getAttribute(valueName);
            if (result.length() != 0) {
                result.append(':');
            }
            result.append("key=").append(valueName);
            result.append(",value=").append(value);
        }
        return result.toString();
    }

    /**
     * Clears all data from the supplied session.
     * <br>Note: this method clears data from the session but keeps the session
     * itself valid hence it is more gentle than session.invalidate().
     * @param session HttpSession to clear.
     * @since NullPointerException When the supplied session is null.
     */
    @SuppressWarnings("unchecked")
    public static void clearSessionData(final HttpSession session) {
        CDebug.checkParameterNotNull(session, "session");
        final Enumeration<String> valueNames = (Enumeration<String>) session.getAttributeNames();
        while (valueNames.hasMoreElements()) {
            session.removeAttribute(valueNames.nextElement());
        }
    }
}
