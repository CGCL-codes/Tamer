package com.od.jtimeseries.capture.impl;

import com.od.jtimeseries.capture.Capture;
import com.od.jtimeseries.capture.CaptureListener;
import com.od.jtimeseries.capture.CaptureState;
import com.od.jtimeseries.identifiable.IdentifiableBase;
import com.od.jtimeseries.source.ValueSource;
import com.od.jtimeseries.timeseries.IdentifiableTimeSeries;
import com.od.jtimeseries.timeseries.TimeSeries;
import com.od.jtimeseries.util.TimeSeriesExecutorFactory;
import com.od.jtimeseries.util.numeric.Numeric;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 06-Feb-2009
 * Time: 11:24:32
 */
public abstract class AbstractCapture extends IdentifiableBase implements Capture {

    private final List<CaptureListener> captureListeners = Collections.synchronizedList(new ArrayList<CaptureListener>());

    private IdentifiableTimeSeries timeSeries;

    private ValueSource source;

    private CaptureState currentState = CaptureState.STOPPED;

    public AbstractCapture(String id, String description, IdentifiableTimeSeries timeSeries, ValueSource source) {
        super(id, description);
        this.timeSeries = timeSeries;
        this.source = source;
    }

    public void addCaptureListener(CaptureListener l) {
        captureListeners.add(l);
    }

    public void removeCaptureListener(CaptureListener l) {
        captureListeners.remove(l);
    }

    protected void changeStateAndFireEvent(final CaptureState newState) {
        final CaptureState oldState = currentState;
        currentState = newState;
        Executor e = TimeSeriesExecutorFactory.getExecutorForCaptureEvents(this);
        e.execute(new Runnable() {

            public void run() {
                CaptureListener[] snapshot = getListenerSnapshot();
                for (CaptureListener l : snapshot) {
                    l.captureStateChanged(AbstractCapture.this, oldState, newState);
                }
            }
        });
    }

    protected void fireTriggerEvent() {
        Executor e = TimeSeriesExecutorFactory.getExecutorForCaptureEvents(this);
        e.execute(new Runnable() {

            public void run() {
                CaptureListener[] snapshot = getListenerSnapshot();
                for (CaptureListener l : snapshot) {
                    l.captureTriggered(AbstractCapture.this);
                }
            }
        });
    }

    protected void fireCaptureCompleteEvent(final Numeric value, final TimeSeries timeseries) {
        Executor e = TimeSeriesExecutorFactory.getExecutorForCaptureEvents(this);
        e.execute(new Runnable() {

            public void run() {
                CaptureListener[] snapshot = getListenerSnapshot();
                for (CaptureListener l : snapshot) {
                    l.captureComplete(AbstractCapture.this, value, timeseries);
                }
            }
        });
    }

    private CaptureListener[] getListenerSnapshot() {
        CaptureListener[] snapshot;
        synchronized (captureListeners) {
            snapshot = captureListeners.toArray(new CaptureListener[captureListeners.size()]);
        }
        return snapshot;
    }

    public IdentifiableTimeSeries getTimeSeries() {
        return timeSeries;
    }

    public ValueSource getValueSource() {
        return source;
    }

    public CaptureState getState() {
        return currentState;
    }
}
