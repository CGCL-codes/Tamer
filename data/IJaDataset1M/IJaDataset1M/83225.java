package com.csam.browser.liveconnect.dom;

import com.csam.browser.JApplet2;
import netscape.javascript.JSObject;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;

/**
 *
 * @author Nathan Crause <ncrause at clarkesolomou.com>
 */
public class LiveConnectAttr extends LiveConnectNode implements Attr {

    public LiveConnectAttr(JSObject js, JApplet2 applet) {
        super(js, applet);
    }

    public String getName() {
        return getStr("name");
    }

    public boolean getSpecified() {
        return (Boolean) getMember("specified");
    }

    public String getValue() {
        return getStr("value");
    }

    public void setValue(String value) throws DOMException {
        setMember("value", value);
    }

    public Element getOwnerElement() {
        return LiveConnectElement.toElement(getMember("ownerElement"), getApplet());
    }

    public TypeInfo getSchemaTypeInfo() {
        return (TypeInfo) getMember("schemaTypeInfo");
    }

    public boolean isId() {
        return (Boolean) call("isId");
    }
}
