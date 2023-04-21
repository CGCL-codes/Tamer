package com.gambi.tapestry.client.gwtui.widgets;

import com.gambi.tapestry.client.gwtui.services.CheckBoxService;
import com.gambi.tapestry.client.gwtui.services.CheckBoxServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

public class CheckBoxWidget extends Composite {

    private final CheckBoxServiceAsync checkBoxService = GWT.create(CheckBoxService.class);

    private String div;

    public CheckBoxWidget(String id, String div, String gwtServiceTarget) {
        this(id, div, gwtServiceTarget, null, null);
    }

    public CheckBoxWidget(String id, String div, String eventLink, String[] events) {
        this(id, div, null, eventLink, events);
    }

    public CheckBoxWidget(String id, String div, String gwtServiceTarget, String eventLink, String[] events) {
        GWT.log(" gwtServiceTarget " + gwtServiceTarget);
        this.div = div;
        if (gwtServiceTarget != null) {
            ((ServiceDefTarget) checkBoxService).setServiceEntryPoint(gwtServiceTarget);
        } else if (eventLink != null && events != null) {
            ((ServiceDefTarget) checkBoxService).setServiceEntryPoint(eventLink + events[0]);
        }
        CheckBox checkBox = new CheckBox();
        checkBox.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                sendValue(((CheckBox) event.getSource()).getValue());
            }
        });
        initWidget(checkBox);
    }

    void sendValue(Boolean value) {
        checkBoxService.change(value, !value, "A String", new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                GWT.log("OnFailure " + caught.getMessage());
                if (div.length() > 0) {
                    Element e = RootPanel.get(div).getElement();
                    e.setInnerHTML("OnFailure " + caught);
                }
            }

            public void onSuccess(String result) {
                GWT.log("OnSuccess " + result);
                if (div.length() > 0) {
                    Element e = RootPanel.get(div).getElement();
                    e.setInnerHTML("OnSuccess " + result);
                }
            }
        });
    }
}
