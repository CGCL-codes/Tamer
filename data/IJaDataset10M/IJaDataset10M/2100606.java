package com.awarix.gwt.dom.svg;

import com.awarix.gwt.dom.DOMObject;
import com.google.gwt.core.client.JavaScriptObject;
import org.w3c.dom.svg.SVGAnimatedEnumeration;

public class SVGAnimatedEnumerationImpl extends DOMObject implements SVGAnimatedEnumeration {

    protected SVGAnimatedEnumerationImpl(JavaScriptObject jso) {
        super(jso);
    }

    public native short getAnimVal();

    public native short getBaseVal();

    public native void setBaseVal(short baseVal);

    public static SVGAnimatedEnumeration wrapSVGAnimatedEnumeration(JavaScriptObject jso) {
        return jso == null ? null : new SVGAnimatedEnumerationImpl(jso);
    }
}
