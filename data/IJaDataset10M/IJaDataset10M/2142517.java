package org.mc4j.ems.connection;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Apr 12, 2005
 * @version $Revision: 570 $($Author: ghinkl $ / $Date: 2006-04-12 15:14:16 -0400 (Wed, 12 Apr 2006) $)
 */
public class EmsConnectException extends EmsException {

    public EmsConnectException(String message) {
        super(message);
    }

    public EmsConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmsConnectException(Throwable cause) {
        super(cause);
    }
}
