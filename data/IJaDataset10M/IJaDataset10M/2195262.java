package org.systemsbiology.apps.gui.client.controller.request;

import org.systemsbiology.apps.gui.client.constants.RequestType;
import org.systemsbiology.apps.gui.domain.ATAQSProject;

/**
 * Request for running transition generator on a project.
 * 
 * @author Mark Christiansen
 * @version 1.0
 * 
 * @see org.systemsbiology.apps.gui.domain.ATAQSProject
 * @see org.systemsbiology.apps.gui.domain.TransitionListGeneratorSetup
 */
@SuppressWarnings("serial")
public class GetTransitionListGeneratorSetupRequest extends Request {

    private ATAQSProject project;

    /**
	 * 
	 */
    public GetTransitionListGeneratorSetupRequest() {
    }

    /**
	 * Creates a new transition generator request.
	 * @param project - project to fetch the transition generator for
	 */
    public GetTransitionListGeneratorSetupRequest(ATAQSProject project) {
        this.setProject(project);
    }

    /**
	 * Returns <code>RequestType.GET_TRAN_GEN</code>
	 */
    public RequestType getRequestType() {
        return RequestType.GET_TRAN_GEN;
    }

    /**
	 * Set project to get transition list generator setup for
	 * @param project project of interest
	 */
    public void setProject(ATAQSProject project) {
        this.project = project;
    }

    /**
	 * @return project project to get transition list generator setup for
	 */
    public ATAQSProject getProject() {
        return project;
    }
}
