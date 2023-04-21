package controller;

import com.gesturn2.comercio.Empresa;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import mvc.model.UbicacionDireccion;
import mvc.model.Contacto;

/**
 *
 * @author Man
 */
public class RegistrarEmpresaAction extends Action implements Serializable {

    String next = "";

    HttpSession session = null;

    boolean sw = false;

    @Override
    public void run() throws ServletException, IOException {
        Empresa clie = new Empresa();
        Contacto contacto = new Contacto();
        UbicacionDireccion dir = new UbicacionDireccion();
        clie.setIdentificacion(request.getParameter("identificacion"));
        clie.setNombre(request.getParameter("nombre"));
        dir.setBarrio(request.getParameter("barrio"));
        dir.setUrbanizacion(request.getParameter("urb"));
        dir.setCiudad(request.getParameter("ciudad"));
        dir.setEstado(request.getParameter("estado_provincia"));
        dir.setCodigo_ostal(request.getParameter("codigo_postal"));
        dir.setPai(request.getParameter("pais"));
        clie.setTelefono(request.getParameter("telefono"));
        clie.setE_mail(request.getParameter("email"));
        contacto.setCedula(request.getParameter("cedula"));
        contacto.setNombre("primer_nombre");
        contacto.setSeundo_nombre("segundo_nombre");
        contacto.setApellido("apellido");
        contacto.setSegundo_apellido("segundo_apellido");
        contacto.setCargo("cargo");
        contacto.setTelefono("telefono");
        contacto.setE_mail("email_contacto");
        clie.setContacto(contacto);
        try {
            model.agregarContacto(contacto);
            model.agregarDireccion(dir);
            dir.setId(model.ultimoIDdireccioningresado());
            clie.setDireccion(dir);
            model.agregarEmpresa(clie);
        } catch (Exception ex) {
            throw new ServletException("Error al agregar Empresa: " + ex);
        }
        next = "/index -clientes_sedes.jsp";
        RequestDispatcher rd = application.getRequestDispatcher(next);
        if (rd == null) {
            throw new ServletException("No se pudo encontrar " + next);
        }
        rd.forward(request, response);
    }
}
