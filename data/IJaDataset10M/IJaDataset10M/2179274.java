package org.onemind.swingweb.client.gwt.ui;

import org.onemind.swingweb.client.core.AbstractClient;
import org.onemind.swingweb.client.dom.DomNode;
import com.google.gwt.user.client.ui.*;

public class ToggleButtonUIHandler extends ButtonUIHandler {

    public ToggleButtonUIHandler(AbstractClient client) {
        super(client);
    }

    /** 
     * {@inheritDoc}
     */
    protected Object updateUI(Object com, DomNode element) {
        super.updateUI(com, element);
        Button btn = (Button) com;
        String clazz = getStyleName(element);
        if (isTrue(element, "selected")) {
            btn.setStyleName(clazz + "-pressed");
        } else {
            btn.setStyleName(clazz);
        }
        return com;
    }

    public void postEvent(Object sender, Object data) {
        postEvent(sender, "true", true);
    }
}
