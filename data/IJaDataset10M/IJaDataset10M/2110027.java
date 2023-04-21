package org.granite.tide.spring;

import org.granite.config.flex.Destination;
import org.granite.messaging.service.ServiceException;
import org.granite.messaging.service.ServiceFactory;
import org.granite.tide.TideServiceInvoker;

/**
 * Base class for Tide service invokers
 * Adapts the Tide invocation model with Granite
 * 
 * @author William DRAI
 */
public class SpringServiceInvoker<T extends ServiceFactory> extends TideServiceInvoker<T> {

    private static final long serialVersionUID = 1L;

    public SpringServiceInvoker(Destination destination, T factory) throws ServiceException {
        super(destination, factory);
    }

    public SpringServiceInvoker(Destination destination, T factory, SpringServiceContext tideContext) throws ServiceException {
        super(destination, factory, tideContext);
    }

    @Override
    public Object initializeObject(Object parent, String[] propertyNames) {
        return getTideContext().lazyInitialize(parent, propertyNames);
    }
}
