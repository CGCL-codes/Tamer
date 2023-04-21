package de.swm.commons.mobile.client.widgets.map.handlers;

/**
 * Handler wenn dei Karte gezoomt wurde.
 * @author wiese.daniel
 * <br>copyright (C) 2011, SWM Services GmbH
 *
 */
public interface IMapZoomHandler {

    /**
	 * Hanlder Method.
	 * @param zoom der zoom
	 */
    void onMapZoomed(int zoom);
}
