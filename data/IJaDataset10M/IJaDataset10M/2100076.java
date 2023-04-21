package com.robaone.gwt.hierarcialdb.client.ui.photos;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class PhotosUi extends Composite {

    private static PhotosUiUiBinder uiBinder = GWT.create(PhotosUiUiBinder.class);

    interface PhotosUiUiBinder extends UiBinder<Widget, PhotosUi> {
    }

    public PhotosUi() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
