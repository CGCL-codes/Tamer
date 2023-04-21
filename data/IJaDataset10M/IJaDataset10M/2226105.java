package net.sourceforge.javautil.network.protocol;

import net.sourceforge.javautil.network.NetworkException;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class ProtocolException extends NetworkException {

    public ProtocolException() {
        super();
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(Throwable cause) {
        super(cause);
    }
}
