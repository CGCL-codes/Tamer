package edu.ucsd.ncmir.jinx.objects.events;

import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;
import edu.ucsd.ncmir.jinx.objects.JxObject;

public class JxSetObjectDescriptionEvent extends AsynchronousEvent {

    public JxSetObjectDescriptionEvent(JxObject object) {
        super(object);
    }
}
