package pt.ips.estsetubal.mig.academicCloud.client.helper.log;

/**
 * This interface has the methods that the logger class must implement.
 * 
 * @author António Casqueiro
 */
public interface ILogger {

    void trace(String message);

    void trace(String message, Throwable t);

    void debug(String message);

    void debug(String message, Throwable t);

    void info(String message);

    void info(String message, Throwable t);

    void warn(String message);

    void warn(String message, Throwable t);

    void error(String message);

    void error(String message, Throwable t);

    void fatal(String message);

    void fatal(String message, Throwable t);
}
