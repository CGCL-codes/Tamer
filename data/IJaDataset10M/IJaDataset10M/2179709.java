package org.stjs.javascript.dom;

import org.stjs.javascript.functions.Function1;

public abstract class Element extends Node {

    public String className;

    public String dir;

    public String id;

    public String lang;

    public String title;

    public String tagName;

    public String innerHTML;

    public int clientHeight;

    public int clientWidth;

    public int height;

    public int offsetHeight;

    public int offsetLeft;

    public Element offsetParent;

    public int offsetTop;

    public int offsetWidth;

    public int scrollHeight;

    public int scrollLeft;

    public int scrollTop;

    public int scrollWidth;

    public int width;

    public Function1<DOMEvent, Boolean> onblur;

    public Function1<DOMEvent, Boolean> onchange;

    public Function1<DOMEvent, Boolean> onclick;

    public Function1<DOMEvent, Boolean> ondblclick;

    public Function1<DOMEvent, Boolean> onerror;

    public Function1<DOMEvent, Boolean> onfocus;

    public Function1<DOMEvent, Boolean> onkeydown;

    public Function1<DOMEvent, Boolean> onkeypress;

    public Function1<DOMEvent, Boolean> onkeyup;

    public Function1<DOMEvent, Boolean> onmousedown;

    public Function1<DOMEvent, Boolean> onmousemove;

    public Function1<DOMEvent, Boolean> onmouseout;

    public Function1<DOMEvent, Boolean> onmouseover;

    public Function1<DOMEvent, Boolean> onmouseup;

    public Function1<DOMEvent, Boolean> onselect;

    public String getAttribute(String name) {
        throw new UnsupportedOperationException();
    }

    public Attr getAttributeNode(String name) {
        throw new UnsupportedOperationException();
    }

    public HTMLList<Element> getElementsByTagName(String tag) {
        throw new UnsupportedOperationException();
    }

    public boolean hasAttribute(String name) {
        throw new UnsupportedOperationException();
    }

    public void removeAttribute(String name) {
        throw new UnsupportedOperationException();
    }

    public void removeAttributeNode(Attr att) {
        throw new UnsupportedOperationException();
    }

    public void setAttribute(String name, String value) {
        throw new UnsupportedOperationException();
    }

    public void setAttributeNode(Attr name) {
        throw new UnsupportedOperationException();
    }

    public void setIdAttribute(String name, boolean id) {
        throw new UnsupportedOperationException();
    }

    public void setIdAttributeNode(Attr attr, boolean id) {
        throw new UnsupportedOperationException();
    }
}
