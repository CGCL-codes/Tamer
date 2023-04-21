package org.isurf.gdssu.dpconsumer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.isurf.gdssu.dpconsumer.bl.ConsumerHandler;
import org.isurf.gdssu.dpconsumer.bl.DbConnection;
import org.isurf.gdssu.dpconsumer.bl.MessageHandler;

/**
 *
 * @author nodari
 */
public class unSubscribeparty extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ConsumerHandler ch = new ConsumerHandler();
        try {
            MessageHandler mh = new MessageHandler();
            String operation = "DELETE";
            String glnsearch = request.getParameter("glnsearch");
            String gtin = "all gtin";
            String datapool = "55555";
            String ownergln = request.getParameter("ownergln");
            java.lang.String result = ch.subscribeParty(ownergln, glnsearch, datapool, gtin, operation);
            if (result.equals("unsubscribe")) {
                out.println("<h1> UnSubscription successfull</h1>");
                out.println("<label>Party GLN subscribe :</label><input type=\"text\" id=\"partygln\" value=\'" + glnsearch + "' class=\"inputbox\" disabled/>");
                out.println("<a href=\"./unsubscribe.jsp\">BACK</a>");
                DbConnection dbcon = new DbConnection();
                dbcon.deleteSubscriprion(ownergln, glnsearch);
            } else {
                out.println("<h1> UnSubscription Unsuccessfull</h1>");
                out.println("<a href=\"./globalsearch.jsp\">BACK</a>");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
