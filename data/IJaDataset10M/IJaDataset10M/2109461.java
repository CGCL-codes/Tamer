package org.illico.common.model.sort;

import org.illico.common.event.Event;
import org.illico.common.event.SimpleEventManager;
import org.illico.common.event.SimpleListener;

public abstract class AbstractSortModel<T> implements SortModel<T> {

    private SimpleEventManager<SortModel<T>> changedManager;

    public AbstractSortModel() {
        this.changedManager = new SimpleEventManager<SortModel<T>>();
    }

    protected void fireChangedEvent() {
        changedManager.fireEvent(new Event<SortModel<T>>(this));
    }

    public void addChangedListener(SimpleListener<SortModel<T>> listener) {
        changedManager.getListeners().add(listener);
    }

    public void removeChangedListener(SimpleListener<SortModel<T>> listener) {
        changedManager.getListeners().remove(listener);
    }
}
