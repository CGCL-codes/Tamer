package simpleorm.simpleweb.core;

import simpleorm.simpleweb.core.WField;

/**
 * An error messageCode which will be reported to the user.
 * Could be multiple for a given form, one per field in error.
 */
public class WValidationException extends RuntimeException {

    /** Message that will be i18ned, typicially contains {0} etc. */
    String messageCode;

    /** Field upon which the error occured, can be null. */
    WField field;

    /** Arbitrary extra parameter if needed. */
    String parameter;

    public String getFieldName() {
        return field == null ? null : field.getName();
    }

    /** Throw these to indicate user errors.
     * Leave field null if interfield.
     * Use setParameter if extra infor required.
     */
    public WValidationException(String messageCode, WField field) {
        super(messageCode);
        this.messageCode = messageCode;
        this.field = field;
    }

    @Override
    public String getLocalizedMessage() {
        String msg = messageCode;
        if (getField() != null) msg += " Field: " + getField().getName();
        if (getParameter() != null) msg += " (" + getParameter() + ")";
        return msg;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public WField getField() {
        return field;
    }

    public void setField(WField field) {
        this.field = field;
    }

    public String getParameter() {
        return parameter;
    }

    public WValidationException setParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }
}
