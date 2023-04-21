package edu.univalle.lingweb.rest;

import java.io.IOException;
import java.text.MessageFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import edu.univalle.lingweb.Common;
import edu.univalle.lingweb.model.DataManagerSingleParagraphTeacher;
import edu.univalle.lingweb.model.Util;
import edu.univalle.lingweb.persistence.CoExercises1DAO;
import edu.univalle.lingweb.persistence.CoParagraphTeacher;
import edu.univalle.lingweb.persistence.MaParagraphCheckListDAO;
import edu.univalle.lingweb.persistence.MaParagraphFormDAO;

/**
 * Controlador REST para todos los servicios Web REST de la tabla 'singleTextTeacher', usando la direcci�n
 * /rest/singleTextTeacher/* para el mapeo del servlet
 * 
 * @author Juan Pablo Rivera Velasco
 *
 * @web.servlet name = "SingleParagraphTeacherRest" display-name = "Servicio Web para las operaci�nes con la tabla 'singleParagraphTeacher' " load-on-startup = "1"
 * @web.servlet-mapping url-pattern = "/edu/univalle/lingweb/lzx/rest/singlePargraphTeacher/*"
 */
public class SingleParagraphTeacherRestController extends BaseRestController {

    /**
	 * Log4J logger.
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    protected Logger log = Logger.getLogger(KnowledgeFieldRestController.class);

    /**
      * P�gina JSP usada para armar un XML con la lista de t�cnicas
      */
    private static final String SINGLE_PARAGRAPH_TEACHER_LIST_RESPONSE_JSP = Common.LINGWEBROOT + "/webservice/SingleParagraphTeacherList.jsp";

    /**
	 * Contiene los m�todos CRUD de respuesta abierta
	 * @uml.property  name="dataManagerSingleTextTeacher"
	 * @uml.associationEnd  
	 */
    protected DataManagerSingleParagraphTeacher dataManagerSingleParagraphTeacher = null;

