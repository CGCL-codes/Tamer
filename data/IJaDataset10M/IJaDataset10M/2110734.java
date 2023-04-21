package com.grt192.event.component;

import com.grt192.sensor.GRTLineTracker;

/**
 * 
 * @author ajc
 */
public class LineTraceEvent {

    private GRTLineTracker source;

    private boolean mode;

    public LineTraceEvent(GRTLineTracker source, boolean mode) {
        this.source = source;
        this.mode = mode;
    }

    public boolean getMode() {
        return mode;
    }

    public GRTLineTracker getSource() {
        return source;
    }
}
