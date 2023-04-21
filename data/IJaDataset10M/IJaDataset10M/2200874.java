package cz.muni.fi.pb138.restaurant.servlets;

import cz.muni.fi.pb138.restaurant.Manager;
import cz.muni.fi.pb138.restaurant.Reservation;
import cz.muni.fi.pb138.restaurant.UserManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Demqoo
 */
public class MyProfile extends HttpServlet {

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
        try {
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
        HttpSession session = request.getSession(true);
        String email = (String) session.getAttribute("email");
        Manager manager = new Manager();
        UserManager userManager = manager.getUm();
        String rid = request.getParameter("reservationId");
        if (rid == null) {
            rid = "0";
        }
        int reservationId = Integer.parseInt(rid);
        if (reservationId != 0) {
            manager.deleteReservation(reservationId);
        }
        request.setAttribute("user", userManager.findUser(email));
        Collection<Reservation> reservations = userManager.allUsersReservations(userManager.findUser(email));
        request.setAttribute("reservations", reservations);
        request.getRequestDispatcher("/MyProfile.jsp").forward(request, response);
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
        HttpSession session = request.getSession(true);
        String email = (String) session.getAttribute("email");
        Manager manager = new Manager();
        UserManager userManager = manager.getUm();
        String rid = request.getParameter("reservationId");
        if (rid == null) {
            rid = "0";
        }
        int reservationId = Integer.parseInt(rid);
        if (reservationId != 0) {
            manager.deleteReservation(reservationId);
        }
        request.setAttribute("user", userManager.findUser(email));
        request.setAttribute("reservations", userManager.allUsersReservations(userManager.findUser(email)));
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
