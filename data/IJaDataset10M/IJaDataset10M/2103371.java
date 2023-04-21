package org.apache.jetspeed.util.servlet;

import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import org.apache.turbine.util.RunData;
import org.apache.ecs.ConcreteElement;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;

/**
 * NOTE: The use of Ecs for aggregating portlet content is deprecated!
 *       This utility class will be removed once we don't have the ecs
 *       dependency any more.
 *
 * EcsServletElement encapsulates a servlet/JSP within the context of ECS
 * HTML-generation.
 *
 * This is a workaround to allow invoking servlets from JetSpeed Portlets.
 * The servlet will be invoked when traversal of an ECS tree during writing
 * reaches the EcsServlet element.
 */
public class EcsServletElement extends ConcreteElement {

    /**
     * Static initialization of the logger for this class
     */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(EcsServletElement.class.getName());

    /** RunData object to obtain HttpServletRequest/Response from. */
    private RunData rundata;

    /** URL of servlet to include */
    private String url;

    /**
    * Construct an ECS element from a given rundata object and URL.
    *
    * @param rundata Rundata object that holds the HttpServletRequest/Response
    *                objects to be used for servlet invocation.
    * @param url     The URL of the servlet to invoke.
    */
    public EcsServletElement(RunData rundata, String urlString) {
        this.url = urlString;
        this.rundata = rundata;
    }

    /** Builds the content of this element and output it in the
     *  passed OutputStream
     *
     * @param out the OutputStream to use for generating content
     */
    public void output(OutputStream out) {
        output(new PrintWriter(out));
    }

    /** Builds the content of this element and output it in the
     *  passed PrintWriter
     *
     * @param out the PrintWriter to use for generating content
     */
    public void output(PrintWriter out) {
        ServletContext ctx = rundata.getServletContext();
        RequestDispatcher dispatcher = ctx.getRequestDispatcher(url);
        try {
            dispatcher.include(rundata.getRequest(), rundata.getResponse());
        } catch (Exception e) {
            String message = "EcsServletElement: Could not include the following URL: " + url + " : " + e.getMessage();
            logger.error(message, e);
            out.print(message);
        }
    }
}
