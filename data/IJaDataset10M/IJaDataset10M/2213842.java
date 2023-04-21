package junit.log4j;

/**
 * Listens to a {@link org.apache.log4j.Appender Appender} of
 * <a href="http://jakarta.apache.org/log4j">Log4J</a>.
 * <p>
 *
 * Created: Mon Nov 12 10:34:13 2001
 *
 * @author <a href="mailto:wolfgang@openfuture.de">Wolfgang Reissenberger</a>
 * @version $Revision: 1.2 $
 */
public interface AppenderListener {

    /**
     * An {@link org.apache.log4j.Appender Appender} will invoke this method
     * to send a new logging message to all its listeners.
     *
     * @param message the message to be sent
     */
    public void addMessage(String message);
}
