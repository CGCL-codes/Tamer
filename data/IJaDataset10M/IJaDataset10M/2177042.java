package es.ait.horas.tareas;

import es.ait.horas.asignaciones.FiltroAsignaciones;
import es.ait.horas.mvc.MVCCommand;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class FormEditarTarea extends MVCCommand {

    @Override
    protected String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = null;
        Tarea tarea;
        FiltroTareas filtro;
        FiltroTareas padre;
        TareaDAO dao = new TareaDAO();
        dao.setEntityManager(em);
        try {
            id = new Long(request.getParameter("id"));
        } catch (Exception e) {
        }
        filtro = new FiltroTareas();
        filtro.setTareaId(id);
        List<Tarea> lista = dao.buscarTareas(filtro);
        if (!lista.isEmpty()) {
            tarea = lista.get(0);
            request.setAttribute("tarea", lista.get(0));
            if (tarea.getTareaPadre() != null) {
                padre = new FiltroTareas();
                padre.setTareaId(tarea.getTareaPadre().getTareaId());
                request.setAttribute("nombrePadre", dao.buscarTareas(padre).get(0).getTareaNombre());
            }
            FiltroAsignaciones fasg = new FiltroAsignaciones();
            fasg.setTareaId(tarea);
            return "/tareas/formEditarTarea.jsp";
        } else {
            request.setAttribute("MENSAJE", "La tarea no existe");
            return "/tareas/respuesta.jsp";
        }
    }
}
