package cn.vlabs.dlog.cachemanage;

import java.io.IOException;
import cn.vlabs.dlog.actionqueue.Action;
import cn.vlabs.dlog.client.Event;
import cn.vlabs.dlog.client.WorkContext;

public class ReportEvent implements Action {

    public ReportEvent(Event event, WorkContext work) {
        this.event = event;
        this.work = work;
    }

    public void doWork() {
        try {
            work.getEventSaver().save(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventQueue queue = work.getEventQueue();
        if (!queue.isCached()) {
            if (!queue.reachMaxSize()) {
                boolean frameReady = queue.put(event);
                if (frameReady) {
                    SendEvent send = new SendEvent(work);
                    send.sendEvents();
                }
            } else {
                queue.setCached(true);
            }
        }
    }

    private Event event;

    private WorkContext work;
}
