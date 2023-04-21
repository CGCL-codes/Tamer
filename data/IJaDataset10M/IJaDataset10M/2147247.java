package org.jfree.report;

/**
 * Creation-Date: 02.12.2006, 14:51:42
 *
 * @author Thomas Morgner
 */
public class ReportConfigurationException extends ReportException {

    /**
   * Creates a StackableRuntimeException with no message and no parent.
   */
    public ReportConfigurationException() {
    }

    /**
   * Creates an exception.
   *
   * @param message the exception message.
   * @param ex      the parent exception.
   */
    public ReportConfigurationException(final String message, final Exception ex) {
        super(message, ex);
    }

    /**
   * Creates an exception.
   *
   * @param message the exception message.
   */
    public ReportConfigurationException(final String message) {
        super(message);
    }
}
