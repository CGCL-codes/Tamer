package org.jxpfw.demo.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jxpfw.jsp.MimeTypes;
import org.jxpfw.servlet.CHttpServlet;

/**
 * A simple servlet, used to show some very basic Ajax capabilities.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 1.6 $
 */
public class Ajax101Servlet extends CHttpServlet {

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = 1207390247534836431L;

    /**
     * Processes the get request.
     * @param request The request.
     * @param response The response.
     * @throws ServletException When a servlet exception occurs.
     * @throws IOException When something goes wrong while creating output.
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String mimeType = request.getParameter("mimeType");
        response.setContentType(mimeType);
        response.setHeader("Cache-Control", "no-cache");
        final PrintWriter out = response.getWriter();
        if (MimeTypes.TEXT_PLAIN.equals(mimeType)) {
            out.print("Ajax 101 responding with plain text!");
        } else if (MimeTypes.TEXT_XML.equals(mimeType)) {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.print("<message>Ajax 101 responding with xml!</message>");
        }
    }
}
