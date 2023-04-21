package com.meterware.httpunit.dom;

import org.w3c.dom.html.HTMLBodyElement;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLBodyElementImpl extends HTMLElementImpl implements HTMLBodyElement {

    private HTMLEventHandler _onLoad = new HTMLEventHandler(this, "onload");

    ElementImpl create() {
        return new HTMLBodyElementImpl();
    }

    /**
     * 
     * @return the onload event
     */
    public Function getOnloadEvent() {
        if (getParentScope() == null && getOwnerDocument() instanceof Scriptable) setParentScope((Scriptable) getOwnerDocument());
        return _onLoad.getHandler();
    }

    public String getALink() {
        return getAttributeWithNoDefault("aLink");
    }

    public String getBackground() {
        return getAttributeWithNoDefault("background");
    }

    public String getBgColor() {
        return getAttributeWithNoDefault("bgColor");
    }

    public String getLink() {
        return getAttributeWithNoDefault("link");
    }

    public String getText() {
        return getAttributeWithNoDefault("text");
    }

    public String getVLink() {
        return getAttributeWithNoDefault("vLink");
    }

    public void setALink(String aLink) {
        setAttribute("aLink", aLink);
    }

    public void setBackground(String background) {
        setAttribute("background", background);
    }

    public void setBgColor(String bgColor) {
        setAttribute("bgColor", bgColor);
    }

    public void setLink(String link) {
        setAttribute("link", link);
    }

    public void setText(String text) {
        setAttribute("text", text);
    }

    public void setVLink(String vLink) {
        setAttribute("vLink", vLink);
    }
}
