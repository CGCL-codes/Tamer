package com.google.gwt.maps.client.event;

import com.google.gwt.maps.client.MapWidget;
import java.util.EventObject;

/**
 * Provides an interface to implement in order to receive
 * MapEvent.INFOWINDOWBEFORECLOSE events from the {@link MapWidget}.
 */
public interface MapInfoWindowBeforeCloseHandler {

    /**
   * Encapsulates the arguments for the MapEvent.INFOWINDOWBEFORECLOSE event on
   * a {@link MapWidget}.
   */
    @SuppressWarnings("serial")
    class MapInfoWindowBeforeCloseEvent extends EventObject {

        public MapInfoWindowBeforeCloseEvent(MapWidget source) {
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
   * Method to be invoked when a MapEvent.INFOWINDOWBEFORECLOSE event fires on a
   * {@link MapWidget}.
   * 
   * @param event contains the properties of the event.
   */
    void onInfoWindowBeforeClose(MapInfoWindowBeforeCloseEvent event);
}
