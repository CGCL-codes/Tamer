package org.contineo.web.document;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contineo.core.document.DownloadTicket;
import org.contineo.core.document.dao.DownloadTicketDAO;
import org.contineo.util.Context;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This servlet is responsible for document downloads using download tickets. It
 * searches for the attribute ticketId in any scope and extracts the proper
 * document's content.
 *
 * @author Marco Meschieri
 * @version $Id: TicketDownload.java,v 1.3 2006/08/24 08:35:08 marco Exp $
 * @since 2.6
 */
public class TicketDownload extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 9088160958327454062L;

    protected static Log logger = LogFactory.getLog(TicketDownload.class);

    /**
     * Constructor of the object.
     */
    public TicketDownload() {
        super();
    }

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String ticketId = request.getParameter("ticketId");
        if (StringUtils.isEmpty(ticketId)) {
            ticketId = (String) request.getAttribute("ticketId");
        }
        if (StringUtils.isEmpty(ticketId)) {
            ticketId = (String) session.getAttribute("ticketId");
        }
        logger.debug("Download ticket ticketId=" + ticketId);
        try {
            DownloadTicketDAO ticketDao = (DownloadTicketDAO) Context.getInstance().getBean(DownloadTicketDAO.class);
            DownloadTicket ticket = ticketDao.findByPrimaryKey(ticketId);
            if ((ticket != null) && (ticket.getMenuId() != 0)) {
                DownloadDocUtil.downloadDocument(response, ticket.getMenuId(), null);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>Download Ticket Action</TITLE></HEAD>");
        out.println("  <BODY>");
        out.print("    This is ");
        out.print(this.getClass());
        out.println(", using the POST method");
        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }
}
