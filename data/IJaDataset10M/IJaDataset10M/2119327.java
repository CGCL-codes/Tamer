package com.google.gwt.maps.client.event;

import com.google.gwt.maps.client.MapWidget;
import java.util.EventObject;

/**
 * Provides an interface to implement in order to receive MapEvent.DRAG events
 * from the {@link MapWidget}.
 */
public interface MapDragHandler {

    /**
   * Encapsulates the arguments for the MapEvent.DRAG event on a
   * {@link MapWidget}.
   */
    @SuppressWarnings("serial")
    class MapDragEvent extends EventObject {

        public MapDragEvent(MapWidget source) {
            super(source);
        }

        /**
     * Returns the instance of the map that generated this event.
     * 
     * @return the instance of the map that generated this event.
     */
        public MapWidget getSender() {
            return (MapWidget) getSource();
        }
    }

    /**
   * Method to be invoked when a MapEvent.DRAG event fires on a
   * {@link MapWidget}.
   * 
   * @param event contains the properties of the event.
   */
    void onDrag(MapDragEvent event);
}
