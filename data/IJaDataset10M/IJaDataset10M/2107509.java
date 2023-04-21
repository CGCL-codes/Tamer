package manageroute;

import Database.ConnectDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author norbert
 */
@WebServlet(name = "ManagePkServlet", urlPatterns = { "/ManagePkServlet" })
public class ManagePkServlet extends HttpServlet {

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
        CallManagePage(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    @SuppressWarnings("static-access")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String command = request.getParameter("command");
        if (command != null) {
            if (command.equals("add")) {
                String kotaAsal = request.getParameter("kotaasal");
                String kotaTujuan = request.getParameter("kotatujuan");
                String tarif = request.getParameter("tarif");
                String error = DBPkt.insertDBPkt(kotaAsal, kotaTujuan, tarif);
                if (error != null) request.setAttribute("error", error);
            } else if (command.equals("edit")) {
                String idTrayek = request.getParameter("idTrayek");
                String kotaAsal = request.getParameter("kotaasal");
                String kotaTujuan = request.getParameter("kotatujuan");
                String tarif = request.getParameter("tarif");
                String error = DBPkt.updateDBPkt(idTrayek, kotaAsal, kotaTujuan, tarif);
                if (error != null) request.setAttribute("error", error);
            } else if (command.equals("delete")) {
                String idTrayek = request.getParameter("idTrayek");
                String error = DBPkt.deleteDBPkt(idTrayek);
                if (error != null) request.setAttribute("error", error);
            }
        }
        CallManagePage(request, response);
    }

    private void CallManagePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<DBPkt> dpktList = DBPkt.getAllRows();
        if (dpktList != null) {
            request.setAttribute("dpktList", dpktList);
        }
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ManagePk.jsp");
        dispatcher.forward(request, response);
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
