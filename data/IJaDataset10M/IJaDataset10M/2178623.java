package net.grinder.console.common;

import net.grinder.common.GrinderException;

/**
 * Console exception class.
 *
 * @author Philip Aston
 * @version $Revision: 3762 $
 */
public class ConsoleException extends GrinderException {

    /**
   * Constructor.
   *
   * @param message The exception message.
   */
    public ConsoleException(String message) {
        super(message);
    }

    /**
   * Constructor.
   *
   * @param message The exception message.
   * @param e Nested exception.
   */
    public ConsoleException(String message, Throwable e) {
        super(message, e);
    }
}
