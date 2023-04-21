package org.apache.myfaces.trinidadinternal.config.xmlHttp;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidadinternal.application.StateManagerImpl;
import org.apache.myfaces.trinidadinternal.renderkit.core.ppr.XmlResponseWriter;

/**
 * Though a configurator in spirit, at this point it purely exposes
 * Servlet functionality, and is only used to wrap the servlet response.
 * 
 * TODO: support portlets, and make this a true configurator.
 */
public class XmlHttpConfigurator {

    public XmlHttpConfigurator() {
    }

    public static ServletRequest getAjaxServletRequest(ServletRequest request) {
        return new XmlHttpServletRequest(request);
    }

    public static void beginRequest(ExternalContext externalContext) {
        StateManagerImpl.reuseRequestTokenForResponse(externalContext);
        Object response = externalContext.getResponse();
        if (response instanceof ServletResponse) {
            externalContext.setResponse(new XmlHttpServletResponse((ServletResponse) response));
        }
    }

    /**
   * Sends a <redirect> element to the server
   */
    static void __sendRedirect(final PrintWriter writer, final String url) throws IOException {
        XmlResponseWriter rw = new XmlResponseWriter(writer, "UTF-8");
        rw.startDocument();
        rw.write("<?Tr-XHR-Response-Type ?>\n");
        rw.startElement("redirect", null);
        rw.writeText(url, null);
        rw.endElement("redirect");
        rw.endDocument();
        rw.close();
    }

    /**
   * Handle a server-side error by reporting it back to the client.
   * TODO: add configuration to hide this in a production
   * environment.
   */
    public static void handleError(ExternalContext ec, Throwable t) throws IOException {
        String error = _getErrorString();
        _LOG.severe(error, t);
        ServletResponse response = (ServletResponse) ec.getResponse();
        PrintWriter writer = response.getWriter();
        XmlResponseWriter rw = new XmlResponseWriter(writer, "UTF-8");
        rw.startDocument();
        rw.write("<?Tr-XHR-Response-Type ?>\n");
        rw.startElement("error", null);
        rw.writeAttribute("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null);
        rw.writeText(_getExceptionString(t) + _PLEASE_SEE_ERROR_LOG + error, null);
        rw.endElement("error");
        rw.endDocument();
        rw.close();
    }

    private static String _getExceptionString(Throwable t) {
        while (_isUninterestingThrowable(t)) {
            Throwable cause = t.getCause();
            if (cause == null) break;
            t = cause;
        }
        String message = t.getMessage();
        if ((message == null) || "".equals(message)) message = t.getClass().getName();
        return message + "\n\n";
    }

    /**
   * Unwrap a bunch of "uninteresting" throwables
   */
    private static boolean _isUninterestingThrowable(Throwable t) {
        return ((t instanceof ServletException) || (t instanceof JspException) || (t instanceof FacesException) || (t instanceof InvocationTargetException));
    }

    private static String _getErrorString() {
        return _PPR_ERROR_PREFIX + _getErrorCount();
    }

    private static synchronized int _getErrorCount() {
        return (++_ERROR_COUNT);
    }

    private static final String _PPR_ERROR_PREFIX = "Server Exception during PPR, #";

    private static final String _PLEASE_SEE_ERROR_LOG = "For more information, please see the server's error log for\n" + "an entry beginning with: ";

    private static int _ERROR_COUNT = 0;

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(XmlHttpConfigurator.class);
}
