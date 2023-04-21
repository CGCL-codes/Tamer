package org.swemas.core.event;

import org.swemas.core.IChannel;

/**
 * @author Alexey Chernov
 * 
 */
public interface IEventDispatchingChannel extends IChannel {

    void registerQueue(Class<?> eventType);

    void event(Event event);

    void event(Event event, int priority);

    void addListener(Class<?> eventType, IEventListeningChannel listener);

    void removeListener(Class<?> eventType, IEventListeningChannel listener);
}
