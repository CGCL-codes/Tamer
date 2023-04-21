package com.aelitis.azureus.core.dht.router;

/**
 * @author parg
 *
 */
public interface DHTRouterAdapter {

    public void requestAdd(DHTRouterContact contact);

    public void requestPing(DHTRouterContact contact);

    public void requestLookup(byte[] id, String description);
}
