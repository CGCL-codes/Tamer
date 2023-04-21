package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javabeans.JBCine;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import persistencia.ISistema;

/**
 *
 * @author Bruno
 */
public class listadoCinesServlet extends HttpServlet {

    ISistema i;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ServletContext context = this.getServletContext();
            i = persistencia.Fabrica.getPersistenciaSistema(context);
        } catch (Exception exc) {
            throw new UnavailableException("No se puedo instanciar el servlet" + exc.toString());
        }
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        try {
            List<JBCine> cines = i.listarCines();
            session.setAttribute("cines", cines);
            response.sendRedirect("listadoCines.jsp");
        } catch (SQLException ex) {
            session.setAttribute("error", "Ha ocurrido un error " + ex.toString());
            request.getRequestDispatcher("listadoCines.jsp").forward(request, response);
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
