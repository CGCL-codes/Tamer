package hu.schmidtsoft.map.gps.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import hu.schmidtsoft.map.gps.IGPS;
import hu.schmidtsoft.map.gps.IGPSEventListener;
import hu.schmidtsoft.map.model.MCoordinate;

/**
 * Base implementation for all gps sources.
 * @author rizsi
 *
 */
public abstract class AbstractGPS implements IGPS {

    public void start() {
        myStart();
    }

    protected void position(MCoordinate position, long time, int accuracy) {
        List<IGPSEventListener> l;
        synchronized (listeners) {
            l = new ArrayList<IGPSEventListener>(listeners);
        }
        for (IGPSEventListener li : l) {
            li.position(position, time, accuracy);
        }
    }

    protected void signalLost() {
        List<IGPSEventListener> l;
        synchronized (listeners) {
            l = new ArrayList<IGPSEventListener>(listeners);
        }
        for (IGPSEventListener li : l) {
            li.signalLost();
        }
    }

    /**
	 * This method must be overridden by implementations.
	 */
    abstract void myStart();

    List<IGPSEventListener> listeners = Collections.synchronizedList(new ArrayList<IGPSEventListener>());

    public void addListener(IGPSEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IGPSEventListener listener) {
        listeners.remove(listener);
    }
}
