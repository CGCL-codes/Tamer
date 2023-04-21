package net.sourceforge.webflowtemplate.db.dao.exception;

import org.springframework.dao.DataAccessResourceFailureException;

public class DangerousUsernameException extends DataAccessResourceFailureException {

    private static final long serialVersionUID = 1;

    public DangerousUsernameException(String pMessage) {
        super(pMessage);
    }

    public DangerousUsernameException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }
}
