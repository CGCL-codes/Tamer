package it.imolinfo.jbi4ejb.exception;

import it.imolinfo.jbi4ejb.jbi.Messages;
import java.util.MissingResourceException;

/**
 * The default exception used within the component.
 *
 * @author <a href="mailto:rspazzoli@imolinfo.it">Raffaele Spazzoli</a>
 * @author <a href="mailto:acannone@imolinfo.it">Amedeo Cannone</a>
 */
public class Jbi4EjbException extends Exception {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 3762815969835563319L;

    /**
     * The localized description of this <code>Throwable</code>.
     */
    private String localizedMessage;

    /**
     * A constructor.
     *
     * @param    message        The message of the exception.
     */
    public Jbi4EjbException(final String message) {
        this(message, null, null);
    }

    /**
     * A constructor.
     *
     * @param    message        The message of the exception.
     * @param    cause        The cause of the exception.
     */
    public Jbi4EjbException(final String message, final Throwable cause) {
        this(message, null, cause);
    }

    /**
     * A constructor.
     *
     * @param    cause    The cause of the exception.
     */
    public Jbi4EjbException(final Throwable cause) {
        this(cause.toString(), null, cause);
        localizedMessage = getMessage();
    }

    /**
     * A constructor with i18n support.
     *
     * @param   message  The message of the exception.
     * @param   args     The <code>MessageFormat</code> arguments.
     */
    public Jbi4EjbException(final String message, final Object[] args) {
        this(message, args, null);
    }

    /**
     * A constructor with i18n support.
     *
     * @param   message  The message of the exception.
     * @param   args     The <code>MessageFormat</code> arguments.
     * @param   cause    The cause of the exception.
     */
    public Jbi4EjbException(final String message, final Object[] args, final Throwable cause) {
        super(message, cause);
        setupLocalizedMessage(args);
    }

    /**
     * Calculates {@link #localizedMessage} value.
     *
     * @param  args  the optional arguments to define the complete message. It
     *               may be <code>null</code>.
     */
    private void setupLocalizedMessage(final Object[] args) {
        StackTraceElement[] stackTrace = getStackTrace();
        if (stackTrace.length == 0) {
            localizedMessage = getMessage();
        } else {
            try {
                Class clazz = Class.forName(stackTrace[0].getClassName());
                Messages messages = Messages.getMessages(clazz);
                localizedMessage = messages.getString(getMessage(), args);
            } catch (ClassNotFoundException e) {
                localizedMessage = getMessage();
            } catch (MissingResourceException e) {
                localizedMessage = getMessage();
            }
        }
    }

    /** {@inheritDoc} */
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
