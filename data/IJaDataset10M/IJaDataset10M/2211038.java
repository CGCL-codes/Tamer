package edu.univalle.lingweb.rest;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import edu.univalle.lingweb.Common;
import edu.univalle.lingweb.model.DataManagerTechnical;
import edu.univalle.lingweb.model.Util;
import edu.univalle.lingweb.persistence.CoHabilityDAO;
import edu.univalle.lingweb.persistence.CoTechnical;
import edu.univalle.lingweb.persistence.CoTechnicalDAO;

/**
 * Controlador REST para todos los servicios Web REST de la tabla 'co_technical', usando la direcci�n
 * /rest/technical/* para el mapeo del servlet
 * 
 * @author Julio Cesar Puentes
 *
 * @web.servlet name = "TechnicalRest" display-name = "Servicio Web para las operaci�nes con la tabla 'co_technical' " load-on-startup = "1"
 * @web.servlet-mapping url-pattern = "/edu/univalle/lingweb/lzx/rest/technical/*"
 */
public class TechnicalRestController extends BaseRestController {

    /**
	 * Log4J logger.
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    protected Logger log = Logger.getLogger(KnowledgeFieldRestController.class);

    /**
      * P�gina JSP usada para armar un XML con la lista de t�cnicas
      */
    private static final String TECHNICAL_LIST_RESPONSE_JSP = Common.LINGWEBROOT + "/webservice/TechnicalList.jsp";

    /**
	 * Contiene los m�todos CRUD de t�cnicas
	 * @uml.property  name="dataManagerTechnical"
	 * @uml.associationEnd  
	 */
    protected DataManagerTechnical dataManagerTechnical = null;

