package com.google.gwt.language.client.transliteration.control;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

/**
 * Listener of transliteration events. Override this class' abstract method
 * onEvent() to put your own logic.
 */
public abstract class TransliterationEventListener {

    /**
   * Called when an event is triggered.
   *
   * @param result the event object
   */
    protected abstract void onEvent(TransliterationEventDetail result);

    /**
   * This wraps onEvent method and provides a framework for catching
   * exceptions in callbacks.
   *
   * @param event the event object.
   */
    public void onEventWrapper(TransliterationEventDetail event) {
        UncaughtExceptionHandler exceptionHandler = GWT.getUncaughtExceptionHandler();
        if (exceptionHandler != null) {
            try {
                onEvent(event);
            } catch (Exception e) {
                exceptionHandler.onUncaughtException(e);
            }
        } else {
            onEvent(event);
        }
    }
}
