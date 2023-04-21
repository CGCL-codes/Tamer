package org.openprojectservices.opsadmin.manager;

public class IllegalNameException extends ManagerException {

    /** default serial */
    private static final long serialVersionUID = 1L;

    public IllegalNameException() {
        super();
    }

    public IllegalNameException(Throwable throwable) {
        super(throwable);
    }

    public IllegalNameException(String comment) {
        super(comment);
    }
}
