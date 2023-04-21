package org.caleigo.core;

public interface IBinaryEntityAction extends IEntityAction {

    public void perform(IEntity primaryEntity, IEntity secondaryEntity);
}
