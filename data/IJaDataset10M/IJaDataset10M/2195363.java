package glaceo.gui.client.rpc.helper;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Exception that is sent to the client indicating a server-side problem.
 *
 * @version $Id$
 * @author jjanke
 */
public class GServerException extends Exception implements IsSerializable {

    /** No-arg constructor for GWT/RPC. */
    protected GServerException() {
    }

    /**
   * Creates a new GException.
   *
   * @param strMsg the error message
   */
    public GServerException(String strMsg) {
        super(strMsg);
    }

    /**
   * Creates a new GException.
   *
   * @param cause the Throwable that caused this exception
   */
    public GServerException(Throwable cause) {
        super(cause);
    }

    /**
   * Creates a new GException.
   *
   * @param strMsg the error message
   * @param cause the Throwable that caused this exception
   */
    public GServerException(String strMsg, Throwable cause) {
        super(strMsg, cause);
    }
}
