package org.lobobrowser.html.domimpl;

import org.w3c.dom.html2.HTMLPreElement;

public class HTMLPreElementImpl extends HTMLAbstractUIElement implements HTMLPreElement {

    public HTMLPreElementImpl(String name) {
        super(name);
    }

    public int getWidth() {
        String widthText = this.getAttribute("width");
        if (widthText == null) {
            return 0;
        }
        try {
            return Integer.parseInt(widthText);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public void setWidth(int width) {
        this.setAttribute("width", String.valueOf(width));
    }
}
