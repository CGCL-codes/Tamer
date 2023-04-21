package org.waveprotocol.wave.client.wavepanel.event;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseDownEvent;

/**
 * A handler of mousedown events.
 *
 */
public interface WaveMouseDownHandler {

    /**
   * Handles a mousedown event.
   *
   * @param event The event object from the browser.
   * @param context The target element whose kind is handled by this handler
   * @return true if the event is handled and should not continue to bubble,
   *         false otherwise.
   */
    boolean onMouseDown(MouseDownEvent event, Element context);
}
