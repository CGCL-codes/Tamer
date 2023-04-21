package org.openremote.web.console.event.controller;

import com.google.gwt.event.shared.EventHandler;

public interface ControllerMessageHandler extends EventHandler {

    void onControllerMessage(ControllerMessageEvent event);
}
