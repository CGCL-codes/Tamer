package de.bea.domingo.monitor;

/**
 * Abstract base class for implementations of the Monitor interface.
 *
 * <p>This class unifies logging to a single method for easy sub classing.
 * Subclasses only have to implement the two methods
 * <code>monitor(java.lang.String)</code> and
 * <code>monitor(java.lang.Throwable)</code>.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class AbstractDefaultMonitor extends AbstractMonitor {

    /**
     * Default constructor.
     */
    public AbstractDefaultMonitor() {
        super();
    }

    /**
     * Constructor.
     *
     * @param theLevel the level of the new monitor, can be one of
     * {@link de.bea.domingo.monitor.AbstractMonitor#DEBUG DEBUG},
     * {@link de.bea.domingo.monitor.AbstractMonitor#INFO INFO},
     * {@link de.bea.domingo.monitor.AbstractMonitor#WARN WARN},
     * {@link de.bea.domingo.monitor.AbstractMonitor#ERROR ERROR} or
     * {@link de.bea.domingo.monitor.AbstractMonitor#FATAL FATAL}
     */
    public AbstractDefaultMonitor(final int theLevel) {
        super(theLevel);
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DNotesMonitor#debug(java.lang.String)
     */
    public final void debug(final String s) {
        if (isDebugEnabled()) {
            monitor("DEBUG: " + s);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DNotesMonitor#debug(java.lang.String, java.lang.Throwable)
     */
    public final void debug(final String s, final Throwable throwable) {
        if (isDebugEnabled()) {
            monitor("DEBUG: " + s);
            monitor(throwable);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DNotesMonitor#info(java.lang.String)
     */
    public final void info(final String s) {
        if (isInfoEnabled()) {
            monitor("INFO:  " + s);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DNotesMonitor#info(java.lang.String, java.lang.Throwable)
     */
    public final void info(final String s, final Throwable throwable) {
        if (isInfoEnabled()) {
            monitor("INFO:  " + s);
            monitor(throwable);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DNotesMonitor#warn(java.lang.String)
     */
    public final void warn(final String s) {
        if (isWarnEnabled()) {
            monitor("WARN:  " + s);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DNotesMonitor#warn(java.lang.String, java.lang.Throwable)
     */
    public final void warn(final String s, final Throwable throwable) {
        if (isWarnEnabled()) {
            monitor("WARN:  " + s);
            monitor(throwable);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DNotesMonitor#error(java.lang.String)
     */
    public final void error(final String s) {
        if (isErrorEnabled()) {
            monitor("ERROR: " + s);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DNotesMonitor#error(java.lang.String, java.lang.Throwable)
     */
    public final void error(final String s, final Throwable throwable) {
        if (isErrorEnabled()) {
            monitor("ERROR: " + s);
            monitor(throwable);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DNotesMonitor#fatalError(java.lang.String)
     */
    public final void fatalError(final String s) {
        if (isFatalErrorEnabled()) {
            monitor("FATAL: " + s);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DNotesMonitor#fatalError(java.lang.String, java.lang.Throwable)
     */
    public final void fatalError(final String s, final Throwable throwable) {
        if (isFatalErrorEnabled()) {
            monitor("FATAL: " + s);
            monitor(throwable);
        }
    }

    /**
     * Abstract monitoring method, must be implemented by concrete monitors.
     *
     * @param message a message to monitor
     */
    protected abstract void monitor(final String message);

    /**
     * Abstract monitoring method, must be implemented by concrete monitors.
     *
     * @param throwable a throwable to monitor
     */
    protected abstract void monitor(final Throwable throwable);
}
