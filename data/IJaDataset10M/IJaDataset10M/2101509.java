package com.antilia.demo.picviewer.f2006;

import com.antilia.common.osgi.AggregatedActivator;
import com.antilia.demo.picviewer.osgi.PicturesServiceTracker;

public class Activator extends AggregatedActivator {

    public Activator() {
        PicturesServiceTracker tracker = new PicturesServiceTracker();
        tracker.add(new CubaPictures());
        addServiceActivator(tracker);
    }
}
