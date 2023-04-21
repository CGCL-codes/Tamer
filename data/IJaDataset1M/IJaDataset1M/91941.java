package vh.error;

/**
 * Top level exception of VirtualHockey. All application specific exceptions 
 * that have to be caught must be derived from this class.
 * 
 * @version $Id: VHException.java 39 2006-10-07 16:29:39Z janjanke $
 * @author jankejan
 */
public class VHException extends Exception {

    /**
   * Creates a new VHException.
   * 
   * @param strMsg the error message
   */
    public VHException(String strMsg) {
        super(strMsg);
    }

    /**
   * Creates a new VHException.
   * 
   * @param cause the Throwable that caused this exception
   */
    public VHException(Throwable cause) {
        super(cause);
    }

    /**
   * Creates a new VHException.
   * 
   * @param strMsg the error message
   * @param cause the Throwable that caused this exception
   */
    public VHException(String strMsg, Throwable cause) {
        super(strMsg, cause);
    }
}
