package com.google.gwt.visualization.client.events;

import com.google.gwt.ajaxloader.client.Properties;
import com.google.gwt.ajaxloader.client.Properties.TypeException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.visualization.client.visualizations.Visualization;

/**
 * The base class for visualization event handlers.
 */
public abstract class Handler {

    /**
   * Add a Handler to a visualization.
   * 
   * @param viz a Visualization supporting the given event.
   * @param eventName The name of the event.
   * @param handler A Handler to add.
   */
    public static native void addHandler(Visualization<?> viz, String eventName, Handler handler);

    @SuppressWarnings("unused")
    private static void onCallback(final Handler handler, final Properties properties) {
        try {
            handler.onEvent(properties);
        } catch (Throwable x) {
            GWT.getUncaughtExceptionHandler().onUncaughtException(x);
        }
    }

    /**
   * This method should be overridden by event-specific Handler subclasses. The
   * subclass should extract the event properties (if any), create a GWT Event
   * bean object, and pass it to the event-specific callback.
   * 
   * @param properties The JavaScriptObject containing data about the event.
   * @throws TypeException If some property of the event has an unexpected type.
   */
    protected abstract void onEvent(Properties properties) throws TypeException;
}
