package openjmx.log;

/**
 * This logger logs to a file. <p>
 * It's used by the ModelMBean implementation. <br>
 * Since the constructor takes a parameter, cannot be used as prototype for logging redirection.
 *
 * @see
 * @author <a href="mailto:biorn_steedom@users.sourceforge.net">Simone Bordet</a>
 * @version $Revision: 1.2 $
 */
public class FileLogger extends Logger {

    public FileLogger(String location) {
    }

    protected void log(int priority, Object message, Throwable t) {
    }
}
