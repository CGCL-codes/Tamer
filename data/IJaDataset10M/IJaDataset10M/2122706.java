package com.visitrend.ndvis.gui;

/**
 * Should be in the lookup of a component that can be zoomed.
 */
public interface Zoomable {

    enum Mode {

        IN, OUT, PAN
    }

    void setMode(Mode mode);
}
