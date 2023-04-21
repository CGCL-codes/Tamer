package com.mdiss.bolsatrabajo.web.users;

import java.sql.Date;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import com.mdiss.bolsatrabajo.bussines.ServiceFinder;
import com.mdiss.bolsatrabajo.model.Oferta;
import com.mdiss.bolsatrabajo.model.Solicitud;
import com.mdiss.bolsatrabajo.persistencia.OfertaDAO;
import com.mdiss.bolsatrabajo.persistencia.SolicitudDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ApuntarseCandidatoAction extends Action {

    private Logger logger = Logger.getLogger(ApuntarseCandidatoAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ApuntarseCandidatoAction");
        int idOferta = Integer.parseInt((String) request.getParameter("id"));
        HttpSession session = request.getSession();
        String idCandidato = (String) session.getAttribute("username");
        Date fecha = new Date(System.currentTimeMillis());
        Solicitud solicitud = (Solicitud) ServiceFinder.getBean(request, "solicitud");
        solicitud.setIdOferta(idOferta);
        solicitud.setIdCandidato(idCandidato);
        solicitud.setFecha(fecha);
        solicitud.setEstado("en curso");
        SolicitudDAO solicitudDAO = (SolicitudDAO) ServiceFinder.getBean(request, "solicitudDAOTarget");
        OfertaDAO ofertaDAO = (OfertaDAO) ServiceFinder.getBean(request, "ofertaDAOTarget");
        Oferta oferta = ofertaDAO.buscar(solicitud.getIdOferta());
        request.setAttribute("oferta", oferta);
        if (!solicitudDAO.localizar(solicitud.getIdOferta(), solicitud.getIdCandidato())) {
            solicitudDAO.insertar(solicitud);
            request.setAttribute("mensaje", "Te has apuntado a la oferta!!!");
        } else {
            request.setAttribute("mensaje", "Ya est�s apuntado en esta oferta!!!");
        }
        ;
        logger.debug("exito");
        return mapping.findForward("success");
    }
}
