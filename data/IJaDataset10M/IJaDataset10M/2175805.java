package ch.trackedbean.validator.internal;

import ch.trackedbean.validator.*;

/**
 * Simple implementation of {@link IErrorDescription}.<br> {@link #getStringRepresentation()} uses the interpolator from
 * {@link ValidatorManager#getMessageInterpolator()} to build the return value.
 * 
 * @author M. Hautle
 */
public class ErrorDescription implements IErrorDescription {

    /** The key of the message. */
    private final String messageKey;

    /** The parameters for the message or null. */
    private Object[] params;

    /**
     * Default constructor.
     * 
     * @param messageKey The key of the message
     */
    public ErrorDescription(String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * Default constructor.
     * 
     * @param messageKey The key of the message
     * @param params The parameters for the message
     */
    public ErrorDescription(String messageKey, Object... params) {
        this.messageKey = messageKey;
        this.params = params;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringRepresentation() {
        final IMessageInterpolator interpolator = ValidatorManager.getMessageInterpolator();
        if (params == null) return interpolator.getMessage(messageKey);
        return interpolator.getMessage(messageKey, params);
    }
}
