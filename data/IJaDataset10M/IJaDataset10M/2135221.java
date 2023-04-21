package com.ivis.xprocess.framework.vcs.exceptions;

public class VCSPathAlreadyExistsInRepositoryException extends VCSException {

    private static final long serialVersionUID = 3618585413067433236L;

    public VCSPathAlreadyExistsInRepositoryException() {
    }

    public VCSPathAlreadyExistsInRepositoryException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public VCSPathAlreadyExistsInRepositoryException(String arg0) {
        super(arg0);
    }

    public VCSPathAlreadyExistsInRepositoryException(Throwable arg0) {
        super(arg0);
    }
}
