package com.googlecode.gwtmapquest.jsapi;

import com.google.gwt.core.client.JavaScriptObject;

public class MQObjectCollection extends MQObject {

    protected MQObjectCollection() {
    }

    public final native int getSize();

    public final native int add(JavaScriptObject obj);
}
