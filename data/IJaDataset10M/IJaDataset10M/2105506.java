package com.google.gwt.event.dom.client;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link KeyDownEvent} events.
 */
public interface KeyDownHandler extends EventHandler {

    /**
   * Called when {@link KeyDownEvent} is fired.
   * 
   * @param event the {@link KeyDownEvent} that was fired
   */
    void onKeyDown(KeyDownEvent event);
}
