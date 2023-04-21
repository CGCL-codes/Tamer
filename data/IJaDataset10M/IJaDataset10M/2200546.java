package org.jomp.prototype.shared.event;

public interface EventSource<T extends Event> {

    void addListener(EventListener<T> listener);

    void removeListener(EventListener<T> listener);

    void fireEvent(T event);
}
