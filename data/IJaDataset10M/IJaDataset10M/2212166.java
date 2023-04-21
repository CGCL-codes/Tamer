package lokahi.agent.util;

/**
 * @author Stephen Toback
 * @version $Id: TMCTaskException.java,v 1.1 2006/03/02 19:19:44 drtobes Exp $
 */
public class TMCTaskException extends Exception {

    /**
   *
   */
    public TMCTaskException() {
        super();
    }

    /**
   * @param message
   */
    public TMCTaskException(String message) {
        super(message);
    }

    /**
   * @param cause
   */
    public TMCTaskException(Throwable cause) {
        super(cause);
    }

    /**
   * @param message
   * @param cause
   */
    public TMCTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
