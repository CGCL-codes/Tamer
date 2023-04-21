package com.awarix.gwt.dom.svg;

import org.w3c.dom.svg.SVGAnimatedLengthList;
import org.w3c.dom.svg.SVGLengthList;
import com.awarix.gwt.dom.DOMObject;
import com.google.gwt.core.client.JavaScriptObject;

public class SVGAnimatedLengthListImpl extends DOMObject<JavaScriptObject> implements SVGAnimatedLengthList {

    public SVGAnimatedLengthListImpl(JavaScriptObject jso) {
        super(jso);
    }

    @Override
    public native SVGLengthList getAnimVal();

    @Override
    public native SVGLengthList getBaseVal();

    public static SVGAnimatedLengthList wrapSVGAnimatedLengthList(JavaScriptObject jso) {
        return jso == null ? null : new SVGAnimatedLengthListImpl(jso);
    }
}
