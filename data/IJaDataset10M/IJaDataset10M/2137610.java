package org.jumpmind.symmetric.web;

import org.jumpmind.symmetric.transport.ITransportResource;
import org.jumpmind.symmetric.transport.ITransportResourceHandler;

/**
 * This is a filter that is used by a transport.
 * 
 * 
 * @param <T>
 * @since 1.4.0
 * 
 */
public abstract class AbstractTransportFilter<T extends ITransportResourceHandler> extends AbstractFilter implements ITransportResource<T> {

    private T transportResourceHandler;

    public void setTransportResourceHandler(T transportResourceHandler) {
        this.transportResourceHandler = transportResourceHandler;
    }

    public T getTransportResourceHandler() {
        return transportResourceHandler;
    }
}
