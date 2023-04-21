package org.homemotion.events.dao;

import javax.ejb.LocalBean;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import org.homemotion.dao.AbstractItemManagerImpl;
import org.homemotion.dao.Item;
import org.homemotion.devices.DataEvent;
import org.homemotion.events.AbstractEvent;
import org.homemotion.events.EventDefinition;
import org.homemotion.events.EventManager;

@LocalBean
public abstract class AbstractEventAwareItemManagerImpl<T extends Item> extends AbstractItemManagerImpl<T> {

    protected abstract String getEventSource();

    protected EventDefinition eventDef;

    private EventManager eventManager;

    private Class<T> itemClass;

    @Inject
    @Any
    javax.enterprise.event.Event<AbstractEvent> daoEvent;

    protected AbstractEventAwareItemManagerImpl(Class<T> itemClass, EventManager eventManager) {
        this.eventManager = eventManager;
        this.itemClass = itemClass;
        logger.debug("Registering events for " + getClass() + "..:");
        eventDef = new EventDefinition(DataEvent.class, itemClass.getName(), "CRUD Events for class: " + itemClass, getClass());
        eventManager.registerEventDefinition(eventDef);
    }

    protected Class<T> getItemClass() {
        return itemClass;
    }

    protected EventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public Long create(T item) {
        try {
            return super.create(item);
        } finally {
            DataEvent evt = DataEvent.createItemCreateEvent(this, item);
            evt.setReadOnly();
            daoEvent.fire(evt);
        }
    }

    @Override
    public void delete(T item) {
        super.delete(item);
        DataEvent evt = DataEvent.createItemDeleteEvent(this, item);
        evt.setReadOnly();
        daoEvent.fire(evt);
    }

    @Override
    public T update(T item) {
        T result = null;
        result = super.update(item);
        DataEvent evt = DataEvent.createItemUpdateEvent(this, item);
        evt.setReadOnly();
        daoEvent.fire(evt);
        return result;
    }
}
