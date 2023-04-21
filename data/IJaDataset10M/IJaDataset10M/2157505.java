package org.butu.core.entity;

import java.io.Serializable;

public interface IEntity extends Serializable {

    public Object getId();

    public void setId(Object id);
}
