package com.gwtaf.core.client.incubator;

import com.google.gwt.user.client.ui.CheckBox;

public final class CheckBoxEx extends CheckBox {

    private IWidgetLoadListener listener;

    public CheckBoxEx() {
    }

    public CheckBoxEx(String title) {
        super(title);
    }

    public void addLoadListener(IWidgetLoadListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (listener != null) listener.onLoad(this);
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        if (listener != null) listener.onUnload(this);
    }
}
