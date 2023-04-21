package net.stickycode.examples.ws.helloworld.v1;

import net.stickycode.stereotype.Scheduled;
import net.stickycode.stereotype.StickyComponent;

@StickyComponent
public class ScheduledCountingWorld implements CountingWorld {

    private int counter = 10;

    @Override
    public Integer count() {
        return counter;
    }

    @Scheduled
    public void increment() {
        counter++;
    }
}
