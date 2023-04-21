package org.netbeans.web;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import org.netbeans.modules.exceptions.utils.PersistenceUtils;

/**
 *
 * @author honza
 * @version
 */
public class Distinct extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Map params = Utils.getParamsFromRequest(request);
        String entity = request.getParameter("entity");
        String column = request.getParameter("column");
        try {
            Class entityClass = Class.forName("org.netbeans.modules.exceptions.entity." + entity);
            List sc = (List) PersistenceUtils.getInstance().getDistinct(entityClass, params, column);
            if (sc != null) {
                for (Iterator it = sc.iterator(); it.hasNext(); ) {
                    Long l = (Long) it.next();
                    if (l != null) {
                        out.print(l);
                        if (it.hasNext()) {
                            out.print(",");
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Invalid Entity Name", e);
        }
        out.close();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
