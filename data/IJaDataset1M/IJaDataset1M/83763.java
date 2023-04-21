package cea;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistroUsuarioServlet
 */
public class RegistroUsuarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html><head><title>TIENDA</title>" + "<link rel='stylesheet' type='text/css' href='css/estilos.css' />" + "</head><body>");
        writer.println("<h2>Registro de usuario</h2><br/>");
        if (request.getParameter("salir") != null) {
            request.getSession().invalidate();
        }
        if (request.getParameter("usuario") != null) {
            request.getSession().setAttribute(AtributosConstantes.usuario.toString(), request.getParameter("usuario"));
            String peticionActual = (String) request.getSession().getAttribute(AtributosConstantes.peticionActual.toString());
            if (peticionActual != null) {
                request.getRequestDispatcher(peticionActual).forward(request, response);
            }
        }
        if (request.getSession().getAttribute("nombreUsuario") == null) {
            writer.println("<form action='./registro' method='POST'>" + "Usuario: <input type='text' name='usuario'/><br>" + "Password: <input type='text' name='password'/>" + "<br><br><input type='submit' name='Enviar'/></form>");
        } else {
            writer.println("Usuario registrado con usuario " + request.getSession().getAttribute("nombreUsuario"));
            writer.println("<br/><a href='./registro?salir=true'>Cerrar sesi&oacute;n</a>");
            writer.println("<br/><a href='./catalogo'>Volver al catalogo</a>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
