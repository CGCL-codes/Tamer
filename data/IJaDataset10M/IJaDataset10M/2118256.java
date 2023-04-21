package com.google.gwt.event.dom.client;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link EndedEvent} events.
 * 
 * <p>
 * <span style="color:red">Experimental API: This API is still under development
 * and is subject to change.
 * </span>
 * </p>
 */
public interface EndedHandler extends EventHandler {

    /**
   * Called when EndedEvent is fired.
   *
   * @param event the {@link EndedEvent} that was fired
   */
    void onEnded(EndedEvent event);
}
