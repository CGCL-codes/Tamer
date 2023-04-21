package net.sf.balm.persistence.hibernate3;

/**
 * @author dz
 */
public class UnsupportedQueryTranslatorFactory extends RuntimeException {

    public UnsupportedQueryTranslatorFactory() {
        super();
    }

    public UnsupportedQueryTranslatorFactory(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedQueryTranslatorFactory(String message) {
        super(message);
    }

    public UnsupportedQueryTranslatorFactory(Throwable cause) {
        super(cause);
    }
}
