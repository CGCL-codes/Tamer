package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ErrorHandler {

    <E> void onError(Subscription<E> subscription, Event<E> event, Exception e);
}
