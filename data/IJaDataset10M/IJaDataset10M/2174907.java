package org.ewm.messenger;

import org.ewm.XEventManager;
import gnu.x11.event.EnterNotify;
import gnu.x11.event.Event;

/**
 * 
 * @author Erik De Rijcke
 *
 */
public final class DefaultEnterNotifyMessenger extends AbstractEventMessenger {

    private EnterNotify enterNotify;

    public DefaultEnterNotifyMessenger(Event event, XEventManager windowController) {
        super(event, windowController);
    }

    @Override
    public void setEvent(Event event) {
        this.enterNotify = (EnterNotify) event;
    }

    @Override
    public void run() {
    }
}
