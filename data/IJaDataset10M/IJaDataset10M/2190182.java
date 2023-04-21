package org.asteriskjava.util;

/**
 * Main interface used for logging throughout Asterisk-Java.<p>
 * Concrete instances of this interface are obtained by calling
 * {@link org.asteriskjava.util.LogFactory#getLog(Class)}.
 * 
 * @author srt
 * @see org.asteriskjava.util.LogFactory
 */
public interface Log {

    void debug(Object obj);

    void info(Object obj);

    void warn(Object obj);

    void warn(Object obj, Throwable exception);

    void error(Object obj);

    void error(Object obj, Throwable exception);
}
