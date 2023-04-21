package com.gwtaf.core.client.fieldadapter;

import com.google.gwt.user.client.ui.Widget;
import com.gwtaf.core.client.datacontext.IDataContext;

public interface IFieldAdapter<T> {

    void setContext(IDataContext<? extends T> ctx);

    void updateField();

    void updateModel();

    Widget getWidget();

    void updatePermission();
}
