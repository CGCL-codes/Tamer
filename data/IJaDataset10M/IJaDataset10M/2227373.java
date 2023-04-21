package org.az.macaroni.impl;

import org.az.macaroni.Event;
import org.az.macaroni.EventPipe;
import org.az.macaroni.Filter;

/**
 * this is basic do-nothing filter
 * @author artem
 *
 */
public class BasicFilter implements Filter {

    public Event after(EventPipe pipe, Event event) {
        return event;
    }

    public Event before(EventPipe pipe, Event event) {
        return event;
    }
}
