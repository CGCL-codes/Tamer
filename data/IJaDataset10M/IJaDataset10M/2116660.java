package com.unboundid.util.args;

import com.unboundid.util.LDAPSDKException;
import com.unboundid.util.NotMutable;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;

/**
 * This class defines an exception that may be thrown if a problem occurs while
 * parsing command line arguments or preparing the argument parser.
 */
@NotMutable()
@ThreadSafety(level = ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class ArgumentException extends LDAPSDKException {

    /**
   * The serial version UID for this serializable class.
   */
    private static final long serialVersionUID = 8353938257797371099L;

    /**
   * Creates a new argument exception with the provided message.
   *
   * @param  message  The message to use for this exception.
   */
    public ArgumentException(final String message) {
        super(message);
    }

    /**
   * Creates a new argument exception with the provided message and cause.
   *
   * @param  message  The message to use for this exception.
   * @param  cause    The underlying exception that triggered this exception.
   */
    public ArgumentException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
