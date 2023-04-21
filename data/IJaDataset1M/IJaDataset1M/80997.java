package com.leaguefox.web.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface PlayerDeletedEventHandler extends EventHandler {

    void onPlayerDeleted(PlayerDeletedEvent event);
}
