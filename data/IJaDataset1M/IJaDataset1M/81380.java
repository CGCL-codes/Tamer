package org.systemsbiology.apps.corragui.client.controller.request;

import org.systemsbiology.apps.corragui.client.constants.RequestType;

/**
 * Request for downloading tab-separated inclusion list
 * 
 * @author Vagisha Sharma
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DownloadInclListRequest extends Request {

    private String projectName;

    private String inclListName;

    /**
	 * Returns <code>RequestType.DOWNLOAD_LIST</code>
	 */
    public RequestType getRequestType() {
        return RequestType.DOWNLOAD_LIST;
    }

    public DownloadInclListRequest() {
    }

    /**
	 * Creates a new request downloading a saved inclusion list
	 * @param projectName -- project containing the saved segment list
	 * @param listName -- name of the inclusion list 
	 */
    public DownloadInclListRequest(String projectName, String listName) {
        this.inclListName = listName;
        this.projectName = projectName;
    }

    /**
	 * @return the projectName
	 */
    public String getProjectName() {
        return projectName;
    }

    /**
	 * @return the inclListName
	 */
    public String getInclListName() {
        return inclListName;
    }
}
