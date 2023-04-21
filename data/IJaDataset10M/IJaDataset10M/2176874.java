package com.wrupple.muba.catalogs.client.widgets.editors.impl;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.wrupple.muba.catalogs.client.activity.process.task.CatalogInteractionState.Mode;
import com.wrupple.muba.catalogs.client.widgets.editors.CatalogEditor;
import com.wrupple.muba.common.shared.State.ProcessManager;

public abstract class SimpleCatalogEditor<V> extends ResizeComposite implements CatalogEditor<V> {

    protected Mode mode;

    protected EventBus bus;

    protected String catalogid;

    protected ProcessManager pm;

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<V> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void reset() {
        setValue(null);
    }

    @Override
    public void initialize(String catalog, Mode mode, EventBus bus) {
        this.catalogid = catalog;
        this.mode = mode;
        this.pm = pm;
        this.bus = bus;
    }

    public Mode getMode() {
        return mode;
    }

    public String getCatalogId() {
        return catalogid;
    }
}
