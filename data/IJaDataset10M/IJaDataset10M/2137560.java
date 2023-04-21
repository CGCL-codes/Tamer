package org.livetribe.net.slp;

/**
 * @version $Revision: 1.1 $ $Date: 2005/01/25 21:06:29 $
 */
public class ServiceLocationBufferOverflowException extends ServiceLocationException {

    public ServiceLocationBufferOverflowException() {
        super(BUFFER_OVERFLOW);
    }
}
