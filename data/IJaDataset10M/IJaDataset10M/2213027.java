package org.columba.core.filter;

import org.columa.core.config.IDefaultItem;

public interface IFilterActionList extends IDefaultItem {

    IFilterAction get(int index);

    void remove(int index);

    void addEmptyAction();
}
