package com.googlecode.gwtmapquest.transaction;

import com.google.gwt.core.client.JavaScriptObject;

public class MQAMapCorner extends JavaScriptObject {

    public static native MQAMapCorner topLeftInstance();

    public static native MQAMapCorner topRightInstance();

    public static native MQAMapCorner bottomLeftInstance();

    public static native MQAMapCorner bottomRightInstance();

    protected MQAMapCorner() {
    }
}
