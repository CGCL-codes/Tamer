package org.onemind.swingweb.client.gwt.ui;

import org.onemind.swingweb.client.core.AbstractClient;
import org.onemind.swingweb.client.dom.DomNode;
import org.onemind.swingweb.client.gwt.widget.Dialog;

public class DialogUIHandler extends WindowUIHandler {

    public DialogUIHandler(AbstractClient client) {
        super(client);
    }

    /** 
     * {@inheritDoc}
     */
    protected Object createComponentInstance(Object parent, DomNode element) {
        Dialog w = new Dialog();
        return w;
    }
}
