package CH.ifa.draw.framework;

/**
 * A JHotDraw RuntimeException.
 *
 * @version <$CURRENT_VERSION$>
 */
public class JHotDrawRuntimeException extends RuntimeException {

    public JHotDrawRuntimeException(String msg) {
        super(msg);
    }

    public JHotDrawRuntimeException(Exception nestedException) {
        this(nestedException.getLocalizedMessage());
        nestedException.fillInStackTrace();
    }
}
