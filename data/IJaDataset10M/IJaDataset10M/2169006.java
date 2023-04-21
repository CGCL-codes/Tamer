package carsharing.web;

import carsharing.session.PassengerLocal;
import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author compaq nx9030
 * @version
 */
public class ChooseRoute extends HttpServlet {

    @EJB
    private PassengerLocal passengerLocal;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Tools tools = new Tools(request, response);
        Date fromTime = null, toTime = null;
        try {
            fromTime = tools.parseTime(request.getParameter("fromTime"), false);
        } catch (ParseException e) {
        }
        try {
            toTime = tools.parseTime(request.getParameter("toTime"), true);
        } catch (ParseException e) {
        }
        request.setAttribute("fromTime", fromTime);
        request.setAttribute("toTime", toTime);
        Integer userId = tools.getUserId();
        Integer pathId = tools.pushIntParam("pathId");
        request.setAttribute("routeList", passengerLocal.findRoutesBetween(pathId, userId, fromTime, toTime));
        tools.doRedirect();
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
