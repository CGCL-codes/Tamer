package org.jumpmind.symmetric.security.inet;

import java.net.InetAddress;

/**
 * 
 */
public interface IInetAddressAuthorizer {

    boolean isAuthorized(InetAddress sourceAddr);
}
