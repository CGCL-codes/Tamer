package by.bsuir.cbsbw;

import javax.faces.application.FacesMessage;

/**
 *
 * @author anton
 */
public class I18nFacesMessage extends FacesMessage {

    public I18nFacesMessage(String key) {
        super(getMessage(key));
    }

    public I18nFacesMessage(String summary, String detail) {
        super(getMessage(summary), getMessage(detail));
    }

    public I18nFacesMessage(Severity severity, String summary, String detail) {
        super(severity, getMessage(summary), getMessage(detail));
    }

    private static String getMessage(String key) {
        return FacesUtils.getI18nMessage(key);
    }
}
