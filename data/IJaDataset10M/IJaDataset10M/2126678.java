package org.leviatan.definator.core.model;

import java.io.Serializable;

public abstract interface Record extends Serializable {

    public abstract String getId();

    public abstract void setId(String aId);

    public abstract String getName();

    public abstract void setName(String aName);

    public abstract String getTableRef();

    public abstract void setTableRef(String aId);
}
