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
import edu.univalle.lingweb.model.DataManagerMaSpell;
import edu.univalle.lingweb.model.Util;
import edu.univalle.lingweb.persistence.MaSpell;
import edu.univalle.lingweb.persistence.MaSpellDAO;
import edu.univalle.lingweb.persistence.CoLanguage;
import edu.univalle.lingweb.persistence.CoLanguageDAO;
import edu.univalle.lingweb.persistence.MaPostag;
import edu.univalle.lingweb.persistence.MaPostagDAO;
import edu.univalle.lingweb.persistence.EntityManagerHelper;

/**
 * Controlador REST para todos los servicios Web REST de la tabla 'ma_spell', usando la direcci�n /rest/spell/* para el mapeo del servlet
 * 
 * @author Diana Carolina Rivera
 * 
 * @web.servlet name = "SpellRest" display-name = "Servicio Web para las operaci�nes con la tabla 'ma_spell' " load-on-startup = "1"
 * @web.servlet-mapping url-pattern = "/edu/univalle/lingweb/lzx/rest/spell/*"
 */
public class SpellRestController extends BaseRestController {

    /**
	 * Manejador de mensajes Log's
	 * 
	 * @uml.property name="log"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    protected Logger log = Logger.getLogger(SpellRestController.class);

    /**
	 * P�gina JSP usada para armar un XML con la lista de secuencias
	 */
    private static final String SPELL_LIST_RESPONSE_JSP = Common.LINGWEBROOT + "/webservice/SpellList.jsp";

    /**

	 * Contiene los m�todos CRUD del Spell
	 * 
	 * @uml.property name="dataManagerSpell"
	 * @uml.associationEnd
	 */
    protected DataManagerMaSpell dataManagerSpell = null;

    /**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DOMConfigurator.configure(SpellRestController.class.getResource("/log4j.xml"));
        log.info("Par�metros del Postag...");
        printRequestParameter(request);
        sCommand = this.getRestMethod(request.getRequestURI(), request.getContextPath(), "spell");
        request.setAttribute("command", sCommand);
        serviceResult = new RestServiceResult();
        dataManagerSpell = new DataManagerMaSpell();
        dataManagerSpell.setBundle(this.loadResourceBundle(request));
        Object[] args = { sCommand };
        serviceResult.setMessage(MessageFormat.format(dataManagerSpell.getBundle().getString("rest.unkownMethod"), args));
        log.info("sCommand: " + sCommand);
        if (sCommand.equals("create")) {
            serviceResult = load(serviceResult, request, false);
            if (serviceResult.isError()) {
            } else {
                MaSpell maSpell = (MaSpell) serviceResult.getObjResult();
                serviceResult = dataManagerSpell.create(serviceResult, maSpell);
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("update")) {
            serviceResult = load(serviceResult, request, true);
            if (serviceResult.isError()) {
            } else {
                MaSpell maSpell = (MaSpell) serviceResult.getObjResult();
                serviceResult = dataManagerSpell.update(serviceResult, maSpell);
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("delete")) {
            long nWordId = 0;
            String sWordId = null;
            sWordId = request.getParameter("wordId");
            try {
                Validate.notNull(sWordId);
                Validate.notEmpty(sWordId.trim());
                nWordId = Long.parseLong(sWordId);
                Validate.isTrue(nWordId > 0);
                serviceResult = dataManagerSpell.search(serviceResult, nWordId);
                if (!serviceResult.isError()) {
                    List<MaSpell> list = (List<MaSpell>) serviceResult.getObjResult();
                    MaSpell maSpell = (MaSpell) list.get(0);
                    serviceResult = dataManagerSpell.delete(serviceResult, maSpell);
                }
            } catch (IllegalArgumentException e) {
                serviceResult.setError(true);
                serviceResult.setMessage(dataManagerSpell.getBundle().getString("spell.search.missingData"));
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("search")) {
            long nWordId = 0;
            String sWordId = null;
            sWordId = request.getParameter("wordId");
            try {
                Validate.notNull(sWordId);
                Validate.notEmpty(sWordId.trim());
                nWordId = Long.parseLong(sWordId);
                Validate.isTrue(nWordId > 0);
                serviceResult = dataManagerSpell.search(serviceResult, nWordId);
            } catch (IllegalArgumentException e) {
                serviceResult.setError(true);
                serviceResult.setMessage(dataManagerSpell.getBundle().getString("sepll.search.missingData"));
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(SPELL_LIST_RESPONSE_JSP);
        } else if (sCommand.equals("list")) {
            String sRowStart = null;
            String sMaxResults = null;
            sRowStart = request.getParameter("rowStart");
            sMaxResults = request.getParameter("maxResults");
            if (sRowStart != null && sMaxResults != null) {
                serviceResult = dataManagerSpell.list(serviceResult, new Integer(sRowStart), new Integer(sMaxResults));
            } else {
                serviceResult = dataManagerSpell.list(serviceResult);
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(SPELL_LIST_RESPONSE_JSP);
        } else if (sCommand.equals("check")) {
            String sWordToCheck = null;
            String sLanguageId = null;
            sWordToCheck = request.getParameter("wordToCheck");
            sLanguageId = request.getParameter("languageId");
            if (sLanguageId != null && sWordToCheck != null) {
                serviceResult = dataManagerSpell.check(serviceResult, new Long(sLanguageId), sWordToCheck);
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(SPELL_LIST_RESPONSE_JSP);
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
        String sWordId = null;
        String sWord = null;
        String sPostagId = null;
        String sLanguageId = null;
        MaSpell maSpell = null;
        sWord = request.getParameter(MaSpellDAO.WORD);
        sPostagId = request.getParameter("postagId");
        sLanguageId = request.getParameter("languageId");
        try {
            Validate.notNull(sWord);
            Validate.notNull(sPostagId);
            Validate.notNull(sLanguageId);
            Validate.notEmpty(sWord.trim());
            Validate.notEmpty(sPostagId.trim());
            Validate.notEmpty(sLanguageId.trim());
            maSpell = new MaSpell();
            if (isUpdate) maSpell.setWordId((new Long(request.getParameter("wordId"))));
            maSpell.setWord(sWord);
            if (Util.validateStringNumeric(log, sPostagId)) maSpell.setMaPostag(new MaPostagDAO().findById(new Long(sPostagId)));
            if (Util.validateStringNumeric(log, sLanguageId)) maSpell.setCoLanguage(new CoLanguageDAO().findById(new Long(sLanguageId)));
        } catch (IllegalArgumentException e) {
            serviceResult.setError(true);
            serviceResult.setMessage(dataManagerSpell.getBundle().getString("postag.create.missingData"));
            String sSqlTrace = e.getMessage() + " --  " + e.getStackTrace()[1];
            serviceResult.setSqlState(sSqlTrace);
            Util.printStackTrace(log, e.getStackTrace());
        }
        if (!serviceResult.isError()) serviceResult.setObjResult(maSpell);
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
