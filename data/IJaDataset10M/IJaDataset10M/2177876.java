package org.qtitools.mathassess.tools.qticasbridge;

/**
 * This Exception is thrown when something unexpected happens in the QTI/CAS bridge
 * code. This almost always indicates a logic failure or bug that needs to be investigated
 * and fixed.
 * <p>
 * Problems caused by bad authoring are raised using {@link BadQTICASCodeException}
 * 
 * @see BadQTICASCodeException
 * @see SpecUnimplementedException
 *
 * @author  David McKain
 * @version $Revision: 2428 $
 */
public class QTICASBridgeException extends RuntimeException {

    private static final long serialVersionUID = -3238153517339012903L;

    public QTICASBridgeException() {
        super();
    }

    public QTICASBridgeException(String message, Throwable cause) {
        super(message, cause);
    }

    public QTICASBridgeException(String message) {
        super(message);
    }

    public QTICASBridgeException(Throwable cause) {
        super(cause);
    }
}
