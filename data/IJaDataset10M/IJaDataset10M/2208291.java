package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.expectations.Report;
import com.volantis.testtools.mock.EventEffect;
import java.io.PrintWriter;

public class NullReport implements Report {

    public static final Report INSTANCE = new NullReport();

    private Object marker;

    public Report append(int i) {
        return this;
    }

    public Report append(String s) {
        return this;
    }

    public Report append(Object o) {
        return this;
    }

    public PrintWriter getPrintWriter() {
        return NullPrintWriter.INSTANCE;
    }

    public void addStackTrace() {
    }

    public Object getMarker() {
        return marker;
    }

    public void setMarker(Object marker) {
        this.marker = marker;
    }

    public void beginBlock() {
    }

    public void endBlock() {
    }

    public void startExpectation() {
    }

    public void handleEventEffect(EventEffect effect) {
    }
}
