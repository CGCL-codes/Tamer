package com.generalynx.common.data;

public abstract class DefaultEntity implements IEntity {

    public boolean equals(Object obj) {
        IEntity entity = (IEntity) obj;
        return (getId() == entity.getId());
    }
}
