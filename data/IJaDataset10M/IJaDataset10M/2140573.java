package eu.future.earth.gwt.client.date;

import com.google.gwt.event.shared.EventHandler;

public interface DateEventListener<T> extends EventHandler {

    void handleDateEvent(DateEvent<T> newEvent);
}
