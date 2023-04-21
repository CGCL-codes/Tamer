package com.dukesoftware.utils.xml;

import javax.xml.stream.EventFilter;
import javax.xml.stream.events.XMLEvent;

public interface StAXHandlerAdvanced {

    void process(XMLEvent event);

    EventFilter getEventFilter();
}
