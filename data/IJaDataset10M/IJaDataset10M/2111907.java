package edu.asu.commons.net;

import edu.asu.commons.event.EventChannel;

/**
 * $Id: DispatcherFactory.java 296 2009-10-13 17:09:51Z alllee $
 * Factory for constructing the appropriate dispatcher.
 * 
 * @author Allen Lee
 * @version $Revision: 296 $
 *
 */
public class DispatcherFactory {

    public static final DispatcherFactory INSTANCE = new DispatcherFactory();

    public static final int DEFAULT_WORKER_POOL_SIZE = 3;

    private DispatcherFactory() {
    }

    public static DispatcherFactory getInstance() {
        return INSTANCE;
    }

    public ClientDispatcher createClientDispatcher(EventChannel channel) {
        return new ClientSocketDispatcher(channel);
    }

    public ServerDispatcher createServerDispatcher(EventChannel channel) {
        return createServerDispatcher(channel, DEFAULT_WORKER_POOL_SIZE);
    }

    public ServerDispatcher createServerDispatcher(EventChannel channel, int workerPoolSize) {
        return new ServerSocketDispatcher(channel, workerPoolSize);
    }
}
