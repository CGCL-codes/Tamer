package com.wrupple.muba.catalogs.client.view;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.wrupple.muba.catalogs.client.events.DataSelectionRequest;
import com.wrupple.muba.widget.client.view.SelectionTask;
import com.wrupple.muba.widget.client.widgets.toolbar.Toolbar;

public interface SelectionToolbar<T extends JavaScriptObject> extends Toolbar, SelectionTask<T> {

    public interface SelectionRequestHandler extends EventHandler {

        void onSelectionReques(DataSelectionRequest event);
    }

    HandlerRegistration addSelectionRequestHandler(SelectionRequestHandler handler);

    void addToSelection(T result);

    void clear();
}
