package daw.controlador;

import daw.bean.Persona;
import daw.bean.RolUsuario;
import daw.bean.Usuario;
import daw.util.Util;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

/**
 *
 * @author Christian
 */
public class controladorUsuario extends HttpServlet {

    @PersistenceUnit
    EntityManagerFactory emf;

    @Resource
    UserTransaction utx;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String operacion = request.getParameter("operacion");
        if (operacion == null) {
            throw new ServletException("Operacion no ha sido definida.");
        } else if (operacion.equals("prepararParaConfigurar")) {
            EntityManager em = emf.createEntityManager();
            List l = em.createNamedQuery("RolUsuario.findAll").getResultList();
            request.setAttribute("roles", l);
            request.getRequestDispatcher("/servicio/protegido/configUsuario.jsp").forward(request, response);
        } else if (operacion.equals("actualizar")) {
            String nombres = request.getParameter("nombres");
            String apellidos = request.getParameter("apellidos");
            String domicilio = request.getParameter("domicilio");
            String telefono = request.getParameter("telefono");
            String cedula = request.getParameter("cedula");
            String contrasenia = request.getParameter("contrasenia");
            String contrasenia2 = request.getParameter("contrasenia2");
            String huellaDigital = request.getParameter("huelladigital");
            String huellaTemplate = request.getParameter("huellatemplate");
            String idRolUsuario = request.getParameter("idRolUsuario");
            try {
                utx.begin();
                EntityManager em = emf.createEntityManager();
                Usuario u = (Usuario) request.getSession().getAttribute("usuario");
                if (u.getPersona() == null) {
                    u.setPersona(new Persona(u.getIdUsuario()));
                }
                u.getPersona().setNombres(nombres);
                u.getPersona().setApellidos(apellidos);
                u.getPersona().setDomicilio(domicilio);
                u.getPersona().setTelefono(telefono);
                u.getPersona().setCedula(cedula);
                RolUsuario r = (RolUsuario) em.createNamedQuery("RolUsuario.findByIdRolUsuario").setParameter("idRolUsuario", Integer.valueOf(idRolUsuario)).getSingleResult();
                u.setRolUsuario(r);
                if (!contrasenia.isEmpty() && !contrasenia2.isEmpty()) {
                    if (contrasenia.equals(contrasenia2)) {
                        u.setPassword(contrasenia);
                    } else {
                        throw new Exception("Campos constraseña no coinciden.");
                    }
                }
                byte[] t1 = null, t2 = null;
                if (!huellaDigital.isEmpty()) {
                    t1 = Util.getStringToHuella(huellaDigital);
                    u.setHuellaDigital(t1);
                }
                if (!huellaTemplate.isEmpty()) {
                    t2 = Util.getStringToTemplate(huellaTemplate);
                    u.setHuellaTemplate(t2);
                }
                em.merge(u);
                utx.commit();
                request.getRequestDispatcher("/servicio/controladorUsuario?operacion=prepararParaConfigurar").forward(request, response);
            } catch (Exception ex) {
                try {
                    utx.rollback();
                } catch (Exception exe) {
                    throw new ServletException(exe);
                }
            }
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
