package eu.vph.predict.vre.base.exception;

/**
 * A generic VRE business exception. These are thrown when the business constraints are not being 
 * met and as such should be caught by the layer above as there existance would imply situations 
 * such as an invalid value being specified or an invalid request has been invoked.
 * <p />
 * Rather than have individual business exceptions all identical except in their enclosing 
 * MessageKeys, instead have a generic exception which has the appropriate message key assigned at 
 * the point of being thrown.
 * <p /> 
 * There are however situations where a method may be required to generate a more specific
 * exception for the calling method to act on. In these situations subclasses of
 * VREBusinessException are created.
 * 
 * @author Geoff Williams
 */
public class VREBusinessException extends Exception implements VREException {

    private static final long serialVersionUID = 5455226130098219641L;

    /** Parameters for placeholders in the message string. */
    private Object[] params = new Object[] {};

    /**
   * Constructor with MessageKey to use and parameters to assign to message placeholders.
   * 
   * @param messageKey MessageKey which reflects this exception's cause.
   * @param params Parameters to assign to placeholders in message string.
   */
    public VREBusinessException(final String messageKey, final Object[] params) {
        super(messageKey);
        setParams(params);
    }

    @Override
    public Object[] getParams() {
        return this.params;
    }

    /**
   * Assign parameters for message placeholders.
   * 
   * @param params The placeholder parameters to set.
   */
    private void setParams(final Object[] params) {
        assert (params != null) : "Null business exception placeholder parameters";
        this.params = params;
    }
}
