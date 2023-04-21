package com.google.gwt.event.dom.client;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link DragLeaveEvent} events.
 */
public interface DragLeaveHandler extends EventHandler {

    /**
   * Called when a {@link DragLeaveEvent} is fired.
   * 
   * @param event the {@link DragLeaveEvent} that was fired
   */
    void onDragLeave(DragLeaveEvent event);
}
