package edu.univalle.lingweb.rest;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import edu.univalle.lingweb.Common;
import edu.univalle.lingweb.model.DataManagerMaPostag;
import edu.univalle.lingweb.model.Util;
import edu.univalle.lingweb.persistence.MaPostag;
import edu.univalle.lingweb.persistence.MaPostagDAO;
import edu.univalle.lingweb.persistence.CoLanguage;
import edu.univalle.lingweb.persistence.CoLanguageDAO;
import edu.univalle.lingweb.persistence.EntityManagerHelper;

/**
 * Controlador REST para todos los servicios Web REST de la tabla 'ma_postag', usando la direcci�n /rest/postag/* para el mapeo del servlet
 * 
 * @author Diana Carolina Rivera
 * 
 * @web.servlet name = "PostagRest" display-name = "Servicio Web para las operaci�nes con la tabla 'ma_postag' " load-on-startup = "1"
 * @web.servlet-mapping url-pattern = "/edu/univalle/lingweb/lzx/rest/postag/*"
 */
public class PostagRestController extends BaseRestController {

    /**
	 * Manejador de mensajes Log's
	 * 
	 * @uml.property name="log"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    protected Logger log = Logger.getLogger(PostagRestController.class);

    /**
	 * P�gina JSP usada para armar un XML con la lista de secuencias
	 */
    private static final String POSTAG_LIST_RESPONSE_JSP = Common.LINGWEBROOT + "/webservice/PostagList.jsp";

    /**

	 * Contiene los m�todos CRUD de actividades
	 * 
	 * @uml.property name="dataManagerPostag"
	 * @uml.associationEnd
	 */
    protected DataManagerMaPostag dataManagerPostag = null;