    /** 
      * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
      * @param request servlet request
      * @param response servlet response
      */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("estoy en el rest controler");
        DOMConfigurator.configure(UserRestController.class.getResource("/log4j.xml"));
        sCommand = this.getRestMethod(request.getRequestURI(), request.getContextPath(), "singleParagraphTeacher");
        request.setAttribute("command", sCommand);
        serviceResult = new RestServiceResult();
        dataManagerSingleParagraphTeacher = new DataManagerSingleParagraphTeacher();
        dataManagerSingleParagraphTeacher.setBundle(this.loadResourceBundle(request));
        Object[] args = { sCommand };
        serviceResult.setMessage(MessageFormat.format(dataManagerSingleParagraphTeacher.getBundle().getString("rest.unkownMethod"), args));
        log.info("sCommand: " + sCommand);
        if (sCommand.equals("create")) {
            log.info("estoy en el create del rest controler");
            serviceResult = load(serviceResult, request, false);
            if (serviceResult.isError()) {
            } else {
                CoParagraphTeacher singleParagraphTeacher = (CoParagraphTeacher) serviceResult.getObjResult();
                serviceResult = dataManagerSingleParagraphTeacher.create(serviceResult, singleParagraphTeacher);
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("update")) {
            serviceResult = load(serviceResult, request, true);
            if (serviceResult.isError()) {
            } else {
                CoParagraphTeacher singleParagraphTeacher = (CoParagraphTeacher) serviceResult.getObjResult();
                serviceResult = dataManagerSingleParagraphTeacher.update(serviceResult, singleParagraphTeacher);
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("search")) {
            String sExerciseId = request.getParameter("exerciseId").trim();
            if (Util.validateStringNumeric(log, sExerciseId)) {
                long nExerciseId = Long.valueOf(sExerciseId);
                serviceResult = dataManagerSingleParagraphTeacher.search(serviceResult, nExerciseId);
                log.info("exerciseId rest controller " + nExerciseId);
            } else {
                Object[] arrayParam = { sExerciseId };
                serviceResult.setError(true);
                serviceResult.setMessage(MessageFormat.format(dataManagerSingleParagraphTeacher.getBundle().getString("singleTextTeacher.search.errorData"), arrayParam));
            }
            if (serviceResult.isError()) {
                dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
            } else {
                dispatcher = getServletContext().getRequestDispatcher(SINGLE_PARAGRAPH_TEACHER_LIST_RESPONSE_JSP);
            }
        } else if (sCommand.equals("list")) {
            serviceResult = dataManagerSingleParagraphTeacher.list(serviceResult);
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(SINGLE_PARAGRAPH_TEACHER_LIST_RESPONSE_JSP);
        } else if (sCommand.equals("listbyTeacher")) {
            String nSingleParagraphTeacherId = request.getParameter("singlePargraphTeacherId");
            if (Util.validateStringNumeric(log, nSingleParagraphTeacherId)) {
                long nmiSingleTextTeacherId = Long.parseLong(nSingleParagraphTeacherId);
                serviceResult = dataManagerSingleParagraphTeacher.listbyTeacher(serviceResult, nmiSingleTextTeacherId);
            } else {
                Object[] arrayParam = { nSingleParagraphTeacherId };
                serviceResult.setError(true);
                serviceResult.setMessage(MessageFormat.format(dataManagerSingleParagraphTeacher.getBundle().getString("singleParagraphTeacher.search.errorData"), arrayParam));
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(SINGLE_PARAGRAPH_TEACHER_LIST_RESPONSE_JSP);
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
        String sExerciseId = null;
        String sTitleText = null;
        String sExplainText = null;
        String sEditorText = null;
        String sNumRows = null;
        String sSizeY = null;
        String sSizeX = null;
        String sCheckListFormId = null;
        CoParagraphTeacher singleParagraphTeacher = null;
        sExerciseId = request.getParameter("exerciseId");
        sTitleText = request.getParameter("titleText");
        sExplainText = request.getParameter("explainText");
        sEditorText = request.getParameter("editorText");
        sNumRows = request.getParameter("numRows");
        sSizeY = request.getParameter("sizeY");
        sSizeX = request.getParameter("sizeX");
        sCheckListFormId = request.getParameter("checkListFormId");
        log.info("sCheckListFormId: " + sCheckListFormId);
        log.info("sTitleText: " + sTitleText);
        log.info("sExplainText: " + sExplainText);
        log.info("sEditorText: " + sEditorText);
        log.info("sNumRows: " + sNumRows);
        log.info("sSizeY: " + sSizeY);
        log.info("sSizeX: " + sSizeX);
        log.info("sExerciseId: " + new Long(sExerciseId));
        log.info("singleParagraphTeacherId " + request.getParameter("singleParagraphTeacherId"));
        log.info("singleParagraphFormId " + request.getParameter("singleParagraphFormId"));
        log.info("estoy en el load del rest controler");
        try {
            singleParagraphTeacher = new CoParagraphTeacher();
            if (isUpdate) singleParagraphTeacher.setParagraphTeacherId(new Long(request.getParameter("singleParagraphTeacherId")));
            singleParagraphTeacher.setMaParagraphCheckList(new MaParagraphCheckListDAO().findById(new Long(sCheckListFormId.trim())));
            singleParagraphTeacher.setCoExercises1(new CoExercises1DAO().findById(new Long(sExerciseId.trim())));
            singleParagraphTeacher.setMaParagraphForm(new MaParagraphFormDAO().findById(new Long(request.getParameter("singleParagraphFormId"))));
            singleParagraphTeacher.setExplainText(new String(request.getParameter("explainText")));
            singleParagraphTeacher.setTitleText(new String(request.getParameter("titleText")));
            singleParagraphTeacher.setEditorText(new String(request.getParameter("editorText")));
            singleParagraphTeacher.setNumRows(new Long(request.getParameter("numRows")));
            singleParagraphTeacher.setSizeY(new Long(request.getParameter("sizeY")));
            singleParagraphTeacher.setSizeX(new Long(request.getParameter("sizeX")));
        } catch (IllegalArgumentException e) {
            serviceResult.setError(true);
            serviceResult.setMessage(dataManagerSingleParagraphTeacher.getBundle().getString("singleParagraphTeacher.search.missingData"));
            log.info("estoy en el error del load del rest controler");
            String sSqlTrace = e.getMessage() + " --  " + e.getStackTrace()[1];
            serviceResult.setSqlState(sSqlTrace);
            Util.printStackTrace(log, e.getStackTrace());
        }
        if (!serviceResult.isError()) serviceResult.setObjResult(singleParagraphTeacher);
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
