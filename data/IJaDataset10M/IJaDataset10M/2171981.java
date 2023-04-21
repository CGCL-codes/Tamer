package carsharing.web;

import carsharing.entity.Route;
import carsharing.session.DriverBean;
import carsharing.session.DriverLocal;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author compaq nx9030
 * @version
 */
public class EditTime extends HttpServlet {

    @EJB
    DriverLocal driverLocal;

    Tools tools;

    private Integer tryInsertUpdate(HttpServletRequest request) {
        Integer vacancy = tools.getIntParam("vacancy");
        if (vacancy == null || vacancy <= 0 || vacancy >= 1000) {
            request.setAttribute("vacancyError", true);
            return null;
        }
        String departureStr = request.getParameter("departure");
        DateFormat df = new SimpleDateFormat("yy-M-d H:m");
        Date departure;
        try {
            departure = tools.parseTime(departureStr, false);
        } catch (ParseException e) {
            request.setAttribute("departureError", true);
            return null;
        }
        Integer routeId = tools.getIntParam("routeId");
        if (routeId == null) {
            return driverLocal.insertRoute(tools.pushIntParam("pathId"), departure, vacancy);
        } else {
            if (tools.getUserId().equals(driverLocal.getRouteOwner(routeId))) driverLocal.updateRoute(routeId, departure, vacancy);
            return routeId;
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        tools = new Tools(request, response);
        if (tools.checkLogin(false)) {
            Integer userId = tools.getUserId();
            Integer pathId = tools.pushIntParam("pathId");
            if (pathId == null) {
                response.sendRedirect("ChoosePath");
                return;
            }
            Integer selectId = tools.getIntParam("select");
            Integer deleteId = tools.getIntParam("delete");
            if (selectId != null) {
                Route route = driverLocal.findRouteById(selectId);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:mm");
                request.setAttribute("routeId", selectId);
                request.setAttribute("departure", df.format(route.getDeparture()));
                request.setAttribute("vacancy", route.getVacancy());
            } else if (deleteId != null) {
                if (tools.getUserId().equals(driverLocal.getRouteOwner(deleteId))) driverLocal.deleteRoute(deleteId);
            } else {
                if (request.getParameter("routeId") != null) {
                    boolean successfulSave = tryInsertUpdate(request) != null;
                    if (!successfulSave) {
                        tools.pushParam("routeId");
                        tools.pushParam("departure");
                        tools.pushParam("vacancy");
                    }
                }
            }
            request.setAttribute("routeList", driverLocal.findAllRoutes(pathId));
            request.setAttribute("driverLocal", driverLocal);
            tools.doRedirect();
        }
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
