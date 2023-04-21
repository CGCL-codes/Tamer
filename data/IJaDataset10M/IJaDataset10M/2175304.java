package com.pricefeeder.bandsintown;

import java.util.List;
import com.pricefeeder.bandsintown.model.Event;

public class BandsInTownResponse {

    /**
	 * all concert events returned from your service call
	 */
    private List<Event> events;

    public BandsInTownResponse(List<Event> events) {
        this.events = events;
    }

    /**
	 * If your service call resulted in a successful query, then
	 * use this to retreive the actual concert events returned from BandsInTown.
	 * 
	 * @return null or a list of concert events
	 */
    public List<Event> getEvents() {
        return events;
    }
}
