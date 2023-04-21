package com.mdiss.bolsatrabajo.web.users;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import com.mdiss.bolsatrabajo.model.Candidato;
import java.util.List;
import com.mdiss.bolsatrabajo.bussines.ServiceFinder;
import com.mdiss.bolsatrabajo.persistencia.CandidatoDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: aitor
 * Date: 07-may-2007
 * Time: 14:03:50
 * To change this template use File | Settings | File Templates.
 */
public class verCandidatoAction extends Action {

    private Logger logger = Logger.getLogger(verEmpresaAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Execute de ver candidato home");
        HttpSession sesion = request.getSession();
        String id = (String) sesion.getAttribute("username");
        logger.debug("El candidato a buscar: " + id);
        CandidatoDAO candidatoDAO = (CandidatoDAO) ServiceFinder.getBean(request, "candidatoDAOTarget");
        Candidato candidato = candidatoDAO.buscar(id);
        request.setAttribute("candidato", candidato);
        request.setAttribute("nombre", candidato.getNombre() + " " + candidato.getApellido1());
        logger.debug("Datos del candidato recuperados.");
        return mapping.findForward("success");
    }
}
