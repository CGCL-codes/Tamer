package org.sodeja.rel;

public interface BaseRelationListener {

    public void inserted(BaseRelation relation, Entity entity);

    public void updated(BaseRelation relation, Entity oldEntity, Entity newEntity);

    public void deleted(BaseRelation relation, Entity entity);
}
