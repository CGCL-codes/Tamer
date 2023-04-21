package br.com.sysmap.crux.core.i18n;

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
public class LocaleResolverException extends RuntimeException {

    private static final long serialVersionUID = -2441066524518712373L;

    /**
	 * 
	 */
    public LocaleResolverException() {
    }

    /**
	 * @param message
	 */
    public LocaleResolverException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public LocaleResolverException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public LocaleResolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
