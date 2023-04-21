package edu.univalle.lingweb.rest;

import java.io.IOException;
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
import edu.univalle.lingweb.model.DataManagerExerciseS1;
import edu.univalle.lingweb.model.Util;
import edu.univalle.lingweb.persistence.CoActivityDAO;
import edu.univalle.lingweb.persistence.CoExercises1;
import edu.univalle.lingweb.persistence.CoExercises1DAO;
import edu.univalle.lingweb.persistence.CoGroupFormatDAO;
import edu.univalle.lingweb.persistence.CoHabilityDAO;
import edu.univalle.lingweb.persistence.CoMomentumDAO;
import edu.univalle.lingweb.persistence.CoSingleFormatDAO;
import edu.univalle.lingweb.persistence.CoTechnicalDAO;
import edu.univalle.lingweb.persistence.CoWritingPhaseDAO;
import edu.univalle.lingweb.persistence.EntityManagerHelper;

/**
 * Controlador REST para todos los servicios Web REST de la tabla 'co_exercises1',
 * usando la direcci�n /rest/exerciseS1/* para el mapeo del servlet
 * 
 * @author Julio Cesar Puentes
 * 
 * @web.servlet name = "ExerciseS1Rest" display-name = "Servicio Web para las
 *              operaci�nes con la tabla 'co_exercises1' " load-on-startup = "1"
 * @web.servlet-mapping url-pattern =
 *                      "/edu/univalle/lingweb/lzx/rest/exerciseS1/*"
 */
public class ExerciseS1RestController extends BaseRestController {

    /**
	 * Manejador de mensajes Log's
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    protected Logger log = Logger.getLogger(ExerciseS1RestController.class);

    /**
	 * P�gina JSP usada para armar un XML con la lista de ejercicios tipo secuencia 1
	 */
    private static final String EXERCISE_S1_LIST_RESPONSE_JSP = Common.LINGWEBROOT + "/webservice/ExerciseS1List.jsp";

    /**
	 * P�gina JSP usada para armar un XML con la lista materiales para el formulario
	 */
    private static final String MATERIAL_FORM_LIST_RESPONSE_JSP = Common.LINGWEBROOT + "/webservice/MaterialFormList.jsp";

    /**
	 * Contiene los m�todos CRUD para ejercicios tipo secuencia 1
	 * @uml.property  name="dataManagerExerciseS1"
	 * @uml.associationEnd  
	 */
    protected DataManagerExerciseS1 dataManagerExerciseS1 = null;

