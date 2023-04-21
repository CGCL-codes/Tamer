package fulmine.distribution.events;

import fulmine.context.IFrameworkContext;
import fulmine.distribution.connection.IConnectionDiscoverer;
import fulmine.event.IEventManager;
import fulmine.event.system.AbstractSystemEvent;

/**
 * Raised by an {@link IConnectionDiscoverer} when a remote context is no longer
 * available.
 * 
 * @author Ramon Servadei
 */
public class ContextNotAvailableEvent extends AbstractSystemEvent {

    /**
     * The name of the remote {@link IFrameworkContext} that is no longer
     * available.
     */
    final String remoteContextIdentity;

    /**
     * Standard constructor with the name of the remote
     * {@link IFrameworkContext} that is no longer available.
     * 
     * @param context
     *            the context for event operations
     * @param remoteContextIdentity
     *            the name of the remote {@link IFrameworkContext} that is no
     *            longer available
     */
    public ContextNotAvailableEvent(IEventManager context, String remoteContextIdentity) {
        super(context);
        this.remoteContextIdentity = remoteContextIdentity;
    }

    public String getRemoteContextIdentity() {
        return this.remoteContextIdentity;
    }

    @Override
    protected String getAdditionalToString() {
        return "remoteContext=" + getRemoteContextIdentity();
    }
}
