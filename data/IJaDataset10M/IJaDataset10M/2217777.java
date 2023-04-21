package com.visitrend.ndvis.event.api;

import com.visitrend.ndvis.thread.MonitorableINF;
import java.util.EventObject;

/**
 * 
 * @author John T. Langton - jlangton at visitrend dot com
 * 
 */
public class MonitorableEvent extends EventObject {

    public MonitorableEvent(MonitorableINF m) {
        super(m);
    }
}
