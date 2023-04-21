package org.openremote.web.console.event.drag;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author rich
 *
 */
public abstract class DragEvent<H extends EventHandler> extends GwtEvent<H> {

    protected int xPos;

    protected int yPos;

    protected Widget source;

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public Widget getSource() {
        return source;
    }
}
