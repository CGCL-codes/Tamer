package web;

import api.Cache;
import api.CollectionManager;
import api.DatabaseManager;
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
public class UpdateCacheServlet extends HttpServlet {

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
        DatabaseManager dm = new DatabaseManager();
        CollectionManager cm = new CollectionManager("league-list", dm);
        try {
            request.setAttribute("xml_leagues", cm.getLeagueList());
            if (lid != null) {
                request.setAttribute("lid", lid);
            }
        } catch (BaseXException ex) {
            Logger.getLogger(RemoveLeagueServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", labelsBundle.getString("database_error"));
            request.getRequestDispatcher("/updateCache.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/updateCache.jsp").forward(request, response);
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
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Locale locale = request.getLocale();
        ResourceBundle labelsBundle;
        labelsBundle = ResourceBundle.getBundle("labelsBundle", locale);
        String lid = request.getParameter("lid");
        if (lid == null || lid.equals("")) {
            request.setAttribute("error", labelsBundle.getString("parameters_not_set"));
            request.getRequestDispatcher("/clearCache.jsp").forward(request, response);
            return;
        }
        DatabaseManager dm = new DatabaseManager();
        CollectionManager cm = new CollectionManager(lid, dm);
        Cache c = new Cache(dm);
        try {
            request.setAttribute("xml_leagues", cm.getLeagueList());
            c.updateCacheByLID(lid);
        } catch (BaseXException ex) {
            Logger.getLogger(AddLeagueServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", labelsBundle.getString("database_error"));
            request.getRequestDispatcher("/updateCache.jsp").forward(request, response);
            return;
        }
        request.setAttribute("success", labelsBundle.getString("cache_updated"));
        request.getRequestDispatcher("/updateCache.jsp").forward(request, response);
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
