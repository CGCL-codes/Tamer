package com.googlecode.gwtmapquest.jsapi;

import com.google.gwt.core.client.JavaScriptObject;

public class MQUtils {

    public static native JavaScriptObject mqGetNode(String xmlDoc, String xPath);

    public static native String mqXmlToStr(JavaScriptObject xmlNode);
}
