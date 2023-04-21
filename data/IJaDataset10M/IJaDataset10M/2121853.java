package com.aurecon.kwb.events;

import com.aurecon.kwb.interfaces.UserEvent;
import com.aurecon.kwb.interfaces.UserEventListener;

/**
 * @author Developer
 *
 */
public class DesktopCloseEvent implements UserEvent {

    public void execute(UserEventListener controller) {
        controller.destroy();
    }
}
