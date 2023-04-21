package com.googlecode.maps3.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Overlay for Google Maps v3 RectangleOptions.
 */
public class RectangleOptions extends JavaScriptObject {

    /** Required by overlays */
    protected RectangleOptions() {
    }

    /** Creation */
    public static native RectangleOptions newInstance();

    public final native void setFillColor(String value);

    public final native void setFillOpacity(double value);

    public final native void setStrokeColor(String value);

    public final native void setStrokeOpacity(double value);

    public final native void setStrokeWeight(double value);

    public final native void setBounds(LatLngBounds value);
}
