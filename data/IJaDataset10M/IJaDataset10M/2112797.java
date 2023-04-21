package browserlauncher.net.sf.wraplog;

/**
 * A Logger that does not log anywhere. This is a useful a an internal default
 * for libraries before the client applications sets a logger.
 * <p>
 * Updated to WrapLog version 1.1.
 *
 * @author Thomas Aglassinger
 */
public class NoneLogger extends AbstractLogger {

    /**
     * Does nothing. Messages are ignored.
     *
     * @see net.sf.wraplog.AbstractLogger#reallyLog(int, java.lang.String,
     *           java.lang.Throwable)
     */
    protected void reallyLog(int logLevel, String message, Throwable error) {
    }
}
