package br.com.bit.ideias.reflection.exceptions;

/**
 * 
 * @author Nadilson Oliveira da Silva
 * @date 16/03/2009
 * 
 */
public class ClassNotExistsException extends BaseReflectionDslException {

    private static final long serialVersionUID = 1L;

    public ClassNotExistsException() {
        super();
    }

    public ClassNotExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClassNotExistsException(final String message) {
        super(message);
    }

    public ClassNotExistsException(final Throwable cause) {
        super(cause);
    }
}
