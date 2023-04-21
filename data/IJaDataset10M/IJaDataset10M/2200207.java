package com.luzan.app.map.gwt.ui.client;

import com.mapitz.gwt.googleMaps.client.JSObject;

/**
 * BDCCPolylineImpl
 *
 * @author Alexander Bondar
 */
public class BDCCPolylineImpl {

    public static native JSObject create(JSObject points, String color, int weight, double opacity, String tooltip, String dash);

    public static native void setWeight(JSObject self, int weight);

    public static native int getWeight(JSObject self);
}
