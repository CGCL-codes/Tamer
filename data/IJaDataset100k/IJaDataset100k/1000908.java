package org.srvlnc.base.model.entity;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Serializable id;

    public BaseEntity() {
    }

    public Serializable getId() {
        return id;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof BaseEntity) {
            BaseEntity objEntity = (BaseEntity) obj;
            return id != null && objEntity.getId() != null && id.equals(objEntity.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName());
        builder.append("[id = ").append(getId()).append(" ]");
        return builder.toString();
    }
}
