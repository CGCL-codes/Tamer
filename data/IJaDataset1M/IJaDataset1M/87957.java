package org.merak.core.persistence;

import java.io.Serializable;
import java.util.List;

public interface ReadableDAO<Entity, Identifier extends Serializable> {

    public Entity load(Identifier id);

    public Entity loadBy(String attribute, Object value);

    public List<Entity> loadAll();

    public List<Entity> loadAllBy(String attribute, Object value);

    public int count();
}
