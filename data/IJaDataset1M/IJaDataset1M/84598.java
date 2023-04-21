package net.sourceforge.javautil.network.ssh;

/**
 * An exception thrown during an SCP transfer.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: SecureSCPException.java 517 2009-08-12 17:37:41Z ponderator $
 */
public class SecureSCPException extends RuntimeException {

    public SecureSCPException() {
    }

    public SecureSCPException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecureSCPException(String message) {
        super(message);
    }

    public SecureSCPException(Throwable cause) {
        super(cause);
    }
}
