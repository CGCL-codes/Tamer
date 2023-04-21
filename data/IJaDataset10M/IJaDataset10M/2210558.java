package net.sf.uitags.tagutil.validation;

/**
 * Thrown to indicate an invalid usage of tag that can only be detected
 * during runtime.
 *
 * @see net.sf.uitags.tagutil.validation.TlvLeakageException
 * @author hgani
 * @version $Id$
 */
public class RuntimeValidationException extends RuntimeException {

    /**
   * Serial Version UID.
   */
    private static final long serialVersionUID = 52L;

    /**
   * See {@link RuntimeException#RuntimeException()}.
   */
    public RuntimeValidationException() {
        super();
    }

    /**
   * See {@link RuntimeException#RuntimeException(java.lang.String)}.
   *
   * @param message the detail message
   */
    public RuntimeValidationException(String message) {
        super(message);
    }

    /**
   * Contructs the exception object specifying the name of the tag that
   * has been misused.
   *
   * See {@link RuntimeException#RuntimeException(java.lang.String)}.
   * @param message the detail message
   * @param tagName the tag name
   */
    public RuntimeValidationException(String message, String tagName) {
        super(tagName + ": " + message);
    }

    /**
   * See
   * {@link RuntimeException#RuntimeException(java.lang.String, java.lang.Throwable)}.
   *
   * @param message the detail message
   * @param cause the cause
   */
    public RuntimeValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
   * See
   * {@link RuntimeException#RuntimeException(java.lang.Throwable)}.
   *
   * @param cause the cause
   */
    public RuntimeValidationException(Throwable cause) {
        super(cause);
    }
}
