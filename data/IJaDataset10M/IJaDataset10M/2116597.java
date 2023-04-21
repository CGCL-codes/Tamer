package org.ewmt.messenger;

import org.ewmt.XEventManager;
import gnu.x11.event.Event;
import gnu.x11.event.MapNotify;

/**
 * 
 * @author Erik De Rijcke
 *
 */
public final class DefaultMapNotifyMessenger extends AbstractEventMessenger {

    private MapNotify mapNotify;

    public DefaultMapNotifyMessenger(Event event, XEventManager windowController) {
        super(event, windowController);
    }

    @Override
    public void setEvent(Event event) {
        this.mapNotify = (MapNotify) event;
    }

    @Override
    public void run() {
    }
}
