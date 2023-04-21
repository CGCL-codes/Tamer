package org.merak.core.persistence;

import java.io.Serializable;

public interface WriteableDAO<Entity, Identifier extends Serializable> extends ReadableDAO<Entity, Identifier> {

    public void insert(Entity entity);

    public void update(Entity entity);

    public void delete(Entity entity);

    public void insertOrUpdate(Entity entity);
}
