package si.cit.eprojekti.eportal.controller.staticStates;

import java.util.ArrayList;
import javax.servlet.ServletRequest;
import org.apache.log4j.Priority;
import si.cit.eprojekti.eportal.controller.StaticController;
import si.cit.eprojekti.eportal.dbobj.PortalView;
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
 * @author Gregor Polancic
 *
 * Created Sep 23, 2004 11:56:53 AM
 * 
 * PromptAddQuestionState description:
 * 	Creating new Question
 */
public class PromptDisplayViewState extends State {

    private static org.apache.log4j.Category standardLog = org.apache.log4j.Category.getInstance("pvn.standard.eportal");

    private static org.apache.log4j.Category debugLog = org.apache.log4j.Category.getInstance("pvn.debug.eportal");

    private static org.apache.log4j.Category observerLog = org.apache.log4j.Category.getInstance("pvn.observer.eportal");

    /**
	 * Constructor
	 */
    public PromptDisplayViewState() {
        super("promptDisplayViewState", "PromptDisplayViewStateDescription");
    }

    /**
	 * @see com.jcorporate.expresso.core.controller.State#run(com.jcorporate.expresso.core.controller.ControllerRequest, com.jcorporate.expresso.core.controller.ControllerResponse)
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        try {
            Transition errorTrans = new Transition();
            errorTrans.setControllerObject(StaticController.class);
            errorTrans.setState(ErrorState.STATE_NAME);
            errorTrans.setName("errorTrans");
            setErrorTransition(errorTrans);
            Input viewName = new Input();
            viewName.setName("viewName");
            viewName.setLabel("viewNameLabel");
            viewName.setType(Input.ATTRIBUTE_LISTBOX);
            viewName.setLines(10);
            viewName.setMaxLength(80);
            PortalView portalViewDB = new PortalView();
            ArrayList al = portalViewDB.searchAndRetrieveList(PortalView.VIEW_NAME);
            for (int i = 0; i < al.size(); i++) {
                portalViewDB = (PortalView) al.get(i);
                portalViewDB.setLocale(request.getLocale());
                if (portalViewDB.getField(PortalView.VIEW_DISABLE).equals("0")) viewName.addValidValue(portalViewDB.getField(PortalView.VIEW_NAME), portalViewDB.getField(PortalView.VIEW_NAME));
            }
            response.add(viewName);
            Transition displayViewStateTrans = new Transition();
            displayViewStateTrans.setControllerObject(si.cit.eprojekti.eportal.controller.StaticController.class);
            displayViewStateTrans.setState("displayViewState");
            displayViewStateTrans.setName("displayViewStateTrans");
            displayViewStateTrans.setLabel("displayViewStateTransLabel");
            response.add(displayViewStateTrans);
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
