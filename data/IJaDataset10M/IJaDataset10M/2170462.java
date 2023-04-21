package org.timepedia.chronoscope.client.gss;

import org.timepedia.chronoscope.client.canvas.View;

/**
 * A GssContext is responsible for mapping GssElement/pseudoElt pairs into
 * GssProperties objects. 
 */
public class GssContext {

    protected View view;

    public GssContext(View view) {
        this.view = view;
    }

    public GssContext() {
    }

    public GssProperties getProperties(GssElement gssElem, String pseudoElt) {
        return new MockGssProperties();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public GssProperties getPropertiesBySelector(String gssSelector) {
        return new GssProperties();
    }
}
