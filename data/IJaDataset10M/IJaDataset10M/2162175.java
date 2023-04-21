package org.mortbay.log;

/** Logging Facade
 * A simple logging facade that is intended simply to capture the style 
 * of logging as used by Jetty.
 *
 */
public interface Logger {

    public boolean isDebugEnabled();

    /** Mutator used to turn debug on programatically.
     * Implementations operation in which case an appropriate
     * warning message shall be generated.
     */
    public void setDebugEnabled(boolean enabled);

    public void info(String msg, Object arg0, Object arg1);

    public void debug(String msg, Throwable th);

    public void debug(String msg, Object arg0, Object arg1);

    public void warn(String msg, Object arg0, Object arg1);

    public void warn(String msg, Throwable th);

    public Logger getLogger(String name);
}
