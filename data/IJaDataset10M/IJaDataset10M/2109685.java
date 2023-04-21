package it.inrich.forum;

import it.inrich.forum.*;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author riccardo
 * @version
 */
public class LogoutServlet extends HttpServlet {

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User us = (User) session.getAttribute("user");
        String goodbye = us.getFirstName() + " " + us.getLastName();
        System.err.println(goodbye);
        request.setAttribute("goodbye", goodbye);
        session.invalidate();
        getServletContext().getRequestDispatcher(response.encodeURL("/goodbye.jsp?goodbye=" + goodbye)).forward(request, response);
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
