package com.android.traceview;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TraceReader {

    private TraceUnits mTraceUnits;

    public TraceUnits getTraceUnits() {
        if (mTraceUnits == null) mTraceUnits = new TraceUnits();
        return mTraceUnits;
    }

    public ArrayList<TimeLineView.Record> getThreadTimeRecords() {
        return null;
    }

    public HashMap<Integer, String> getThreadLabels() {
        return null;
    }

    public MethodData[] getMethods() {
        return null;
    }

    public ThreadData[] getThreads() {
        return null;
    }

    public long getEndTime() {
        return 0;
    }

    public ProfileProvider getProfileProvider() {
        return null;
    }
}
