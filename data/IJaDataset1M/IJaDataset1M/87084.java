package org.systemsbiology.apps.gui.client.controller.request;

import org.systemsbiology.apps.gui.client.constants.RequestType;
import org.systemsbiology.apps.gui.domain.ATAQSProject;

/**
 * Request for creating a TraML file on a particular project
 * 
 * @author Mark Christiansen
 * @version 1.0
 * @see org.systemsbiology.apps.gui.domain.ATAQSProject
 */
@SuppressWarnings("serial")
public class CreateTraMLRequest extends Request {

    private ATAQSProject project;

    /**
	 * 
	 */
    public CreateTraMLRequest() {
    }

    /**
	 * Creates a TraML file for an ATAQSProject
	 * @param project project to create TraML for
	 */
    public CreateTraMLRequest(ATAQSProject project) {
        setProject(project);
    }

    /**
	 * Returns <code>RequestType.CREATE_TRAML</code>
	 */
    public RequestType getRequestType() {
        return RequestType.CREATE_TRAML;
    }

    /**
	 * Set project to create TraML for
	 * @param project project to create TraML for
	 */
    public void setProject(ATAQSProject project) {
        this.project = project;
    }

    /**
	 * Get project to create TraML for
	 * @return project 
	 */
    public ATAQSProject getProject() {
        return project;
    }
}
