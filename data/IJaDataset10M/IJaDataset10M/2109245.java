package com.google.gwt.maps.client.event;

import com.google.gwt.maps.client.overlay.Marker;
import java.util.EventObject;

/**
 * Provides an interface to implement in order to receive MapEvent.MOUSEOVER
 * events from the {@link Marker}.
 */
public interface MarkerMouseOverHandler {

    /**
   * Encapsulates the arguments for the MapEvent.MOUSEOVER event on a
   * {@link Marker}.
   */
    @SuppressWarnings("serial")
    class MarkerMouseOverEvent extends EventObject {

        public MarkerMouseOverEvent(Marker source) {
            super(source);
        }

        /**
     * Returns the instance of the map that generated this event.
     * 
     * @return the instance of the map that generated this event.
     */
        public Marker getSender() {
            return (Marker) getSource();
        }
    }

    /**
   * Method to be invoked when a MapEvent.MOUSEOVER event fires on a
   * {@link Marker}.
   * 
   * @param event contains the properties of the event.
   */
    void onMouseOver(MarkerMouseOverEvent event);
}
