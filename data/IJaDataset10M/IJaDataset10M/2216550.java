package com.gwtmobile.phonegap.kitchensink.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.phonegap.client.Compass;
import com.gwtmobile.phonegap.client.Compass.Callback;
import com.gwtmobile.phonegap.client.Compass.Options;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Page;

public class CompassUi extends Page {

    @UiField
    HTML text;

    String watchId;

    private static CompassUiUiBinder uiBinder = GWT.create(CompassUiUiBinder.class);

    interface CompassUiUiBinder extends UiBinder<Widget, CompassUi> {
    }

    public CompassUi() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    protected void onUnload() {
        clearWatch();
        super.onUnload();
    }

    @UiHandler("list")
    void onListSelectionChanged(SelectionChangedEvent e) {
        switch(e.getSelection()) {
            case 0:
                getCurrentHeading();
                break;
            case 1:
                watchHeading();
                break;
            case 2:
                clearWatch();
                break;
        }
    }

    public void getCurrentHeading() {
        Compass.getCurrentHeading(new Callback() {

            @Override
            public void onSuccess(float heading) {
                text.setHTML("Current Heading:<br/>" + heading);
            }

            @Override
            public void onError() {
                text.setText("Error");
            }
        });
    }

    public void watchHeading() {
        watchId = Compass.watchHeading(new Callback() {

            @Override
            public void onSuccess(float heading) {
                text.setHTML("Watch Heading:<br/>" + heading);
            }

            @Override
            public void onError() {
                text.setHTML("Error");
            }
        }, new Options().frequency(100));
    }

    public void clearWatch() {
        if (watchId != null) {
            Compass.clearWatch(watchId);
            text.setText("");
            watchId = null;
        }
    }
}
