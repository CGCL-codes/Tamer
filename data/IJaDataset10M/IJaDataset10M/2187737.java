package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.util.ExceptionUtils;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.ErrorView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Redirects the user to a error page.
 *
 * @author    Simon Brown
 */
public class ErrorAction extends Action {

    /** the log used by this class */
    private static final Log log = LogFactory.getLog(ErrorAction.class);

    /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
    public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Exception e = (Exception) request.getAttribute("exception");
        if (e != null) {
            log.error("Request URL = " + request.getRequestURL());
            log.error("Request URI = " + request.getRequestURI());
            log.error("Query string = " + request.getQueryString());
            Enumeration en = request.getHeaderNames();
            while (en.hasMoreElements()) {
                String headerName = (String) en.nextElement();
                log.error(headerName + " = " + request.getHeader("headerName"));
            }
            log.error("Parameters :");
            java.util.Enumeration names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                log.error(" " + name + " = " + request.getParameter(name));
            }
            log.error(e);
            getModel().put("stackTrace", ExceptionUtils.getStackTraceAsString(e));
        }
        return new ErrorView();
    }
}
