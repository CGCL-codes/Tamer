package Servlets;

import Beans.BeanAutenticacion;
import Beans.BeanError;
import Beans.BeanHome;
import Beans.BeanPublicacion;
import Beans.BeanSesionUsuario;
import EntidadesCompartidas.Comentario;
import EntidadesCompartidas.Productos_Servicios;
import EntidadesCompartidas.Publicacion;
import EntidadesCompartidas.Usuario;
import Logica.ILogicaFachada;
import Logica.LogicaFachada;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.String;

/**
 *
 * @author Andres
 */
public class ServletPublicacion extends HttpServlet {

    private HttpSession session;

    private BeanHome BHome;

    private BeanSesionUsuario sesionUsuario;

    private RequestDispatcher dis;

    private BeanPublicacion SessionPublicacion;

    private BeanAutenticacion BeanAut;

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
            session = request.getSession();
            sesionUsuario = (BeanSesionUsuario) session.getAttribute("beanSessionUsuario");
            SessionPublicacion = (BeanPublicacion) session.getAttribute("beanPublica");
            String strComentario = request.getParameter("hiddenComentario");
            String strIdpro = request.getParameter("hiddenIdProducto");
            String strIdCometarioEliminar = request.getParameter("hiddenIdComentarioDelete");
            String strAction = request.getParameter("hiddenAction");
            if (strAction.equalsIgnoreCase("Eliminar") && SessionPublicacion != null && strIdCometarioEliminar != null) {
                int idCom = Integer.parseInt(strIdCometarioEliminar);
                SessionPublicacion.DeleteComentarioById(idCom);
                SessionPublicacion.getPublicacionById(SessionPublicacion.getId());
                session.setAttribute("beanPublica", SessionPublicacion);
            }
            if (sesionUsuario != null && sesionUsuario.getUsuarioSesion() != null) {
                if ((sesionUsuario.getUsuarioSesion().isLoged()) && (strComentario != "") && (strIdpro != "")) {
                    Comentario Com = new Comentario();
                    Com.getPro().setId(Integer.parseInt(strIdpro));
                    Com.getUsu().setId(sesionUsuario.getUsuarioSesion().getId());
                    Com.setComentario(strComentario);
                    SessionPublicacion.InsertatComentario(Com);
                    SessionPublicacion.getPublicacionById(SessionPublicacion.getId());
                    session.setAttribute("beanPublica", SessionPublicacion);
                }
            }
            RequestDispatcher rd = request.getRequestDispatcher("/Publicacion.jsp");
            rd.forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ServletHome.class.getName()).log(Level.SEVERE, null, ex);
            BeanError BE = new BeanError();
            BE.setMessage(ex.getMessage());
            session = request.getSession();
            session.setAttribute("BeanError", BE);
            request.getSession().setAttribute("id", "sabe");
            String nextJSP = "/ErrorPage.jsp";
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
            dispatcher.forward(request, response);
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
        processRequest(request, response);
        try {
        } catch (Exception ex) {
            Logger.getLogger(ServletHome.class.getName()).log(Level.SEVERE, null, ex);
            BeanError BE = new BeanError();
            BE.setMessage(ex.getMessage());
            session = request.getSession();
            session.setAttribute("BeanError", BE);
            request.getSession().setAttribute("id", "sabe");
            String nextJSP = "/ErrorPage.jsp";
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
            dispatcher.forward(request, response);
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
