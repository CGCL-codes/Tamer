package com.awarix.gwt.dom.svg;

import com.google.gwt.core.client.JavaScriptObject;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGTransformList;
import org.w3c.dom.svg.SVGViewSpec;

public class SVGViewSpecImpl extends AbstractFitToViewBoxElement implements SVGViewSpec {

    protected SVGViewSpecImpl(com.google.gwt.dom.client.Element jso) {
        super(jso);
    }

    public native short getZoomAndPan();

    public native void setZoomAndPan(short zoomAndPan);

    public native SVGAnimatedPreserveAspectRatio getPreserveAspectRatio();

    public native SVGAnimatedRect getViewBox();

    public native String getPreserveAspectRatioString();

    public native SVGTransformList getTransform();

    public native String getTransformString();

    public native String getViewBoxString();

    public native SVGElement getViewTarget();

    public native String getViewTargetString();

    public static SVGViewSpec wrapSVGViewSpec(com.google.gwt.dom.client.Element jso) {
        return jso == null ? null : new SVGViewSpecImpl(jso);
    }
}
