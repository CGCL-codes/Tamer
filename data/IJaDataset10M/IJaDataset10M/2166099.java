package org.openedc.web;

import org.openedc.service.RssFeedRetrieverService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openedc.service.RssFeedWriterService;

/**
 *
 * @author peter
 */
@WebServlet(name = "RssFeedServlet", urlPatterns = { "/rss/events" })
public class RssFeedServlet extends HttpServlet {

    RssFeedWriterService rssFeedWriterService = new RssFeedWriterService();

    RssFeedRetrieverService rssFeedRetrieverService = new RssFeedRetrieverService();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            rssFeedWriterService.write(rssFeedRetrieverService.retrieveFeed(), out);
            out.flush();
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            out.close();
        }
    }

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
