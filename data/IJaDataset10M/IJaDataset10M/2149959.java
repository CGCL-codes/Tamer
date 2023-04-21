package com.sin.client.ui.east2;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class East2VideosViewImpl extends Composite implements East2VideosView {

    private static East2VideosViewImplUiBinder uiBinder = GWT.create(East2VideosViewImplUiBinder.class);

    interface East2VideosViewImplUiBinder extends UiBinder<Widget, East2VideosViewImpl> {
    }

    public East2VideosViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setName(String helloName) {
    }
}
