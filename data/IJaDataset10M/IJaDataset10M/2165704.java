package servlets;

import java.io.IOException;
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
public class cambiarFechaServlet extends HttpServlet {

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        try {
            String var = request.getParameter("var");
            if (var.equals("nuevaFecha")) {
                String fecha = request.getParameter("fechaNueva");
                String hora = request.getParameter("hora");
                String min = request.getParameter("minutos");
                String fechaCompleta = fecha + " " + hora + ":" + min;
                session.setAttribute("fechaSistema", fechaCompleta);
                request.setAttribute("message", "Se cambio la fecha del sistema");
                request.getRequestDispatcher("cambiarFecha.jsp").forward(request, response);
            } else if (var.equals("restablecer")) {
                session.removeAttribute("fechaSistema");
                request.setAttribute("message", "Se restablecion la fecha del sistema a la fecha actual");
                request.getRequestDispatcher("cambiarFecha.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("message", "Error" + ex.getMessage());
            request.getRequestDispatcher("cambiarFecha.jsp").forward(request, response);
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
