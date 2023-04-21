package si.cit.eprojekti.eprocess.controller.managestates;

import java.util.Hashtable;
import javax.servlet.ServletRequest;
import org.apache.log4j.Priority;
import si.cit.eprojekti.eprocess.ProcessSchema;
import si.cit.eprojekti.eprocess.controller.ManageController;
import si.cit.eprojekti.eprocess.controller.browsestate.ErrorState;
import si.cit.eprojekti.eprocess.util.ComponentSecurityManager;
import si.cit.eprojekti.projectvianet.dbobj.Member;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.Input;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.ServletControllerRequest;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.core.db.DBException;

/**
 * @author Luka Pavli� (luka.pavlic@cit.si)
 *
 * PromptAddWorkFlowState description:
 *	Prompt user to insert workflow
 */
public class PromptAddWorkFlowState extends State {

    private static final long serialVersionUID = 23134546488887987L;

    /**
	 * Constructor
	 */
    public PromptAddWorkFlowState() {
        super("PromptAddWorkFlowState", "PromptAddWorkFlowStateDescription");
        addRequiredParameter("categoryId");
        addRequiredParameter("projectId");
    }

    /**
	 * @see com.jcorporate.expresso.core.controller.State#run(com.jcorporate.expresso.core.controller.ControllerRequest, com.jcorporate.expresso.core.controller.ControllerResponse)
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        Transition errorTrans = new Transition();
        errorTrans.setControllerObject(ProcessSchema.class);
        errorTrans.setState(ErrorState.STATE_NAME);
        errorTrans.setName("errorTrans");
        setErrorTransition(errorTrans);
        try {
            if (ComponentSecurityManager.getAccessOnCategory(Integer.parseInt(request.getParameter("categoryId")), request.getUid()) < ComponentSecurityManager.ACCESS_LEADER) throw ComponentSecurityManager.accessDeniedOccured(response);
            Input inName = new Input("inName", "inName*");
            response.add(inName);
            Input inDescription = new Input("inDescription", "inDescription");
            inDescription.setType("textarea");
            inDescription.setLines(5);
            response.add(inDescription);
            Input inOwner = new Input("inOwner", "inOwner*");
            inOwner.setValidValues(Member.getProjectMembersAndLEadersValidValues(Integer.parseInt(request.getParameter("projectId"))));
            response.add(inOwner);
            Transition createWFTransition = new Transition("createWFTransition", "createWFTransition", ManageController.class, "ProcessAddWorkFlowState");
            Hashtable params = new Hashtable();
            params.put("categoryId", request.getParameter("categoryId"));
            params.put("projectId", request.getParameter("projectId"));
            createWFTransition.setParams(params);
            response.add(createWFTransition);
        } catch (Exception e) {
            if (e instanceof DBException) addError("errors.DBException"); else if (e.getMessage().equals("errors.accessDeniedOccured")) addError("errors.accessDeniedOccured"); else addError("errors.Exception");
            if (ProcessSchema.standardLog.isEnabledFor(Priority.WARN)) ProcessSchema.standardLog.warn(" :: Exception in \"" + this.getName() + "\" : " + e.toString());
            if (ProcessSchema.debugLog.isDebugEnabled()) ProcessSchema.debugLog.debug(" :: Exception in \"" + this.getName() + "\" : " + e.toString(), e.fillInStackTrace());
        } finally {
            if (ProcessSchema.observerLog.isInfoEnabled()) {
                ServletRequest servletRq = ((ServletControllerRequest) request).getServletRequest();
                ProcessSchema.observerLog.info(" :: Location= " + this.getClass().getName() + " :: UID= " + request.getUid() + " :: User= " + request.getUser() + " :: IP= " + servletRq.getRemoteAddr());
            }
        }
    }
}
