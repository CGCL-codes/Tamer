package es.ait.horas.tareas;

import es.ait.horas.mvc.MVCCommand;
import es.ait.horas.proyectos.Proyecto;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class FormListaTareas extends MVCCommand {

    @Override
    protected String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TareaDAO dao = new TareaDAO();
        FiltroTareas filtro = new FiltroTareas();
        filtro.setActivo(true);
        filtro.setProyId((Proyecto) session.getAttribute("proyecto"));
        filtro.setTareaDescripcion(request.getParameter("descripcion"));
        dao.setEntityManager(em);
        request.setAttribute("TAREAS", dao.buscarTareas(filtro));
        return "/tareas/formListaTareas.jsp";
    }
}
