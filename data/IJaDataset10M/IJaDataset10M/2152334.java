package fr.macymed.commons.logging;

/** 
 * <p>
 * A Logger object is used to log messages for a specific system or application component. Loggers are normally named, using a hierarchical dot-separated namespace. Logger names can be arbitrary strings, but they should normally be based on the package name or class name of the logged component, such as java.net or javax.swing.
 * </p> 
 * <p>
 * Logger objects may be obtained by calls on one of the {@link fr.macymed.commons.logging.LoggerFactory#getLogger(String) getLogger} {@link fr.macymed.commons.logging.LoggerFactory factory} methods.  These will either create a new Logger or return a suitable existing Logger.
 * </p>
 * @author <a href="mailto:alexandre.cartapanis@macymed.fr">Cartapanis Alexandre</a>
 * @version 1.1.0
 * @since Commons - Logging API 2.0
 */
public interface Logger {

    /**
     * <p>
     * Gets the name for this logger.
     * </p>
     * @return <code>String</code> - The logger name. Will be null for anonymous Loggers.
     */
    public String getName();

    /**
     * <p>
     * Logs a DEBUG message, with no arguments. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     */
    public void debug(Object _message);

    /**
     * <p>
     * Logs a DEBUG message, with associated Throwable information. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     * @param _thrown A previously catched exception.
     */
    public void debug(Object _message, Throwable _thrown);

    /**
     * <p>
     * Logs a CONFIG message, with no arguments. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     */
    public void config(Object _message);

    /**
     * <p>
     * Logs a CONFIG message, with associated Throwable information. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     * @param _thrown A previously catched exception.
     */
    public void config(Object _message, Throwable _thrown);

    /**
     * <p>
     * Logs an INFO message, with no arguments. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     */
    public void info(Object _message);

    /**
     * <p>
     * Logs an INFO message, with associated Throwable information. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     * @param _thrown A previously catched exception.
     */
    public void info(Object _message, Throwable _thrown);

    /**
     * <p>
     * Logs a WARNING message, with no arguments. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     */
    public void warning(Object _message);

    /**
     * <p>
     * Logs a WARNING message, with associated Throwable information. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     * @param _thrown A previously catched exception.
     */
    public void warning(Object _message, Throwable _thrown);

    /**
     * <p>
     * Logs an ERROR message, with no arguments. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     */
    public void error(Object _message);

    /**
     * <p>
     * Logs an ERROR message, with associated Throwable information. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     * @param _thrown A previously catched exception.
     */
    public void error(Object _message, Throwable _thrown);

    /**
     * <p>
     * Logs a FATAL message, with no arguments. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     */
    public void fatal(Object _message);

    /**
     * <p>
     * Logs a FATAL message, with associated Throwable information. If the logger is currently enabled for the given message level then the given message is forwarded to all the registered output Handler objects.
     * </p>
     * @param _message The message.
     * @param _thrown A previously catched exception.
     */
    public void fatal(Object _message, Throwable _thrown);
}
