package org.jw.web.rdc.business.accounts.exception;

import org.jw.web.rdc.business.exceptions.BusinessServiceException;

/**
 * <p>
 * <b>T�tulo:</b> Nome do Projeto.
 * </p>
 * <p>
 * <b>Descri��o:</b>
 * </p>
 * <p>
 * Descri��o da classe.
 * </p>
 * 
 * @author CPM Braxis / Edwin M. A. Costa
 * @version 1.0
 */
@SuppressWarnings("serial")
public class AccountServiceException extends BusinessServiceException {

    /**
     * Construtor da classe AccountServiceException.
     * @param message
     * @param code
     */
    public AccountServiceException(String message, String code) {
        super(message, code);
    }

    /**
     * Construtor da classe AccountServiceException.
     * @param message
     * @param cause
     * @param code
     */
    public AccountServiceException(String message, Throwable cause, String code) {
        super(message, cause, code);
    }

    /**
     * Construtor da classe AccountServiceException.
     * @param message
     * @param cause
     */
    public AccountServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construtor da classe AccountServiceException.
     * @param message
     */
    public AccountServiceException(String message) {
        super(message);
    }

    /**
     * Construtor da classe AccountServiceException.
     * @param cause
     * @param code
     */
    public AccountServiceException(Throwable cause, String code) {
        super(cause, code);
    }

    /**
     * Construtor da classe AccountServiceException.
     * @param cause
     */
    public AccountServiceException(Throwable cause) {
        super(cause);
    }
}
