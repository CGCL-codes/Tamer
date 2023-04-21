package com.pavco.caribbeanvisit.client.eventhandlers;

import com.google.gwt.event.shared.EventHandler;
import com.pavco.caribbeanvisit.client.event.CountryMarkerClickEvent;

public interface CountryMarkerClickEventHandler extends EventHandler {

    void onMarkerClicked(CountryMarkerClickEvent event);
}
