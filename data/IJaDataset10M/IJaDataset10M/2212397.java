package org.freehold.jukebox.logger;

import org.freehold.jukebox.conf.Configurable;

/**
 * The log target, the final destination of a log message.
 *
 * <p>
 *
 * All the messages submitted to the {@link Logger Logger} will be
 * eventually delivered here.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 1995-1998
 * @version $Id: LogTarget.java,v 1.3 2001-01-17 06:39:57 vtt Exp $
 */
public interface LogTarget extends Configurable {

    /**
     * Log the message.
     *
     * @param message Message to log.
     *
     * @exception IllegalStateException if the log target is not active (in
     * other words, before it is {@link #open open()}ed or after it is
     * {@link #close close()}d). The implementation <strong>must</strong>
     * ensure this.
     */
    public void logMessage(LogRecord message);

    /**
     * Make the log target active.
     */
    public void open();

    /**
     * Flush the content of the buffer, if any, to the final destination.
     */
    public void flush();

    /**
     * Inactivate the log target.
     */
    public void close();
}
