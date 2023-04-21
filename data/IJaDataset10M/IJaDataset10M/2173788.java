package eu.future.earth.gwt.client.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasFileEventHandlers extends HasHandlers {

    HandlerRegistration addFileEventHandler(FileEventListener handler);
}
