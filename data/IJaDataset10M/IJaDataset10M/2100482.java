package control;

import control.dao.*;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Lea;

/**
 *
 * @author Nile
 */
public class LeaActionServlet extends HttpServlet {

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sess = request.getSession();
        String role = (String) sess.getAttribute("uel_role");
        if (role != null && !role.isEmpty()) {
            String action = request.getParameter("action");
            if (action.equalsIgnoreCase("view")) {
                viewLeas(sess, request, response, role);
                return;
            }
            sess.removeAttribute("info");
            if (action.equalsIgnoreCase("delete")) {
                if (!role.equalsIgnoreCase("Mentor")) {
                    deleteLea(sess, request, response);
                } else {
                    response.sendRedirect(response.encodeRedirectURL("main.jsp"));
                    return;
                }
            }
            if (action.equalsIgnoreCase("edit")) {
                editLea(sess, request, response, role);
                return;
            }
        } else {
            response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            return;
        }
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
        HttpSession sess = request.getSession();
        String role = (String) sess.getAttribute("uel_role");
        if (role != null && !role.isEmpty()) {
            String action = request.getParameter("action");
            if (action == null || action.isEmpty()) {
                response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            }
            sess.removeAttribute("info");
            if (action.equalsIgnoreCase("addnew")) {
                if (!role.equalsIgnoreCase("Mentor")) {
                    doLeaAdd(sess, request, response);
                } else {
                    response.sendRedirect(response.encodeRedirectURL("index.jsp"));
                }
            }
            if (action.equalsIgnoreCase("update")) {
                if (!role.equalsIgnoreCase("Mentor")) {
                    doUpdate(sess, request, response);
                    return;
                } else {
                    response.sendRedirect(response.encodeRedirectURL("index.jsp"));
                    return;
                }
            }
        } else {
            response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            return;
        }
    }

    protected void doLeaAdd(HttpSession sess, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Lea l = new Lea();
        String lname = request.getParameter("lname");
        String pnum = request.getParameter("lpnum");
        String laddr = request.getParameter("laddr");
        if (l == null) {
            request.getRequestDispatcher("/main.jsp").forward(request, response);
            return;
        }
        if (lname != null && !lname.isEmpty()) {
            l.setLname(lname);
        }
        if (pnum != null && !pnum.isEmpty()) {
            l.setLphonenum(pnum);
        }
        if (laddr != null && !laddr.isEmpty()) {
            l.setLaddr(laddr);
        }
        try {
            LeaActions lea = new LeaActions();
            lea.addNewLea(l);
            response.sendRedirect(response.encodeRedirectURL("leas?action=view"));
            return;
        } catch (SQLException e) {
            throw new ServletException("Error adding new LEA:", e);
        }
    }

    protected void doUpdate(HttpSession sess, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Lea l = (Lea) sess.getAttribute("selectedLea");
        String lname = request.getParameter("lname");
        String pnum = request.getParameter("lpnum");
        String laddr = request.getParameter("laddr");
        if (l == null) {
            request.getRequestDispatcher("/main.jsp").forward(request, response);
            return;
        }
        if (lname != null && !lname.isEmpty()) {
            l.setLname(lname);
        }
        if (pnum != null && !pnum.isEmpty()) {
            l.setLphonenum(pnum);
        }
        if (laddr != null && !laddr.isEmpty()) {
            l.setLaddr(laddr);
        }
        try {
            LeaActions lea = new LeaActions();
            lea.updateLea(l);
            System.out.println("Updated Record");
            response.sendRedirect(response.encodeRedirectURL("leas?action=view"));
            return;
        } catch (Exception e) {
            request.setAttribute("message", "Error updating Lea information");
            request.setAttribute("exception", e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    protected void viewLeas(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        if (role != null && !role.isEmpty()) {
            RequestDispatcher rd;
            LeaActions lea = new LeaActions();
            List<Lea> leas = lea.getAllLea();
            sess.setAttribute("leas", leas);
            if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser") || role.equalsIgnoreCase("Administrator")) {
                rd = request.getRequestDispatcher("/WEB-INF/Pages/AllLeas.jsp");
                rd.forward(request, response);
                return;
            }
        } else {
            response.sendRedirect(response.encodeRedirectURL("main.jsp"));
        }
    }

    protected void deleteLea(HttpSession sess, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String o = request.getParameter("leaID");
        if (o == null || o.isEmpty()) {
            response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            return;
        } else {
            Integer i = Integer.parseInt(o);
            LeaActions lea = new LeaActions();
            Lea l = lea.getLeaByID(i);
            try {
                lea.deleteLea(l);
            } catch (SQLException ex) {
                throw new ServletException("Error deleting LEA:", ex);
            }
            response.sendRedirect(response.encodeRedirectURL("leas?action=view"));
            return;
        }
    }

    protected void editLea(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        LeaActions lea = new LeaActions();
        int i = Integer.parseInt(request.getParameter("leaID"));
        Lea l = lea.getLeaByID(i);
        HttpSession session = request.getSession();
        session.setAttribute("selectedLea", l);
        RequestDispatcher rd;
        System.out.println(l);
        if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser") || role.equalsIgnoreCase("Administrator")) {
            rd = rd = request.getRequestDispatcher("/WEB-INF/Pages/EditLea.jsp");
            rd.forward(request, response);
            return;
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This Servlet controls actions pertaining to information held on Mentors";
    }
}
