package com.jcorporate.eforum.controller.browseStates;

import javax.servlet.ServletRequest;
import org.apache.log4j.Priority;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.ServletControllerRequest;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.db.DBException;

/**
 * @author gpolancic
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ErrorState extends State {

    public static final String STATE_NAME = "errorState";

    private static org.apache.log4j.Category standardLog = org.apache.log4j.Category.getInstance("pvn.standard.eforum");

    private static org.apache.log4j.Category debugLog = org.apache.log4j.Category.getInstance("pvn.debug.eforum");

    private static org.apache.log4j.Category observerLog = org.apache.log4j.Category.getInstance("pvn.observer.eforum");

    /**
	 * Constructor
	 */
    public ErrorState() {
        super("errorState", "errorStateDesc");
    }

    /**
	 * @see com.jcorporate.expresso.core.controller.State#run(com.jcorporate.expresso.core.controller.ControllerRequest, com.jcorporate.expresso.core.controller.ControllerResponse)
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        try {
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
