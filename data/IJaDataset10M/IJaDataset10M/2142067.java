package br.gov.frameworkdemoiselle.monitoring.exception;

/**
 * Customized exception class for <b>MBeans</b>.
 * 
 * @author SERPRO
 */
public class MBeanException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MBeanException() {
        super();
    }

    public MBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public MBeanException(String message) {
        super(message);
    }

    public MBeanException(Throwable cause) {
        super(cause);
    }
}
