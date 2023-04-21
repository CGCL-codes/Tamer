package br.com.fiapbank.negocio.autenticacao;

/**
 * @author robson.oliveira
 *
 */
public class AuteticacaoException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7726585686093603444L;

    /**
	 * 
	 */
    public AuteticacaoException() {
    }

    /**
	 * @param message
	 */
    public AuteticacaoException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public AuteticacaoException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public AuteticacaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
