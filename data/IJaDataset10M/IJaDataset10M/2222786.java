package com.google.gwt.event.dom.client;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link GestureEndEvent} events.
 */
public interface GestureEndHandler extends EventHandler {

    /**
   * Called when GestureEndEvent is fired.
   *
   * @param event the {@link GestureEndEvent} that was fired
   */
    void onGestureEnd(GestureEndEvent event);
}
