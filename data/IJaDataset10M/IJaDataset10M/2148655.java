package si.cit.eprojekti.projectvianet.controller.projectstates;

import javax.servlet.ServletRequest;
import org.apache.log4j.Priority;
import si.cit.eprojekti.projectvianet.ProjectViaNetSchema;
import si.cit.eprojekti.projectvianet.controller.ProjectController;
import si.cit.eprojekti.projectvianet.dbobj.Project;
import si.cit.eprojekti.projectvianet.dbobj.ProjectSubjectList;
import si.cit.eprojekti.usermanagement.dbobj.NaturalSubject;
import si.cit.eprojekti.usermanagement.dbobj.OfficialSubject;
import si.cit.eprojekti.usermanagement.dbobj.Subject;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.Input;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.ServletControllerRequest;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.core.db.DBException;

/**
 * @author Luka Pavlic (luka.pavlic@cit.si)
 *
 * Created Sep 2, 2004 11:56:53 AM
 * 
 * ProcessAddSubjectState description:
 * 	Add subject to project
 */
public class ProcessAddSubjectState extends State {

    private static final long serialVersionUID = 23134546488887987L;

    /**
	 * Constructor
	 */
    public ProcessAddSubjectState() {
        super("ProcessAddSubjectState", "ProcessAddSubjectStateDescription");
        addRequiredParameter("subjectID");
        addRequiredParameter("subjectType");
        addRequiredParameter("projectID");
    }

    /**
	 * @see com.jcorporate.expresso.core.controller.State#run(com.jcorporate.expresso.core.controller.ControllerRequest, com.jcorporate.expresso.core.controller.ControllerResponse)
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        Transition errorTrans = new Transition();
        errorTrans.setControllerObject(ProjectViaNetSchema.class);
        errorTrans.setState(ErrorState.STATE_NAME);
        errorTrans.setName("errorTrans");
        setErrorTransition(errorTrans);
        try {
            Project pr = Project.getProjectByPID(Integer.parseInt(request.getParameter("projectID")));
            if (pr == null) throw new Exception(getString("ProjectNotFoundException"));
            Subject s = null;
            if (request.getParameter("subjectType").equals("O")) s = new OfficialSubject(); else s = new NaturalSubject();
            s.setField(Subject.FLD_ID, request.getParameter("subjectID"));
            if (!s.find()) throw new Exception("Subject nof found.");
            String subjectName = s.toString();
            response.add(new Output("outName", subjectName));
            if (request.getParameter("proceed") != null) {
                ProjectSubjectList psl = new ProjectSubjectList();
                psl.setField(ProjectSubjectList.FLD_PID, request.getParameter("projectID"));
                psl.setField(ProjectSubjectList.FLD_SID, request.getParameter("subjectID"));
                psl.setField(ProjectSubjectList.FLD_SID_TYPE, request.getParameter("subjectType"));
                psl.setField(ProjectSubjectList.FLD_DESC, request.getParameter("inRole"));
                psl.add();
                Transition tr = new Transition("viewProject", "viewProject", ProjectController.class, "ViewProjectDetailsState");
                tr.addParam("projectID", request.getParameter("projectID"));
                response.add(tr);
            } else {
                response.add(new Input("inRole", "inRole"));
                Transition tr = new Transition("transProceed", "transProceed", ProjectController.class, "ProcessAddSubjectState");
                tr.addParam("subjectID", request.getParameter("subjectID"));
                tr.addParam("subjectType", request.getParameter("subjectType"));
                tr.addParam("projectID", request.getParameter("projectID"));
                tr.addParam("proceed", "1");
                response.add(tr);
            }
        } catch (Exception e) {
            if (e instanceof DBException) addError("errors.DBException"); else if (e.getMessage().equals("errors.accessDeniedOccured")) addError("errors.accessDeniedOccured"); else addError("errors.Exception");
            if (ProjectViaNetSchema.standardLog.isEnabledFor(Priority.WARN)) ProjectViaNetSchema.standardLog.warn(" :: Exception in \"" + this.getName() + "\" : " + e.toString());
            if (ProjectViaNetSchema.debugLog.isDebugEnabled()) ProjectViaNetSchema.debugLog.debug(" :: Exception in \"" + this.getName() + "\" : " + e.toString(), e.fillInStackTrace());
        } finally {
            if (ProjectViaNetSchema.observerLog.isInfoEnabled()) {
                ServletRequest servletRq = ((ServletControllerRequest) request).getServletRequest();
                ProjectViaNetSchema.observerLog.info(" :: Location= " + this.getClass().getName() + " :: UID= " + request.getUid() + " :: User= " + request.getUser() + " :: IP= " + servletRq.getRemoteAddr());
            }
        }
    }
}
