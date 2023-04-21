package com.vircon.myajax.web.event;

import com.vircon.myajax.web.Component;

public interface ClientEvent extends Event<Object> {

    EventId getEventId();

    EventClass getEventClass();
}
