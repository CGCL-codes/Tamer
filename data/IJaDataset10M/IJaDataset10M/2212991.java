package org.openprojectservices.opsadmin.manager;

public class DuplicateProjectUidException extends ManagerException {

    /** default serial */
    private static final long serialVersionUID = 1L;

    DuplicateProjectUidException() {
        super();
    }

    DuplicateProjectUidException(Throwable throwable) {
        super(throwable);
    }

    DuplicateProjectUidException(String comment) {
        super(comment);
    }
}
