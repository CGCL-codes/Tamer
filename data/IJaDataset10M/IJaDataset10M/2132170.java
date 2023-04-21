package org.lobobrowser.html.domimpl;

import org.w3c.dom.html2.HTMLLIElement;

public class HTMLLIElementImpl extends HTMLAbstractUIElement implements HTMLLIElement {

    public HTMLLIElementImpl(String name) {
        super(name);
    }

    public String getType() {
        return this.getAttribute("type");
    }

    public void setType(String type) {
        this.setAttribute("type", type);
    }

    public int getValue() {
        String valueText = this.getAttribute("value");
        if (valueText == null) {
            return 0;
        }
        try {
            return Integer.parseInt(valueText);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public void setValue(int value) {
        this.setAttribute("value", String.valueOf(value));
    }
}
