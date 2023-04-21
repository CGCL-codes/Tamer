package com.belt.design.session;

import com.belt.design.entity.Item;
import com.belt.design.entity.Param;

public interface ParamAction {

    public void control(Param activeParam);

    public void addItem(Param activeParam, String itemValue, String itemDescription);

    public void removeItem(Param activeParam, Item item);
}
