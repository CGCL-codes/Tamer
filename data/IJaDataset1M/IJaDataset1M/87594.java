package net.sf.marineapi.provider.event;

/**
 * Listener interface for GPS time/position/velocity events.
 * 
 * @author Kimmo Tuukkanen
 * @version $Revision: 155 $
 * @see net.sf.marineapi.provider.TPVProvider
 * @see net.sf.marineapi.provider.event.TPVEvent
 */
public interface TPVListener extends ProviderListener<TPVEvent> {

    /**
     * Invoked when fresh time/position/velocity report is available, typically
     * once per second.
     * 
     * @param evt TPVEvent
     */
    void providerUpdate(TPVEvent evt);
}
