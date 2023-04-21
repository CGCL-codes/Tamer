package com.sin.client.ui.west2;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sin.client.ui.west2.West2VideosView.Presenter;

public class West2VideosViewImpl extends Composite implements West2VideosView {

    private static West2VideosViewImplUiBinder uiBinder = GWT.create(West2VideosViewImplUiBinder.class);

    interface West2VideosViewImplUiBinder extends UiBinder<Widget, West2VideosViewImpl> {
    }

    private Presenter presenter;

    public West2VideosViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
