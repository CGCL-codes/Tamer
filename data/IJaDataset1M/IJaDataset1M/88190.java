package de.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import de.javagimmicks.collections.event.SetEvent.Type;

public class ObservableEventSet<E> extends AbstractEventSet<E> {

    private static final long serialVersionUID = 4799365684601532982L;

    protected transient List<EventSetListener<E>> _listeners;

    public ObservableEventSet(Set<E> decorated) {
        super(decorated);
    }

    public void addEventSetListener(EventSetListener<E> listener) {
        if (_listeners == null) {
            _listeners = new ArrayList<EventSetListener<E>>();
        }
        _listeners.add(listener);
    }

    public void removeEventSetListener(EventSetListener<E> listener) {
        if (_listeners != null) {
            _listeners.remove(listener);
        }
    }

    @Override
    protected void fireElementAdded(E element) {
        fireEvent(new SetEvent<E>(this, Type.ADDED, element));
    }

    @Override
    protected void fireElementReadded(E element) {
        fireEvent(new SetEvent<E>(this, Type.READDED, element));
    }

    @Override
    protected void fireElementRemoved(E element) {
        fireEvent(new SetEvent<E>(this, Type.REMOVED, element));
    }

    private void fireEvent(SetEvent<E> event) {
        if (_listeners == null) {
            return;
        }
        for (EventSetListener<E> listener : _listeners) {
            listener.eventOccured(event);
        }
    }
}
