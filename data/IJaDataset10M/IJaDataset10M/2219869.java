package com.prolix.editor.resourcemanager.exceptions;

/**
 * Main Exception Class for the load and save Exception of the Ressourcemanager
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public abstract class GLMRessourceManagerException extends Exception {

    private int status;

    public GLMRessourceManagerException() {
        status = 0;
    }

    public abstract String getMessage();

    public int getStatus() {
        return status;
    }

    protected void setStatus(int status) {
        this.status = status;
    }
}
