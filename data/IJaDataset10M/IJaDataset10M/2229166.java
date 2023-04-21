package org.freehold.jukebox.logger;

/**
 * Log filter.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 1995-2000
 * @version $Id: LogFilter.java,v 1.1 2000-12-15 23:51:28 vtt Exp $
 * @since Jukebox v4 2.0p10
 */
public interface LogFilter {

    /**
     * Find out if we should pass this record down the chain.
     *
     * @param lr Log record to analyze.
     *
     * @return <code>true</code> if the filter configuration allows this
     * record to be passed to the log device further down the chain.
     */
    public boolean isEnabled(LogRecord lr);
}
