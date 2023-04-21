package com.luzan.app.map.gwt.ui.client;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.core.client.GWT;
import com.luzan.app.map.gwt.constants.client.TextConstants;
import com.luzan.app.map.gwt.constants.client.TextUtils;
import com.luzan.app.map.gwt.bean.client.MapOriginal;
import com.luzan.common.gwt.agent.client.AbstractHTTPAgent;
import com.luzan.common.gwt.agent.client.UserAgent;
import com.luzan.common.gwt.ui.client.XFileUpload;
import com.luzan.common.gwt.ui.client.RequestStatusPanel;

/**
 * MapCalibrateControl
 *
 * @author Alexander Bondar
 */
public class MapCalibrateControl extends HorizontalPanel {

    static TextConstants textConstants = (TextConstants) GWT.create(TextConstants.class);

    TextBox tbCrLat = new TextBox();

    TextBox tbCrLon = new TextBox();

    TextBox tbSouth = new TextBox();

    TextBox tbWest = new TextBox();

    TextBox tbNorth = new TextBox();

    TextBox tbEast = new TextBox();

    TextBox offXTB = new TextBox();

    TextBox offYTB = new TextBox();

    TextBox areaTB = new TextBox();

    XFileUpload fileUpload = new XFileUpload();

    Button btnSubmit = new Button("Calibrate");

    Button btnCancel = new Button("Cancel");

    RadioButton rbExisting = new RadioButton("map-calibrate", "Last time uploaded image");

    RadioButton rbUpload = new RadioButton("map-calibrate", "Upload from local computer");

    RadioButton rbDownload = new RadioButton("map-calibrate", "Upload from web");

    VerticalPanel mainPanel = new VerticalPanel();

    SimplePanel messagePanel = new SimplePanel();

    VerticalPanel existingPanel = new VerticalPanel();

    VerticalPanel uploadImgPanel = new VerticalPanel();

    VerticalPanel downloadPanel = new VerticalPanel();

    ClickListenerCollection submitClickListeners = new ClickListenerCollection();

    static RequestStatusPanel status = new RequestStatusPanel();

