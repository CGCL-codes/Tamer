package org.ewm.messenger;

import org.ewm.XEventManager;
import gnu.x11.event.Event;
import gnu.x11.event.FocusOut;

/**
 * 
 * @author Erik De Rijcke
 *
 */
public final class DefaultFocusOutMessenger extends AbstractEventMessenger {

    private FocusOut focusOut;

    public DefaultFocusOutMessenger(Event event, XEventManager windowController) {
        super(event, windowController);
    }

    @Override
    public void setEvent(Event event) {
        this.focusOut = (FocusOut) event;
    }

    @Override
    public void run() {
    }
}
