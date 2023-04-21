package com.luzan.app.vist.gwt.control.graber.client;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.GWT;
import com.luzan.app.vist.gwt.control.tager.client.TagerControl;

/**
 * GraberControl
 *
 * @author Alexander Bondar
 */
public class GraberControl extends VerticalPanel {

    TextBox url;

    Label label;

    TagerControl tager = new TagerControl();

    Image imageSubmit = new Image();

    Image imageCancel = new Image();

    GraberControlConstants constants = (GraberControlConstants) GWT.create(GraberControlConstants.class);

    public GraberControl() {
        tager.addStyleName("graber");
        HorizontalPanel hUrlPanel = new HorizontalPanel();
        VerticalPanel hCtrPanel = new VerticalPanel();
        HorizontalPanel hImgPanel = new HorizontalPanel();
        hUrlPanel.setStyleName("graber");
        label = new Label(constants.label() + ":", false);
        hUrlPanel.add(label);
        url = new TextBox();
        hUrlPanel.add(url);
        imageSubmit.setTitle("Submit");
        imageCancel.setTitle("Cancel");
        hCtrPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        hCtrPanel.setWidth("100%");
        hImgPanel.add(imageSubmit);
        hImgPanel.add(imageCancel);
        hCtrPanel.add(hImgPanel);
        this.add(hUrlPanel);
        this.add(tager);
        this.add(hCtrPanel);
    }

    public void addSubmitClickListener(ClickListener listener) {
        imageSubmit.addClickListener(listener);
    }

    public void addCancelClickListener(ClickListener listener) {
        imageCancel.addClickListener(listener);
    }

    public String getUrl() {
        return url.getText();
    }

    public TagerControl getTager() {
        return tager;
    }

    public void setUrl(String url) {
        this.url.setText(url);
    }

    public void setTags(String tags) {
        this.tager.setTags(tags);
    }
}
