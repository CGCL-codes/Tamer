package org.eesgmbh.gimv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ViewportDragFinishedEventHandler extends EventHandler {

    void onDragFinished(ViewportDragFinishedEvent event);
}
