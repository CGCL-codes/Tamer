package org.mobicents.servlet.sip.ctf.examples.modules;

import java.util.concurrent.LinkedBlockingQueue;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class EventsModule {

    @Produces
    LinkedBlockingQueue<String> eventsQueue = new LinkedBlockingQueue<String>();

    public void notifyStatus(@Observes String event) {
        try {
            eventsQueue.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
