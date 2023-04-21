package com.mathassistant.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

public abstract interface Presenter {

    public abstract void go(final HasWidgets container);

    public abstract void go();
}
