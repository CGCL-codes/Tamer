package org.riverock.portlet.image;

import java.io.IOException;
import java.io.Writer;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.portlet.tools.RequestTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.tools.PortletService;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 12:32:51 PM
 *
 * $Id: ImageSelectFile.java,v 1.7 2006/04/22 12:22:07 serg_main Exp $
 */
public class ImageSelectFile extends HttpServlet {

    private static Logger log = Logger.getLogger(ImageSelectFile.class);

    public ImageSelectFile() {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (log.isDebugEnabled()) log.debug("method is POST");
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response) throws IOException, ServletException {
        Writer out = null;
        try {
            RenderRequest renderRequest = null;
            RenderResponse renderResponse = null;
            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);
            out = response.getWriter();
            AuthSession auth_ = (AuthSession) renderRequest.getUserPrincipal();
            if (auth_ == null) {
                throw new IllegalStateException("You have not enough right to execute this operation");
            }
            String index_page = PortletService.url("mill.image.index", renderRequest, renderResponse);
            if (auth_.isUserInRole("webmill.upload_image")) {
                PortletSession sess = renderRequest.getPortletSession(true);
                if (sess.getAttribute("MILL.IMAGE.ID_MAIN") == null) {
                    response.sendRedirect(index_page);
                    return;
                }
                sess.setAttribute("MILL.IMAGE.DESC_IMAGE", RequestTools.getString(renderRequest, "d", "none"));
                out.write("\r\n            ");
                out.write("" + ((Long) sess.getAttribute("MILL.IMAGE.ID_MAIN")));
                out.write("<br>\r\n            ");
                out.write(((String) sess.getAttribute("MILL.IMAGE.DESC_IMAGE")));
                out.write("<br>�������� ���� ��� ��������");
                out.write("<form method=\"post\" action=\"");
                out.write(PortletService.url("mill.image.upload_image", renderRequest, renderResponse));
                out.write("\" ENCTYPE=\"multipart/form-data\">\r\n");
                out.write("<p>\r\n");
                out.write("<input type=\"submit\" value=\"Submit\">\r\n");
                out.write("<br>\r\n");
                out.write("<input type=\"FILE\" name=\"f\" size=\"50\">\r\n");
                out.write("<br>\r\n");
                out.write("</p>\r\n");
                out.write("</form>\r\n            ");
            }
        } catch (Exception e) {
            log.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
    }
}
