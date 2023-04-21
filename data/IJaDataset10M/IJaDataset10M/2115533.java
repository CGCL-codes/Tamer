package com.mdiss.bolsatrabajo.web.users;

import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import com.mdiss.bolsatrabajo.bussines.ServiceFinder;
import com.mdiss.bolsatrabajo.model.Solicitud;
import com.mdiss.bolsatrabajo.model.Oferta;
import com.mdiss.bolsatrabajo.persistencia.SolicitudDAO;
import com.mdiss.bolsatrabajo.persistencia.OfertaDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: aitor
 * Date: 07-may-2007
 * Time: 14:03:50
 * To change this template use File | Settings | File Templates.
 */
public class RechazarSolicitudesAction extends Action {

    private Logger logger = Logger.getLogger(verOfertaAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Execute de rechazar solicitudes home");
        String id = (String) request.getParameter("id");
        SolicitudDAO solicitudDAO = (SolicitudDAO) ServiceFinder.getBean(request, "solicitudDAOTarget");
        Solicitud solicitud = (Solicitud) ServiceFinder.getBean(request, "solicitud");
        solicitud = solicitudDAO.buscar(Integer.parseInt(id));
        solicitud.setEstado("rechazada");
        solicitudDAO.insertar(solicitud);
        return mapping.findForward("success");
    }
}
