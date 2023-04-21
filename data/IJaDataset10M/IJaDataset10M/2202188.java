package org.openremote.web.console.event.press;

import com.google.gwt.event.shared.EventHandler;

public interface PressEndHandler extends EventHandler {

    void onPressEnd(PressEndEvent event);
}
