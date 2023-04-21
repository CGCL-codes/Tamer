package edu.ucsd.ncmir.jibber.events;

import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;
import edu.ucsd.ncmir.spl.minixml.Element;

/**
 *
 * @author spl
 */
public class GetURIElementEvent extends AsynchronousEvent {

    /**
     * Creates a new instance of GetURIElementEvent
     */
    public GetURIElementEvent() {
    }

    private Element element;

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
