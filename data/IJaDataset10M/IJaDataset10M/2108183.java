package com.mathassistant.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class DefaultContentPresenter implements Presenter {

    public interface Display {

        Widget asWidget();
    }

    private final Display display;

    public DefaultContentPresenter(Display view) {
        this.display = view;
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(display.asWidget());
    }

    @Override
    public void go() {
    }
}
