package net.zehrer.vse.common;

public interface IStatus {

    public enum SeverityType {

        OK, INFO, WARNING, ERROR, CANCEL
    }

    /**
   * Returns the relevant low-level exception, or null if none. For example, when an operation fails because of a network communications failure, this might return the java.io.IOException describing the exact nature of that failure.
   * @return the relevant low-level exception, or null if none
   */
    public Throwable getException();

    /**
   * Returns the message describing the outcome. The message is localized to the current locale.
   * @return a localized message
   */
    public String getMessage();

    public SeverityType getSeverity();

    /**
   * Returns whether this status indicates everything is okay (neither info, warning, nor error).
   * @return true if this status has severity OK, and false otherwise
   */
    public boolean isOK();
}
