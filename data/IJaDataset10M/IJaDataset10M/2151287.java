package org.gwt.formlayout.client.util;

import com.google.gwt.user.client.Element;

public class DOMImplIE6 extends DOMImpl {

    @Override
    public native String getComputedStyleAttribute(Element elem, String attr);

    public native void setStyleAttribute(Element elem, String attr, String value);
}
