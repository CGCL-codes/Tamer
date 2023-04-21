package controller;

import com.gesturn2.comercio.Servicio;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import mvc.model.Servicio_Dao1;

/**
 *
 * @author MAN
 */
public class ConsultarServicioAction extends Action implements Serializable {

    String next = "";

    HttpSession session = null;

    @Override
    public void run() throws ServletException, IOException {
        Servicio ser = new Servicio();
        Servicio_Dao1 servdao = new Servicio_Dao1();
        session = request.getSession(true);
        String id = "2";
        try {
            ser = (Servicio) servdao.extrae(id);
            session.setAttribute("servicio", ser);
        } catch (Exception ex) {
            throw new ServletException("Error al consultar empresa: " + ex);
        }
        next = "/index -listarservicio.jsp";
        RequestDispatcher rd = application.getRequestDispatcher(next);
        if (rd == null) {
            throw new ServletException("No se pudo encontrar " + next);
        }
        rd.forward(request, response);
    }
}