    /** 
      * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
      * @param request servlet request
      * @param response servlet response
      */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DOMConfigurator.configure(UserRestController.class.getResource("/log4j.xml"));
        sCommand = this.getRestMethod(request.getRequestURI(), request.getContextPath(), "technical");
        request.setAttribute("command", sCommand);
        serviceResult = new RestServiceResult();
        dataManagerTechnical = new DataManagerTechnical();
        dataManagerTechnical.setBundle(this.loadResourceBundle(request));
        Object[] args = { sCommand };
        serviceResult.setMessage(MessageFormat.format(dataManagerTechnical.getBundle().getString("rest.unkownMethod"), args));
        log.info("sCommand: " + sCommand);
        if (sCommand.equals("create")) {
            serviceResult = load(serviceResult, request, false);
            if (serviceResult.isError()) {
            } else {
                CoTechnical coTechnical = (CoTechnical) serviceResult.getObjResult();
                serviceResult = dataManagerTechnical.create(serviceResult, coTechnical);
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("update")) {
            serviceResult = load(serviceResult, request, true);
            if (serviceResult.isError()) {
            } else {
                CoTechnical coTechnical = (CoTechnical) serviceResult.getObjResult();
                serviceResult = dataManagerTechnical.update(serviceResult, coTechnical);
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("search")) {
            String sTechnicalId = request.getParameter("technicalId");
            if (Util.validateStringNumeric(log, sTechnicalId)) {
                long nTechnicalId = Long.valueOf(sTechnicalId);
                serviceResult = dataManagerTechnical.search(serviceResult, nTechnicalId);
            } else {
                Object[] arrayParam = { sTechnicalId };
                serviceResult.setError(true);
                serviceResult.setMessage(MessageFormat.format(dataManagerTechnical.getBundle().getString("technical.search.errorData"), arrayParam));
            }
            if (serviceResult.isError()) {
                dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
            } else {
                dispatcher = getServletContext().getRequestDispatcher(TECHNICAL_LIST_RESPONSE_JSP);
            }
        } else if (sCommand.equals("delete")) {
            String sTechnicalId = request.getParameter("technicalId");
            try {
                Validate.notNull(sTechnicalId);
                Validate.notEmpty(sTechnicalId.trim());
                long nTechnicalId = Long.valueOf(sTechnicalId);
                Validate.isTrue(nTechnicalId > 0);
                serviceResult = dataManagerTechnical.search(serviceResult, nTechnicalId);
                if (!serviceResult.isError()) {
                    List<CoTechnical> list = (List<CoTechnical>) serviceResult.getObjResult();
                    CoTechnical coTechnical = (CoTechnical) list.get(0);
                    serviceResult = dataManagerTechnical.delete(serviceResult, coTechnical);
                }
            } catch (IllegalArgumentException e) {
                Object[] arrayParam = { sTechnicalId };
                serviceResult.setError(true);
                serviceResult.setMessage(MessageFormat.format(dataManagerTechnical.getBundle().getString("technical.search.errorData"), arrayParam));
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("list")) {
            serviceResult = dataManagerTechnical.list(serviceResult);
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(TECHNICAL_LIST_RESPONSE_JSP);
        } else if (sCommand.equals("listTechnicalForHability")) {
            String sHabilityId = request.getParameter("habilityId");
            if (Util.validateStringNumeric(log, sHabilityId)) {
                long nHabilityId = Long.parseLong(sHabilityId);
                serviceResult = dataManagerTechnical.listTechnicalForHability(serviceResult, nHabilityId);
            } else {
                Object[] arrayParam = { sHabilityId };
                serviceResult.setError(true);
                serviceResult.setMessage(MessageFormat.format(dataManagerTechnical.getBundle().getString("technical.search.errorData"), arrayParam));
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(TECHNICAL_LIST_RESPONSE_JSP);
        }
        request.setAttribute("result", serviceResult);
        dispatcher.forward(request, response);
    }

    /**
 	 * Realiza la captura de par�metros y valores que proviene de la petici�n
 	 * HTTP
 	 * 
 	 * @param request
 	 *            petici�n http del cliente
 	 * @param boolean
 	 *            isUpdate true si es actualizaci�n.
 	 * @return El {@link RestServiceResult} que contiene el resultado de la
 	 *         operaci�n.
 	 */
    private RestServiceResult load(RestServiceResult serviceResult, HttpServletRequest request, boolean isUpdate) {
        String sTechnicalName = null;
        String sTechnicalNameEn = null;
        String sTechnicalNameFr = null;
        CoTechnical coTechnical = null;
        sTechnicalName = request.getParameter(CoTechnicalDAO.TECHNICAL_NAME);
        sTechnicalNameEn = request.getParameter(CoTechnicalDAO.TECHNICAL_NAME_EN);
        sTechnicalNameFr = request.getParameter(CoTechnicalDAO.TECHNICAL_NAME_FR);
        try {
            Validate.notNull(sTechnicalName);
            Validate.notNull(sTechnicalNameEn);
            Validate.notNull(sTechnicalNameFr);
            Validate.notEmpty(sTechnicalName.trim());
            Validate.notEmpty(sTechnicalNameEn.trim());
            Validate.notEmpty(sTechnicalNameFr.trim());
            coTechnical = new CoTechnical();
            if (isUpdate) coTechnical.setTechnicalId(new Long(request.getParameter("technicalId")));
            coTechnical.setTechnicalName(sTechnicalName);
            coTechnical.setTechnicalNameEn(sTechnicalNameEn);
            coTechnical.setTechnicalNameFr(sTechnicalNameFr);
            if (Util.validateStringNumeric(log, request.getParameter("habilityId"))) coTechnical.setCoHability(new CoHabilityDAO().findById(new Long(request.getParameter("habilityId"))));
        } catch (IllegalArgumentException e) {
            serviceResult.setError(true);
            serviceResult.setMessage(dataManagerTechnical.getBundle().getString("technical.search.missingData"));
            String sSqlTrace = e.getMessage() + " --  " + e.getStackTrace()[1];
            serviceResult.setSqlState(sSqlTrace);
            Util.printStackTrace(log, e.getStackTrace());
        }
        if (!serviceResult.isError()) serviceResult.setObjResult(coTechnical);
        return serviceResult;
    }

    /** 
      * Handles the HTTP <code>GET</code> method.
      * @param request servlet request
      * @param response servlet response
      */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
      * Handles the HTTP <code>POST</code> method.
      * @param request servlet request
      * @param response servlet response
      */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
      * Returns a short description of the servlet.
      */
    public String getServletInfo() {
        return "Short description";
    }
}
