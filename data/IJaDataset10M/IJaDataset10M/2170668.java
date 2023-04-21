package com.pcmsolutions.device.EMU.E4.events.sample;

import com.pcmsolutions.device.EMU.E4.sample.SampleListener;
import com.pcmsolutions.device.EMU.E4.events.sample.SampleEvent;

public class SampleRefreshEvent extends SampleEvent {

    public SampleRefreshEvent(Object source, Integer sample) {
        super(source, sample);
    }

    public void fire(SampleListener sl) {
        if (sl != null) sl.sampleRefreshed(this);
    }
}
