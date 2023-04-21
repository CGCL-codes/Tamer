package org.ozi.core.log;

/**
 * <p>Provides an abstraction of the two log provider's methods. (JDKProvider/Log4JProvider)</p>
 *
 * @author Khairul Nadzry Azman
 *
 */
public interface LogProvider {

    public boolean isTraceEnabled();

    public boolean isDebugEnabled();

    public boolean isInfoEnabled();

    public boolean isWarningEnabled();

    public boolean isErrorEnabled();

    public boolean isFatalEnabled();

    public void trace(Object object);

    public void trace(Object object, Throwable t);

    public void debug(Object object);

    public void debug(Object object, Throwable t);

    public void info(Object object);

    public void info(Object object, Throwable t);

    public void warn(Object object);

    public void warn(Object object, Throwable t);

    public void error(Object object);

    public void error(Object object, Throwable t);

    public void fatal(Object object);

    public void fatal(Object object, Throwable t);
}
