package org.databene.commons.ui;

/**
 * Error with localizable message.<br/>
 * <br/>
 * Created at 14.12.2008 13:23:51
 * @since 0.4.7
 * @author Volker Bergmann
 */
public class I18NError extends RuntimeException {

    private static final long serialVersionUID = -7876200178254927951L;

    private Object[] parameters;

    public I18NError() {
        super();
    }

    public I18NError(String code) {
        super(code);
    }

    public I18NError(Throwable cause) {
        super(cause);
    }

    public I18NError(String code, Throwable cause, Object... parameters) {
        super(code, cause);
        this.parameters = parameters;
    }

    public String renderMessage(I18NSupport i18n) {
        String message = getMessage();
        return renderMessage(message, i18n, parameters);
    }

    public static String renderMessage(String message, I18NSupport i18n, Object... parameters) {
        return i18n.format("error." + message, parameters);
    }
}
