package org.openremote.web.console.event.press;

import com.google.gwt.event.shared.EventHandler;

public interface PressCancelHandler extends EventHandler {

    void onPressCancel(PressCancelEvent event);
}
