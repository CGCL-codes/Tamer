package com.grt192.spot.event.create;

import com.grt192.spot.sensor.create.GRTCreateBumpSensor;

/**
 *
 * @author ajc
 */
public class BumpEvent {

    private final GRTCreateBumpSensor source;

    private final boolean left;

    private final boolean right;

    public BumpEvent(GRTCreateBumpSensor source, boolean left, boolean right) {
        this.source = source;
        this.left = left;
        this.right = right;
    }

    public GRTCreateBumpSensor source() {
        return source;
    }

    public boolean[] state() {
        boolean[] state = { left, right };
        return state;
    }
}
