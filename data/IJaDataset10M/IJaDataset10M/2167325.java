package com.malethan.seemorej.rendering;

import com.malethan.seemorej.context.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

/**
 * Rendering for views, by name. That it is, given the name of a view this class will determine what the JSP to
 * render should be based on the controller name, format (html, xml etc.) and name of the view.
 *
 * @author Elwyn Malethan
 */
public class ViewRendering implements Rendering {

    private static final Log log = LogFactory.getLog(ViewRendering.class);

    private String view;

    /**
     * Creates a new ViewRendering for the named view
     * 
     * @param view the name of the view to render
     */
    public ViewRendering(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    /**
     * First tries to find a JSP that is specific to the format being requested
     * (e.g. /WEB-INF/app/ctrl/index.html.jsp). If the JSP exists, the reqquest is forwarded to this JSP. Failing
     * that, an attempt is made to dispatch to a JSP without a format specific sub-extension
     * (e.g. /WEB-INF/app/ctrl/index.jsp)
     *
     * @param servletContext
     * @param seemoreContext
     * @throws IOException passess through from RequestDispatcher#forward()
     * @throws ServletException passess through from RequestDispatcher#forward()
     */
    public void render(ServletContext servletContext, Context seemoreContext) throws IOException, ServletException {
        String jspPath = getPathToJsp(servletContext, seemoreContext);
        servletContext.getRequestDispatcher(jspPath).forward(seemoreContext.getRequest(), seemoreContext.getResponse());
    }

    private String getPathToJsp(ServletContext servletContext, Context seemoreContext) {
        String jspPath = getFormatSpecificJspPath(servletContext, seemoreContext);
        if (jspCanBeRead(servletContext.getRealPath(jspPath))) {
            return jspPath;
        } else {
            log.warn(jspPath + " does not exist, using default JSP");
            return getDefaultJspPath(servletContext, seemoreContext);
        }
    }

    private String getFormatSpecificJspPath(ServletContext servletContext, Context seemoreContext) {
        return getViewPrefix(servletContext) + "/" + seemoreContext.getController() + "/" + view + "." + seemoreContext.getFormat() + "." + "jsp";
    }

    private String getDefaultJspPath(ServletContext servletContext, Context seemoreContext) {
        return getViewPrefix(servletContext) + "/" + seemoreContext.getController() + "/" + view + ".jsp";
    }

    protected boolean jspCanBeRead(String jspPath) {
        return new File(jspPath).canRead();
    }

    protected String getViewPrefix(ServletContext servletContext) {
        String pakage = servletContext.getInitParameter("viewPrefix");
        if (pakage == null || pakage.length() == 0) {
            pakage = "/WEB-INF/app";
        }
        return pakage;
    }
}
