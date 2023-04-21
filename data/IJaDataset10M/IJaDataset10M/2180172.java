package ch.jester.commonservices.api.logging;

import org.eclipse.core.runtime.ILog;

/**
 * Ein Logger Interface
 *
 */
public interface ILogger extends ILog {

    /**
 * Eine Info Message
 * @param pMessage
 * @param pThrowable
 */
    public void info(String pMessage, Throwable pThrowable);

    /**
 * Eine Info Message
 * @param pMessage
 */
    public void info(String pMessage);

    /**
 * Eine Debug Message
 * @param pMessage
 */
    public void debug(String pMessage);

    /**
 * Eine Error Message
 * @param pError
 */
    public void error(String pError);

    /**
 * Eine Error Message
 * @param pThrowable
 */
    public void error(Throwable pThrowable);

    /**
 * Eine Error Message
 * @param pMessage
 * @param pThrowable
 */
    public void error(String pMessage, Throwable pThrowable);

    /**
 * Gibt das Log vom Framework zurück
 * @return
 */
    public ILog getLog();
}
