package com.bazaaroid.client.web.gwt.clientmodule.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface ClientModuleView {

    public interface Presenter {
    }

    void setPresenter(Presenter presenter);

    Widget asWidget();
}
