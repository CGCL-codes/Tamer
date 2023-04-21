package com.volantis.map.agent;

import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Indicate that a exception occured when attempting to use the Media Agent
 */
public class MediaAgentException extends Exception {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = LocalizationFactory.createExceptionLocalizer(MediaAgentException.class);

    /**
     * Accepts a localization key a substitution parameter (or an array of
     * substitution parameters, and a cause exception
     *
     * @param key the message localization key
     * @param param the substitution parameter or array of substitution params
     * @param cause the cause excception
     */
    public MediaAgentException(String key, Object param, Throwable cause) {
        super(EXCEPTION_LOCALIZER.format(key, param), cause);
    }

    /**
     * Accepts a localization key.
     *
     * @param key   the message localization key
     */
    public MediaAgentException(String key) {
        this(key, null, null);
    }

    /**
     * Accepts a localization key a substitution parameter (or an array of
     * substitution parameters.
     *
     * @param key   the message localization key
     * @param param the substitution parameter or array of substitution params
     */
    public MediaAgentException(String key, Object param) {
        this(key, param, null);
    }
}