    /**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DOMConfigurator.configure(PostagRestController.class.getResource("/log4j.xml"));
        log.info("Par�metros del Postag...");
        printRequestParameter(request);
        sCommand = this.getRestMethod(request.getRequestURI(), request.getContextPath(), "postag");
        request.setAttribute("command", sCommand);
        serviceResult = new RestServiceResult();
        dataManagerPostag = new DataManagerMaPostag();
        dataManagerPostag.setBundle(this.loadResourceBundle(request));
        Object[] args = { sCommand };
        serviceResult.setMessage(MessageFormat.format(dataManagerPostag.getBundle().getString("rest.unkownMethod"), args));
        log.info("sCommand: " + sCommand);
        if (sCommand.equals("create")) {
            serviceResult = load(serviceResult, request, false);
            if (serviceResult.isError()) {
            } else {
                MaPostag maPostag = (MaPostag) serviceResult.getObjResult();
                serviceResult = dataManagerPostag.create(serviceResult, maPostag);
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("update")) {
            serviceResult = load(serviceResult, request, true);
            if (serviceResult.isError()) {
            } else {
                MaPostag maPostag = (MaPostag) serviceResult.getObjResult();
                serviceResult = dataManagerPostag.update(serviceResult, maPostag);
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("delete")) {
            long nPostagId = 0;
            String sPostagId = null;
            sPostagId = request.getParameter("postagId");
            try {
                Validate.notNull(sPostagId);
                Validate.notEmpty(sPostagId.trim());
                nPostagId = Long.parseLong(sPostagId);
                Validate.isTrue(nPostagId > 0);
                serviceResult = dataManagerPostag.search(serviceResult, nPostagId);
                if (!serviceResult.isError()) {
                    List<MaPostag> list = (List<MaPostag>) serviceResult.getObjResult();
                    MaPostag maPostag = (MaPostag) list.get(0);
                    serviceResult = dataManagerPostag.delete(serviceResult, maPostag);
                }
            } catch (IllegalArgumentException e) {
                serviceResult.setError(true);
                serviceResult.setMessage(dataManagerPostag.getBundle().getString("postag.search.missingData"));
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("search")) {
            long nPostagId = 0;
            String sPostagId = null;
            sPostagId = request.getParameter("postagId");
            try {
                Validate.notNull(sPostagId);
                Validate.notEmpty(sPostagId.trim());
                nPostagId = Long.parseLong(sPostagId);
                Validate.isTrue(nPostagId > 0);
                serviceResult = dataManagerPostag.search(serviceResult, nPostagId);
            } catch (IllegalArgumentException e) {
                serviceResult.setError(true);
                serviceResult.setMessage(dataManagerPostag.getBundle().getString("postag.search.missingData"));
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(POSTAG_LIST_RESPONSE_JSP);
        } else if (sCommand.equals("list")) {
            String sRowStart = null;
            String sMaxResults = null;
            sRowStart = request.getParameter("rowStart");
            sMaxResults = request.getParameter("maxResults");
            if (sRowStart != null && sMaxResults != null) {
                serviceResult = dataManagerPostag.list(serviceResult, new Integer(sRowStart), new Integer(sMaxResults));
            } else {
                serviceResult = dataManagerPostag.list(serviceResult);
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(POSTAG_LIST_RESPONSE_JSP);
        } else if (sCommand.equals("listByLanguage")) {
            long nLangugeId = 0;
            String sLangugeId = null;
            sLangugeId = request.getParameter("languageId");
            try {
                Validate.notNull(sLangugeId);
                Validate.notEmpty(sLangugeId.trim());
                nLangugeId = Long.parseLong(sLangugeId);
                Validate.isTrue(nLangugeId > 0);
                serviceResult = dataManagerPostag.listByLanguage(serviceResult, nLangugeId);
            } catch (IllegalArgumentException e) {
                serviceResult.setError(true);
                serviceResult.setMessage(dataManagerPostag.getBundle().getString("postag.search.missingData"));
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(POSTAG_LIST_RESPONSE_JSP);
        }
        request.setAttribute("result", serviceResult);
        dispatcher.forward(request, response);
    }

    /**
	 * Realiza la captura de par�metros y valores que proviene de la petici�n HTTP
	 * 
	 * @param request
	 *            petici�n http del cliente
	 * @param boolean
	 *            isUpdate true si es actualizaci�n.
	 * @return El {@link RestServiceResult} que contiene el resultado de la operaci�n.
	 */
    private RestServiceResult load(RestServiceResult serviceResult, HttpServletRequest request, boolean isUpdate) {
        String sPostagId = null;
        String sTag = null;
        String sDescription = null;
        String sLanguageId = null;
        MaPostag maPostag = null;
        sTag = request.getParameter(MaPostagDAO.TAG);
        sDescription = request.getParameter(MaPostagDAO.DESCRIPTION);
        sLanguageId = request.getParameter("languageId");
        try {
            Validate.notNull(sTag);
            Validate.notNull(sDescription);
            Validate.notNull(sLanguageId);
            Validate.notEmpty(sTag.trim());
            Validate.notEmpty(sDescription.trim());
            Validate.notEmpty(sLanguageId.trim());
            maPostag = new MaPostag();
            if (isUpdate) maPostag.setPostagId((new Long(request.getParameter("postagId"))));
            maPostag.setTag(sTag);
            maPostag.setDescription(sDescription);
            if (Util.validateStringNumeric(log, sLanguageId)) maPostag.setCoLanguage(new CoLanguageDAO().findById(new Long(sLanguageId)));
        } catch (IllegalArgumentException e) {
            serviceResult.setError(true);
            serviceResult.setMessage(dataManagerPostag.getBundle().getString("postag.create.missingData"));
            String sSqlTrace = e.getMessage() + " --  " + e.getStackTrace()[1];
            serviceResult.setSqlState(sSqlTrace);
            Util.printStackTrace(log, e.getStackTrace());
        }
        if (!serviceResult.isError()) serviceResult.setObjResult(maPostag);
        return serviceResult;
    }

    /**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
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
