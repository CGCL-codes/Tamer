package web;

import api.CollectionManager;
import api.DatabaseManager;
import api.Statistics;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.basex.core.BaseXException;

/**
 *
 * @author Football-Statistics Team
 */
public class GetStandingsServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Locale locale = request.getLocale();
        ResourceBundle labelsBundle;
        labelsBundle = ResourceBundle.getBundle("labelsBundle", locale);
        String lid = request.getParameter("lid");
        String hg = request.getParameter("hg");
        String fh = request.getParameter("fh");
        if (lid == null || lid.trim().equals("") || hg == null || hg.trim().equals("") || fh == null || fh.trim().equals("")) {
            request.setAttribute("error", labelsBundle.getString("parameters_not_set"));
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        if ((!hg.equals("home") && !hg.equals("guests") && !hg.equals("both")) || (!fh.equals("full") && !fh.equals("half"))) {
            request.setAttribute("error", labelsBundle.getString("illegal_parameter"));
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        DatabaseManager dm = new DatabaseManager();
        Statistics stats = new Statistics(dm);
        CollectionManager cm = new CollectionManager(lid, dm);
        try {
            request.setAttribute("lid", lid);
            request.setAttribute("hg", hg);
            request.setAttribute("fh", fh);
            request.setAttribute("xml_menu", cm.getLeagueList());
            request.setAttribute("xml_standings", stats.getStandings(lid, hg, fh));
            request.getRequestDispatcher("/standings.jsp").forward(request, response);
            return;
        } catch (BaseXException ex) {
            Logger.getLogger(GetStandingsServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", labelsBundle.getString("database_error"));
            request.getRequestDispatcher("/index.jsp").forward(request, response);
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
