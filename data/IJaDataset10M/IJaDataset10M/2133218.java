package com.anzsoft.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;

public class XmlDocument extends JavaScriptObject {

    protected XmlDocument() {
    }

    public final native Element documentElement();

    public final native Element firstChild();

    public final native Element createElement(final String nodeName);

    public final native Element createElementNS(final String xmlns, final String elName);

    public final native Node createTextNode(final String content);

    public final native void loadXML(final String xml);

    public static native XmlDocument create(final String name, final String ns);
}
