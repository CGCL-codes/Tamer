package org.pageley.games.domain.environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.pageley.games.domain.entity.Entity;
import org.pageley.games.domain.event.Event;
import org.pageley.games.domain.event.EventListener;

public abstract class AbstractEnvironment implements Environment, EventListener {

    private Map<UUID, Entity> entities;

    private List<Event> events;

    public AbstractEnvironment() {
        this.entities = new HashMap<UUID, Entity>();
        this.events = new ArrayList<Event>();
    }

    @Override
    public void addEntity(Entity entity) {
        this.entities.put(entity.getId(), entity);
        entity.setEnvironment(this);
        entity.addEventListener(this);
    }

    public Collection<Entity> getEntities() {
        return this.entities.values();
    }

    @Override
    public List<Event> dequeueEvents() {
        List<Event> result = new ArrayList<Event>(this.events);
        this.events.clear();
        return result;
    }

    @Override
    public void eventOccured(Event event) {
        this.events.add(event);
    }

    @Override
    public Entity getEntity(UUID entityId) {
        return this.entities.get(entityId);
    }
}
