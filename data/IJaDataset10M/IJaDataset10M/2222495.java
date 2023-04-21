package org.slasoi.gslam.pac.events;

import org.slasoi.common.eventschema.EventInstance;

public class MonitoringEventMessage implements Message {

    private EventInstance event;

    public MonitoringEventMessage(EventInstance event) {
        this.event = event;
    }

    public EventInstance getEventInstance() {
        return event;
    }
}
