package org.matsim.core.events.handler;

import org.matsim.core.events.AdditionalTeleportationDepartureEvent;

/**
 * @author nagel
 *
 */
@Deprecated
public interface AdditionalTeleportationDepartureEventHandler extends EventHandler {

    public void handleEvent(AdditionalTeleportationDepartureEvent eve);
}
