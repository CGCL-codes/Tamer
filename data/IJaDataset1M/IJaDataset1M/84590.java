package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.enjava.j2ee.master.aplicaciones.libreria.dao.LibreriaDAO;
import com.enjava.j2ee.master.aplicaciones.libreria.model.Libro;

public class DetalleLibroAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = new ActionForward();
        LibreriaDAO dao = (LibreriaDAO) request.getSession().getAttribute("dao");
        request.setAttribute("libros", dao);
        Libro libro = dao.getLibro(Long.parseLong(request.getParameter("id")));
        if (libro != null) {
            actionForward = mapping.findForward("ok");
            request.setAttribute("libro", libro);
        }
        return actionForward;
    }
}
