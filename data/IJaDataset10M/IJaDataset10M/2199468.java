package org.livetribe.net.slp;

/**
 * @version $Revision: 1.1 $ $Date: 2005/01/25 21:06:30 $
 */
public class ServiceLocationNetworkException extends ServiceLocationException {

    public ServiceLocationNetworkException() {
        super(NETWORK_ERROR);
    }
}
