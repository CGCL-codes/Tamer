package si.cit.eprojekti.ebug.controller.bugstates;

import javax.servlet.ServletRequest;
import org.apache.log4j.Priority;
import si.cit.eprojekti.ebug.controller.BugController;
import si.cit.eprojekti.ebug.dbobj.Bugs;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.ServletControllerRequest;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.misc.DateTime;
import com.jcorporate.expresso.services.dbobj.GroupMembers;

/**
 * @author taks
 * PromptUpdateBugState
 */
public class UpdateBugState extends State {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 3258409521541298482L;

    public static final String STATE_NAME = "updateBugState";

    private static org.apache.log4j.Category standardLog = org.apache.log4j.Category.getInstance("pvn.standard.ebug");

    private static org.apache.log4j.Category debugLog = org.apache.log4j.Category.getInstance("pvn.debug.ebug");

    private static org.apache.log4j.Category observerLog = org.apache.log4j.Category.getInstance("pvn.observer.ebug");

    /**
	 * Constructor
	 */
    public UpdateBugState() {
        super("updateBugState", "UpdateBugStateDescription");
        addRequiredParameter("bugId");
    }

    /**
	 * @see com.jcorporate.expresso.core.controller.State#run(com.jcorporate.expresso.core.controller.ControllerRequest, com.jcorporate.expresso.core.controller.ControllerResponse)
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        try {
            Transition errorTrans = new Transition();
            errorTrans.setControllerObject(BugController.class);
            errorTrans.setState(ErrorState.STATE_NAME);
            errorTrans.setName("errorTrans");
            setErrorTransition(errorTrans);
            GroupMembers gm = new GroupMembers();
            gm.setField(GroupMembers.EXPUID, request.getUid());
            gm.setField(GroupMembers.GROUP_NAME, "Admin");
            if (!gm.find()) throw new Exception("errors.accessDeniedOccured");
            String bugId = request.getParameter("bugId");
            Bugs bugsDB = new Bugs();
            bugsDB.setField("BugId", bugId);
            bugsDB.setField("Component", request.getParameter("locationInput"));
            bugsDB.setField("BugDescription", request.getParameter("descriptionInput"));
            bugsDB.setField("BugURL", request.getParameter("urlInput"));
            bugsDB.setField("StatusReport", request.getParameter("reportInput"));
            bugsDB.setField("OS", request.getParameter("osInput"));
            bugsDB.setField("Priority", request.getParameter("priorityInput"));
            bugsDB.setField("Type", request.getParameter("typeInput"));
            bugsDB.setField("Status", request.getParameter("statusInput"));
            bugsDB.setField("DateUpdated", DateTime.getDateTimeForDB());
            if ((request.getParameter("statusInput") != null) && (request.getParameter("statusInput").equals("3"))) {
                bugsDB.setField("FixedInVer", bugsDB.getReleaseVersion());
                bugsDB.setField("FixedInVerDate", bugsDB.getReleaseVersionDate());
            }
            bugsDB.setField("UpdatedUid", request.getUid());
            bugsDB.update(true);
            Transition goToListBugState = new Transition();
            goToListBugState.setName("GoToReport");
            goToListBugState.setControllerObject(this.getController().getClass());
            goToListBugState.setState("listBugState");
            setSuccessTransition(goToListBugState);
        } catch (Exception e) {
            if (e instanceof DBException) addError("errors.DBException"); else if (e.getMessage().equals("errors.accessDeniedOccured")) addError("errors.accessDeniedOccured"); else addError("errors.Exception");
            if (standardLog.isEnabledFor(Priority.WARN)) standardLog.warn(" :: Exception in \"" + this.getName() + "\" : " + e.toString());
            if (debugLog.isDebugEnabled()) debugLog.debug(" :: Exception in \"" + this.getName() + "\" : " + e.toString(), e.fillInStackTrace());
        } finally {
            if (observerLog.isInfoEnabled()) {
                ServletRequest servletRq = ((ServletControllerRequest) request).getServletRequest();
                observerLog.info(" :: Location= " + this.getClass().getName() + " :: UID= " + request.getUid() + " :: User= " + request.getUser() + " :: IP= " + servletRq.getRemoteAddr());
            }
        }
    }
}
