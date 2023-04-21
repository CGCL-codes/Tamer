package cn.vlabs.dlog.cachemanage;

import cn.vlabs.dlog.actionqueue.Action;
import cn.vlabs.dlog.client.WorkContext;

public class TimeOut extends SendEvent implements Action {

    public TimeOut(WorkContext work) {
        super(work);
    }

    public void doWork() {
        super.sendEvents();
    }
}
