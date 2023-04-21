package org.openremote.modeler.server.lutron.importmodel;

public class LutronImportResult {

    private String errorMessage;

    private Project project;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
