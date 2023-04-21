package Usuarios;

import java.io.IOException;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.servlet.http.*;
import Libros.PMF;
import java.util.List;

public class UsuarioServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(UsuarioServlet.class.getName());

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        String correo = req.getParameter("correo");
        String password = req.getParameter("password");
        String nombres = req.getParameter("nombres");
        String apellidos = req.getParameter("apellidos");
        String pais = req.getParameter("pais");
        boolean bool = false;
        String query = "select from " + Usuario.class.getName() + " order by correo asc";
        List<Usuario> ListaUsuarios = (List<Usuario>) pm.newQuery(query).execute();
        if (!ListaUsuarios.isEmpty()) {
            for (Usuario u : ListaUsuarios) {
                if (u.getCorreo().equalsIgnoreCase(correo)) {
                    bool = true;
                    break;
                }
            }
        }
        Usuario nuevousuario = new Usuario(correo, password, nombres, apellidos, pais);
        try {
            if (bool) {
                resp.sendRedirect("/registro_usuario.jsp");
            } else {
                pm.makePersistent(nuevousuario);
                resp.sendRedirect("/login.jsp");
            }
        } catch (Exception e) {
        } finally {
            pm.close();
        }
    }
}
