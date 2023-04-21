package com.siberhus.stars.model;

import java.io.Serializable;

public interface Model<ID extends Serializable> extends Serializable {

    public ID getId();

    public void setId(ID id);

    public boolean isNew();

    public String[] toNameKeys();

    public String[] toNames();

    public Object[] toValues();
}
