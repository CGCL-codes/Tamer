package org.systemsbiology.apps.corragui.client.controller.request;

import java.util.List;
import org.systemsbiology.apps.corragui.client.constants.RequestType;

/**
 * Request for Boardcast Target Feature Annotation result on a project to Pipe2.
 * 
 * @author skwok
 * @version 1.0
 */
@SuppressWarnings("serial")
public class IPIRequest extends Request {

    private String projectName;

    private String fileName;

    private List<String> ipiColumn;

    public IPIRequest() {
    }

    /**
	 * Creates a new IPIRequest request.
	 * @param projName - name of the project on which to get TargetFeatureAnnotation IPI
	 * @gwt.typeArgs List<String>
	 */
    public IPIRequest(String projName, String fileName) {
        this.projectName = projName;
        this.fileName = fileName;
    }

    /**
	 * Returns <code>RequestType.IPI_GET</code>
	 */
    public RequestType getRequestType() {
        return RequestType.IPI_GET;
    }

    /**
	 * @return the projName
	 */
    public String getProjectName() {
        return projectName;
    }

    public String getFileName() {
        return fileName;
    }

    public List<String> getIpiColumn() {
        return this.ipiColumn;
    }

    public void setIpiColumn(List<String> ipiColumn) {
        this.ipiColumn = ipiColumn;
    }
}
