package be.fedict.trust.service.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class TrustPointAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 1L;
}
