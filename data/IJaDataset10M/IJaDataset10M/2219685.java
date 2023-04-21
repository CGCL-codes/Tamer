package com.aelitis.azureus.core.proxy.socks;

import com.aelitis.azureus.core.proxy.AEProxyException;

/**
 * @author parg
 *
 */
public interface AESocksProxyPlugableConnectionFactory {

    public AESocksProxyPlugableConnection create(AESocksProxyConnection connection) throws AEProxyException;
}
