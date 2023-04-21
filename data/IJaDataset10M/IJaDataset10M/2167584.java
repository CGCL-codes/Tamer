package com.genia.toolbox.basics.exception.technical;

/**
 * This exception represent technical problems during treatment of mail.
 */
@SuppressWarnings("serial")
public class TechnicalMailException extends TechnicalException {

    /**
   * default key used in the internationalization properties file.
   */
    public static final String DEFAULT_KEY = "com.genia.toolbox.basics.exception.technical.TechnicalMailException.DefaultMessage";

    /**
   * Constructor from message, the key of the message and the arguments to
   * constructs the message.
   * 
   * @param message
   *          exception message
   * @param key
   *          the key used in the internationalization properties file
   * @param arguments
   *          exception arguments to send
   */
    public TechnicalMailException(final String message, final String key, final Object... arguments) {
        super(message, key, arguments);
    }

    /**
   * constructor from an existing <code>Throwable</code>.
   * 
   * @param cause
   *          the underlying throwable cause
   */
    public TechnicalMailException(final Throwable cause) {
        super(cause, DEFAULT_KEY);
    }
}
