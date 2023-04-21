package com.privilege.model;

import com.privilege.displayable.Action;

public interface ButtonModel extends Model {

    public Action getAction();

    public void setAction(Action action);
}
