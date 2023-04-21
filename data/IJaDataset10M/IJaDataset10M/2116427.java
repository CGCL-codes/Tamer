package org.ceno.communication.cli.dispatch;

import org.ceno.communication.cli.CommunicationException;
import org.ceno.communication.cli.IConnectorService;
import org.ceno.communication.cli.IEventCommunicator;
import org.ceno.communication.cli.IServerUnavailableObserver;
import org.ceno.communication.cli.ServerUnavailableException;
import org.ceno.communication.cli.internal.Activator;
import org.ceno.protocol.event.IEvent;
import org.ceno.protocol.event.ReturnableEvent;
import org.eclipse.core.runtime.Status;

/**
 * Implementation that is visible to client. Simply delegates to internal
 * communicators that will look for a certain implementation registered at the
 * <code>org.ceno.communication.communicators</code> extension point.
 * 
 * @author Andre Albert &lt;andre.albert82@googlemail.com&gt;
 * @created 15.07.2009
 * @version 0.0.1
 * @since 0.0.1
 * 
 */
public class EventCommunicatorService implements IEventCommunicator {

    private final IConnectorService connService;

    private final IEventCommunicator dispatcher;

    private IServerUnavailableObserver serverUnavailableNotifier;

    /**
	 * 
	 */
    public EventCommunicatorService(final IConnectorService connService) {
        dispatcher = new ExtensionPointEventCommunicatorDispatcher();
        this.connService = connService;
    }

    /**
	 * 
	 * @since 0.0.5
	 */
    public EventCommunicatorService(final IConnectorService connService, final IServerUnavailableObserver serverUnavailableDispatcher) {
        this(connService);
        this.serverUnavailableNotifier = serverUnavailableDispatcher;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @throws CommunicationException
	 */
    public void communicate(final IEvent event) throws CommunicationException {
        if (!connService.isConnected()) {
            throw new CommunicationException("Disconnected");
        }
        try {
            dispatcher.communicate(event);
        } catch (final ServerUnavailableException e) {
            serverUnavailableNotifier.serverUnavailable();
            Activator.log(Status.ERROR, "Server Unavailable", e);
            throw e;
        }
    }

    public <T> T query(final ReturnableEvent<T> event) throws CommunicationException {
        if (!connService.isConnected()) {
            throw new CommunicationException("Disconnected");
        }
        T queryResult = null;
        try {
            queryResult = dispatcher.query(event);
        } catch (final ServerUnavailableException e) {
            serverUnavailableNotifier.serverUnavailable();
            Activator.log(Status.ERROR, "Server Unavailable", e);
            throw e;
        }
        return queryResult;
    }
}
