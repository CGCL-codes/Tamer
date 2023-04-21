package org.waveprotocol.box.webclient.client.events;

import com.google.gwt.event.shared.EventHandler;

public abstract class WaveCreationEventHandler implements EventHandler {

    /**
   * Called when something wants to create a new wave.
   */
    public abstract void onCreateRequest(WaveCreationEvent event);
}