    /**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DOMConfigurator.configure(UserRestController.class.getResource("/log4j.xml"));
        long nIni = System.currentTimeMillis();
        log.info("Par�metros Ejercicios S1...");
        printRequestParameter(request);
        sCommand = this.getRestMethod(request.getRequestURI(), request.getContextPath(), "exerciseS1");
        request.setAttribute("command", sCommand);
        serviceResult = new RestServiceResult();
        dataManagerExerciseS1 = new DataManagerExerciseS1();
        dataManagerExerciseS1.setBundle(this.loadResourceBundle(request));
        Object[] args = { sCommand };
        serviceResult.setMessage(MessageFormat.format(dataManagerExerciseS1.getBundle().getString("rest.unkownMethod"), args));
        log.info("sCommand: " + sCommand);
        if (sCommand.equals("create")) {
            serviceResult = load(serviceResult, request, false);
            if (serviceResult.isError()) {
            } else {
                String sDeliveryDate = request.getParameter("sDeliveryDate");
                CoExercises1 coExercises1 = (CoExercises1) serviceResult.getObjResult();
                if (Util.validateString(log, sDeliveryDate)) {
                    serviceResult = dataManagerExerciseS1.create(serviceResult, coExercises1, sDeliveryDate);
                    String sArrayMaterialId = request.getParameter("arrayMaterialId");
                    dataManagerExerciseS1.createExerciseS1Material(new RestServiceResult(), sArrayMaterialId, coExercises1);
                    String sExercises1CloneId = request.getParameter("exerciseCloneId");
                    if (Util.validateStringNumeric(log, sExercises1CloneId)) {
                        log.info("Clonaci�n EJERCICIOS T1....");
                        dataManagerExerciseS1.cloneExercises1(new Long(sExercises1CloneId), coExercises1.getExerciseId());
                    } else {
                        log.info("No es clonaci�n de Ejercicios T2");
                    }
                }
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("update")) {
            serviceResult = load(serviceResult, request, true);
            if (serviceResult.isError()) {
            } else {
                String sDeliveryDate = request.getParameter("sDeliveryDate");
                CoExercises1 coExercises1 = (CoExercises1) serviceResult.getObjResult();
                if (Util.validateString(log, sDeliveryDate)) {
                    serviceResult = dataManagerExerciseS1.update(serviceResult, coExercises1, sDeliveryDate);
                    String sArrayMaterialId = request.getParameter("arrayMaterialId");
                    dataManagerExerciseS1.createExerciseS1Material(new RestServiceResult(), sArrayMaterialId, coExercises1);
                }
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("delete")) {
            long nExerciseId = 0;
            String sExerciseId = null;
            sExerciseId = request.getParameter("exerciseId");
            try {
                Validate.notNull(sExerciseId);
                Validate.notEmpty(sExerciseId.trim());
                nExerciseId = Long.parseLong(sExerciseId);
                Validate.isTrue(nExerciseId > 0);
                serviceResult = dataManagerExerciseS1.search(serviceResult, nExerciseId);
                if (!serviceResult.isError()) {
                    List<CoExercises1> list = (List<CoExercises1>) serviceResult.getObjResult();
                    CoExercises1 coExercises1 = (CoExercises1) list.get(0);
                    serviceResult = dataManagerExerciseS1.delete(serviceResult, coExercises1);
                }
            } catch (IllegalArgumentException e) {
                serviceResult.setError(true);
                serviceResult.setMessage(dataManagerExerciseS1.getBundle().getString("exercises1.search.missingData"));
            }
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        } else if (sCommand.equals("search")) {
            long nExerciseId = 0;
            String sExerciseId = null;
            sExerciseId = request.getParameter("exerciseId");
            try {
                Validate.notNull(sExerciseId);
                Validate.notEmpty(sExerciseId.trim());
                nExerciseId = Long.parseLong(sExerciseId);
                Validate.isTrue(nExerciseId > 0);
                serviceResult = dataManagerExerciseS1.search(serviceResult, nExerciseId);
            } catch (IllegalArgumentException e) {
                serviceResult.setError(true);
                serviceResult.setMessage(dataManagerExerciseS1.getBundle().getString("exercises1.search.missingData"));
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(EXERCISE_S1_LIST_RESPONSE_JSP);
        } else if (sCommand.equals("list")) {
            String sRowStart = null;
            String sMaxResults = null;
            sRowStart = request.getParameter("rowStart");
            sMaxResults = request.getParameter("maxResults");
            if (sRowStart != null && sMaxResults != null) {
                serviceResult = dataManagerExerciseS1.list(serviceResult, new Integer(sRowStart), new Integer(sMaxResults));
            } else {
                serviceResult = dataManagerExerciseS1.list(serviceResult);
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(EXERCISE_S1_LIST_RESPONSE_JSP);
        } else if (sCommand.equals("listClone")) {
            String sRowStart = null;
            String sMaxResults = null;
            sRowStart = request.getParameter("rowStart");
            sMaxResults = request.getParameter("maxResults");
            if (sRowStart != null && sMaxResults != null) {
                serviceResult = dataManagerExerciseS1.listClone(serviceResult, new Integer(sRowStart), new Integer(sMaxResults));
            } else {
                serviceResult = dataManagerExerciseS1.listClone(serviceResult);
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(EXERCISE_S1_LIST_RESPONSE_JSP);
        } else if (sCommand.equals("listExerciseS1Material")) {
            String sExercise1Id = request.getParameter("exercise1Id");
            if (Util.validateStringNumeric(log, sExercise1Id)) {
                CoExercises1 coExercises1 = new CoExercises1DAO().findById(new Long(sExercise1Id));
                EntityManagerHelper.refresh(coExercises1);
                String sRequestPath = request.getRequestURL().toString();
                int nIndexOf = sRequestPath.indexOf("rest");
                String sPathServlet = sRequestPath.substring(0, nIndexOf - 1);
                request.setAttribute("pathServer", sPathServlet);
                List<CoExercises1> list = new ArrayList<CoExercises1>();
                list.add(coExercises1);
                serviceResult.setObjResult(list);
            } else {
                serviceResult.setMessage(dataManagerExerciseS1.getBundle().getString("exercises1.search.missingData"));
                serviceResult.setError(true);
            }
            if (serviceResult.isError()) dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP); else dispatcher = getServletContext().getRequestDispatcher(MATERIAL_FORM_LIST_RESPONSE_JSP);
        }
        if (dispatcher == null) {
            serviceResult.setError(true);
            dispatcher = getServletContext().getRequestDispatcher(USER_RESPONSE_JSP);
        }
        request.setAttribute("result", serviceResult);
        log.info("TERMINA CONSULTA " + (System.currentTimeMillis() - nIni) + " ms");
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
        String sActivityId = null;
        String sItem = null;
        String sExerciseName = null;
        String sStudentWorkTime = null;
        String sFlagExerciseScore = null;
        String sFlagScoreType = null;
        String sWritingPhaseId = null;
        String sMomentumId = null;
        String sStatement = null;
        String sSuggestions = null;
        String sHabilityId = null;
        String sTechnicalId = null;
        String sFlagWorkModality = null;
        String sSingleFormatId = null;
        String sGroupFormatId = null;
        String sDeliveryNumber = null;
        String sFlagPublish = null;
        String sFlagForum = null;
        String sForumDescription = null;
        String sMaterialEditor = null;
        String sExercise1CompleteId = null;
        CoExercises1 coExercises1 = null;
        sActivityId = request.getParameter("activityId");
        sItem = request.getParameter(CoExercises1DAO.ITEM);
        sExerciseName = request.getParameter(CoExercises1DAO.EXERCISE_NAME);
        sStudentWorkTime = request.getParameter(CoExercises1DAO.STUDENT_WORK_TIME);
        sFlagExerciseScore = request.getParameter(CoExercises1DAO.FLAG_EXERCISE_SCORE);
        sFlagScoreType = request.getParameter(CoExercises1DAO.FLAG_SCORE_TYPE);
        sMaterialEditor = request.getParameter(CoExercises1DAO.MATERIAL_EDITOR);
        sWritingPhaseId = request.getParameter("writingPhaseId");
        sMomentumId = request.getParameter("momentumId");
        sStatement = request.getParameter(CoExercises1DAO.STATEMENT);
        sSuggestions = request.getParameter(CoExercises1DAO.SUGGESTIONS);
        sHabilityId = request.getParameter("habilityId");
        sTechnicalId = request.getParameter("technicalId");
        sFlagWorkModality = request.getParameter(CoExercises1DAO.FLAG_WORK_MODALITY);
        sSingleFormatId = request.getParameter("singleFormatId");
        sGroupFormatId = request.getParameter("groupFormatId");
        sDeliveryNumber = request.getParameter(CoExercises1DAO.DELIVERY_NUMBER);
        sFlagPublish = request.getParameter(CoExercises1DAO.FLAG_PUBLISH);
        sFlagForum = request.getParameter(CoExercises1DAO.FLAG_FORUM);
        sForumDescription = request.getParameter(CoExercises1DAO.FORUM_DESCRIPTION);
        sExercise1CompleteId = request.getParameter(CoExercises1DAO.EXERCISE1_COMPLETE_ID);
        try {
            Validate.notNull(sItem);
            Validate.notNull(sExerciseName);
            Validate.notNull(sStudentWorkTime);
            Validate.notNull(sStatement);
            Validate.notNull(sFlagWorkModality);
            Validate.notNull(sDeliveryNumber);
            Validate.notEmpty(sItem.trim());
            Validate.notEmpty(sExerciseName.trim());
            Validate.notEmpty(sStudentWorkTime.trim());
            Validate.notEmpty(sStatement.trim());
            Validate.notEmpty(sFlagWorkModality.trim());
            Validate.notEmpty(sDeliveryNumber.trim());
            coExercises1 = new CoExercises1();
            if (isUpdate) coExercises1.setExerciseId((new Long(request.getParameter("exerciseId"))));
            coExercises1.setItem(new Long(sItem));
            coExercises1.setExerciseName(sExerciseName);
            coExercises1.setStudentWorkTime(new Long(sStudentWorkTime));
            coExercises1.setFlagExerciseScore(sFlagExerciseScore);
            coExercises1.setFlagScoreType(sFlagScoreType);
            coExercises1.setStatement(sStatement);
            coExercises1.setSuggestions(sSuggestions);
            coExercises1.setFlagWorkModality(sFlagWorkModality);
            coExercises1.setFlagClone(request.getParameter(CoExercises1DAO.FLAG_CLONE));
            coExercises1.setDeliveryNumber(new Long(sDeliveryNumber));
            coExercises1.setFlagPublish(sFlagPublish);
            coExercises1.setFlagForum(sFlagForum);
            coExercises1.setForumDescription(sForumDescription);
            coExercises1.setMaterialEditor(sMaterialEditor);
            if (sExercise1CompleteId != "" && sExercise1CompleteId != null) coExercises1.setExercise1CompleteId(new Long(sExercise1CompleteId));
            if (Util.validateStringNumeric(log, sWritingPhaseId)) coExercises1.setCoWritingPhase(new CoWritingPhaseDAO().findById(new Long(sWritingPhaseId)));
            if (Util.validateStringNumeric(log, sMomentumId)) coExercises1.setCoMomentum(new CoMomentumDAO().findById(new Long(sMomentumId)));
            if (Util.validateStringNumeric(log, sHabilityId)) coExercises1.setCoHability(new CoHabilityDAO().findById(new Long(sHabilityId)));
            if (Util.validateStringNumeric(log, sTechnicalId)) coExercises1.setCoTechnical(new CoTechnicalDAO().findById(new Long(sTechnicalId)));
            if (Util.validateStringNumeric(log, sSingleFormatId)) coExercises1.setCoSingleFormat(new CoSingleFormatDAO().findById(new Long(sSingleFormatId)));
            if (Util.validateStringNumeric(log, sGroupFormatId)) coExercises1.setCoGroupFormat(new CoGroupFormatDAO().findById(new Long(sGroupFormatId)));
            if (Util.validateStringNumeric(log, sActivityId)) coExercises1.setCoActivity(new CoActivityDAO().findById(new Long(sActivityId)));
        } catch (IllegalArgumentException e) {
            serviceResult.setError(true);
            serviceResult.setMessage(dataManagerExerciseS1.getBundle().getString("exercises1.create.missingData"));
            String sSqlTrace = e.getMessage() + " --  " + e.getStackTrace()[1];
            serviceResult.setSqlState(sSqlTrace);
            Util.printStackTrace(log, e.getStackTrace());
        }
        if (!serviceResult.isError()) serviceResult.setObjResult(coExercises1);
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