    public MapCalibrateControl(MapOriginal map) {
        this.setWidth("100%");
        final FormPanel form = new FormPanel();
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);
        form.addFormHandler(new FormHandler() {

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                if (event == null || TextUtils.isEmpty(event.getResults())) status.complete("Slow connection or server did not respond in time."); else if (event.getResults().indexOf("SUCCESS") >= 0) status.complete("Data sent successfully. Calibrating has started."); else status.complete("Data sending was failed. Calibrating has not been started.");
                submitClickListeners.fireClick(MapCalibrateControl.this);
            }

            public void onSubmit(FormSubmitEvent event) {
            }
        });
        form.setAction(AbstractHTTPAgent.getUrlPrefix("map") + "/map.calibrate");
        fileUpload.setName("uploadFile");
        DOM.setElementAttribute(fileUpload.getElement(), "size", "50");
        VerticalPanel uploadPanel = new VerticalPanel();
        uploadPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        uploadPanel.add(fileUpload);
        FlexTable table = new FlexTable();
        table.setCellSpacing(5);
        int r = 0;
        table.setWidget(r, 0, new Label(textConstants.south()));
        table.getCellFormatter().setHorizontalAlignment(r, 0, ALIGN_RIGHT);
        table.setWidget(r, 1, tbSouth);
        table.setWidget(r, 2, new Label(textConstants.west()));
        table.getCellFormatter().setHorizontalAlignment(r, 2, ALIGN_RIGHT);
        table.setWidget(r, 3, tbWest);
        r++;
        table.setWidget(r, 0, new Label(textConstants.north()));
        table.getCellFormatter().setHorizontalAlignment(r, 0, ALIGN_RIGHT);
        table.setWidget(r, 1, tbNorth);
        table.setWidget(r, 2, new Label(textConstants.east()));
        table.getCellFormatter().setHorizontalAlignment(r, 2, ALIGN_RIGHT);
        table.setWidget(r, 3, tbEast);
        tbEast.setName("east");
        tbWest.setName("west");
        tbSouth.setName("south");
        tbNorth.setName("north");
        this.setStyleName("map-calibrate");
        VerticalPanel imageSourcePanel = new VerticalPanel();
        imageSourcePanel.setWidth("100%");
        imageSourcePanel.add(new Label("Please, select the image source for the map \"" + map.getName() + "\":", false));
        if (TextUtils.isCalibrate(map.getState()) && TextUtils.isComplete(map.getSubState())) {
            final Image img = new Image(AbstractHTTPAgent.getUrlPrefix("map") + "/map.thmb?guid=" + map.getGuid() + "&size=s");
            img.addClickListener(new ClickListener() {

                public void onClick(Widget sender) {
                    rbExisting.setChecked(true);
                }
            });
            existingPanel.add(rbExisting);
            existingPanel.add(img);
            DOM.setStyleAttribute(img.getElement(), "marginLeft", "30px");
            imageSourcePanel.add(existingPanel);
            rbExisting.setChecked(true);
        } else if (Utils.isEmpty(map.getMapUrl())) rbUpload.setChecked(true); else rbDownload.setChecked(true);
        uploadImgPanel.add(rbUpload);
        uploadImgPanel.add(uploadPanel);
        DOM.setStyleAttribute(uploadPanel.getElement(), "marginLeft", "30px");
        TextBox tbUrl = new TextBox();
        tbUrl.setWidth("400px");
        tbUrl.setText(map.getMapUrl());
        downloadPanel.add(rbDownload);
        downloadPanel.add(tbUrl);
        DOM.setStyleAttribute(tbUrl.getElement(), "marginLeft", "30px");
        tbUrl.setName("mapUrl");
        imageSourcePanel.add(uploadImgPanel);
        imageSourcePanel.add(downloadPanel);
        mainPanel.add(imageSourcePanel);
        mainPanel.add(new Label("Calibration points (degrees):"));
        mainPanel.add(table);
        mainPanel.add(new Label("Calibration points correction (for non WGS 84):"));
        FlexTable corrTable = new FlexTable();
        corrTable.setCellSpacing(5);
        r = 0;
        corrTable.setWidget(r, 0, new Label("latitude"));
        corrTable.getCellFormatter().setHorizontalAlignment(r, 0, ALIGN_RIGHT);
        corrTable.setWidget(r, 1, tbCrLat);
        corrTable.setWidget(r, 2, new Label("longitude"));
        corrTable.getCellFormatter().setHorizontalAlignment(r, 2, ALIGN_RIGHT);
        corrTable.setWidget(r, 3, tbCrLon);
        mainPanel.add(corrTable);
        HorizontalPanel btnPanel = new HorizontalPanel();
        btnPanel.add(btnSubmit);
        btnPanel.add(btnCancel);
        mainPanel.add(btnPanel);
        mainPanel.setCellHorizontalAlignment(btnPanel, HasHorizontalAlignment.ALIGN_CENTER);
        existingPanel.setWidth("100%");
        uploadImgPanel.setWidth("100%");
        downloadPanel.setWidth("100%");
        DOM.setStyleAttribute(imageSourcePanel.getElement(), "marginBottom", "10px");
        DOM.setStyleAttribute(table.getElement(), "marginBottom", "10px");
        DOM.setStyleAttribute(existingPanel.getElement(), "marginLeft", "10px");
        DOM.setStyleAttribute(uploadImgPanel.getElement(), "marginLeft", "10px");
        DOM.setStyleAttribute(downloadPanel.getElement(), "marginLeft", "10px");
        DOM.setStyleAttribute(existingPanel.getElement(), "marginTop", "10px");
        DOM.setStyleAttribute(uploadImgPanel.getElement(), "marginTop", "10px");
        DOM.setStyleAttribute(downloadPanel.getElement(), "marginTop", "10px");
        DOM.setStyleAttribute(existingPanel.getElement(), "padding", "10px");
        DOM.setStyleAttribute(uploadImgPanel.getElement(), "padding", "10px");
        DOM.setStyleAttribute(downloadPanel.getElement(), "padding", "10px");
        DOM.setStyleAttribute(existingPanel.getElement(), "backgroundColor", "#f2f0ff");
        DOM.setStyleAttribute(uploadImgPanel.getElement(), "backgroundColor", "#f2f0ff");
        DOM.setStyleAttribute(downloadPanel.getElement(), "backgroundColor", "#f2f0ff");
        DOM.setStyleAttribute(btnPanel.getElement(), "marginTop", "15px");
        tbSouth.setText(Double.toString(map.getSWLat()));
        tbWest.setText(Double.toString(map.getSWLon()));
        tbNorth.setText(Double.toString(map.getNELat()));
        tbEast.setText(Double.toString(map.getNELon()));
        messagePanel.setWidth("300px");
        mainPanel.setWidth("500px");
        this.add(messagePanel);
        final Hidden hiddenMapGuid = new Hidden("guid", map.getGuid());
        final Hidden hiddenAuth = new Hidden("auth", UserAgent.getAuth());
        uploadPanel.add(hiddenAuth);
        uploadPanel.add(hiddenMapGuid);
        DOM.setStyleAttribute(form.getElement(), "display", "inline");
        form.setWidget(mainPanel);
        this.add(form);
        tbUrl.addChangeListener(changeListener);
        fileUpload.addChangeListener(changeListener);
        tbUrl.addFocusListener(focusListener);
        fileUpload.addFocusListener(focusListener);
        btnSubmit.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                final String v;
                if (rbDownload.isChecked()) v = "download"; else if (rbUpload.isChecked()) v = "upload"; else v = "current";
                mainPanel.add(new Hidden("mapSource", v));
                status.start("Sending data...");
                form.submit();
            }
        });
    }

    public MapOriginal populateBean(MapOriginal map) {
        map.setSWLat(Double.parseDouble(tbSouth.getText()));
        map.setSWLon(Double.parseDouble(tbWest.getText()));
        map.setNELat(Double.parseDouble(tbNorth.getText()));
        map.setNELon(Double.parseDouble(tbEast.getText()));
        return map;
    }

    public void addSubmitClickListener(ClickListener submit) {
        if (submit != null) submitClickListeners.add(submit);
    }

    public void addCancelClickListener(ClickListener cancel) {
        if (cancel != null) btnCancel.addClickListener(cancel);
    }

    public ChangeListener changeListener = new ChangeListener() {

        public void onChange(Widget sender) {
            if (DOM.isOrHasChild(existingPanel.getElement(), sender.getElement())) {
                rbExisting.setChecked(true);
            } else if (DOM.isOrHasChild(uploadImgPanel.getElement(), sender.getElement())) {
                rbUpload.setChecked(true);
            } else if (DOM.isOrHasChild(downloadPanel.getElement(), sender.getElement())) {
                rbDownload.setChecked(true);
            }
        }
    };

    public FocusListener focusListener = new FocusListener() {

        public void onFocus(Widget sender) {
            if (DOM.isOrHasChild(existingPanel.getElement(), sender.getElement())) {
                rbExisting.setChecked(true);
            } else if (DOM.isOrHasChild(uploadImgPanel.getElement(), sender.getElement())) {
                rbUpload.setChecked(true);
            } else if (DOM.isOrHasChild(downloadPanel.getElement(), sender.getElement())) {
                rbDownload.setChecked(true);
            }
        }

        public void onLostFocus(Widget sender) {
        }
    };
}
