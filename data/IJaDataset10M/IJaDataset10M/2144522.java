package com.jorgealtamirano.lomalarga.client.content.management;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.jorgealtamirano.lomalarga.client.Application;
import com.jorgealtamirano.lomalarga.client.ContentWidget;
import com.jorgealtamirano.lomalarga.client.ContentWidget.ErrorCallback;
import com.jorgealtamirano.lomalarga.client.content.management.SwServiceView.CwConstants;
import com.jorgealtamirano.lomalarga.client.content.management.SwServiceView.CwMessages;

/**
 * @author jorge3a
 *
 */
public class WServiceNew extends VerticalPanel {

    /**
   * URL of the JSON path, prependded to the Application.JSON_PREPATH 
   */
    public static final String JSON_NEWSERVICE_PATH = "newservice.php";

    private final CwConstants constants;

    private final CwMessages messages;

    private SwServiceView parent;

    private final FormPanel frmNewService;

    private final TextBox txtServiceName;

    private final Button btnSubmit;

    public WServiceNew(CwConstants constants, CwMessages messages) {
        super();
        this.constants = constants;
        this.messages = messages;
        FlexTable ftNewService = new FlexTable();
        final Label lblServiceName = new Label(this.constants.cwServiceName());
        txtServiceName = new TextBox();
        btnSubmit = new Button(this.constants.cwCreateNewService());
        final Button btnClear = new Button(this.constants.cwClearServiceFields());
        txtServiceName.setTabIndex(1);
        btnSubmit.setTabIndex(4);
        btnClear.setTabIndex(5);
        ftNewService.setWidget(0, 0, lblServiceName);
        ftNewService.setWidget(3, 0, btnSubmit);
        ftNewService.setWidget(0, 1, txtServiceName);
        ftNewService.setWidget(3, 1, btnClear);
        frmNewService = new FormPanel();
        frmNewService.add(ftNewService);
        btnSubmit.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                frmNewService.submit();
            }
        });
        btnClear.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                frmNewService.reset();
            }
        });
        frmNewService.addSubmitHandler(new SubmitHandler() {

            public void onSubmit(SubmitEvent event) {
                if (txtServiceName.getText().isEmpty()) {
                    showError(WServiceNew.this.constants.cwServiceEmptyName());
                    txtServiceName.setFocus(true);
                } else {
                    Services service = new Services(txtServiceName.getText());
                    service.setType(Services.LOCAL);
                    PushChanges(service);
                }
                event.cancel();
            }
        });
        add(frmNewService);
    }

    private void setLoading(boolean b) {
        if (b) {
            btnSubmit.setHTML(Application.LOADING_IMG_HTML);
        } else {
            btnSubmit.setText(this.constants.cwCreateNewService());
        }
        btnSubmit.setEnabled(!b);
    }

    public void showError(Exception e) {
        showError(e.getMessage());
    }

    public void showError(String errorMessage) {
        setLoading(false);
        parent.showError(errorMessage);
    }

    private void PushChanges(Services service) {
        setLoading(true);
        ErrorCallback errorCallback = new ErrorCallback() {

            public void onError(Exception e) {
                showError(e);
            }
        };
        try {
            NewServiceCallback callback = new NewServiceCallback();
            callback.parent = this.parent;
            callback.service = service;
            String data = "name=" + service.getName();
            parent.AjaxPostRequest(Application.JSON_PREPATH + JSON_NEWSERVICE_PATH, data, callback, errorCallback);
        } catch (Exception e) {
            showError(e);
        }
    }

    public void setParent(SwServiceView parent) {
        this.parent = parent;
    }

    private class NewServiceCallback implements RequestCallback {

        private SwServiceView parent;

        private Services service;

        public void onError(Request req, Throwable exception) {
            showError(new Exception(constants.JsonRetrieveError()));
        }

        public void onResponseReceived(Request req, Response res) {
            if (res.getStatusCode() == 200) {
                try {
                    JSONValue jsonValue = ContentWidget.parseSafely(res.getText());
                    JSONArray jsonArray = jsonValue.isArray();
                    int affectedRows = 0;
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONValue jsValueA;
                            JSONObject jsAffected;
                            JSONNumber jsAffectedRows;
                            if ((jsAffected = jsonArray.get(i).isObject()) == null) continue;
                            try {
                                if ((jsValueA = jsAffected.get("Affected")) == null) continue;
                                if ((jsAffectedRows = jsValueA.isNumber()) == null) continue;
                                affectedRows += (int) jsAffectedRows.doubleValue();
                            } catch (Exception e) {
                                showError(e);
                            }
                        }
                        if (affectedRows == 1) {
                            parent.wServiceEdit.setParent(parent);
                            parent.wServiceEdit.setService(service);
                            parent.wServiceEdit.RemoveOtherTabs();
                            parent.addTab(parent.wServiceEdit, messages.cwServicesEditService(service.getName()));
                            parent.getTabBar().selectTab(parent.getTabBar().getTabCount() - 1);
                            frmNewService.reset();
                            setLoading(false);
                        } else {
                            showError(constants.cwErrorNumRecords());
                        }
                    } else {
                        throw new JSONException();
                    }
                } catch (JSONException e) {
                    showError(new Exception(constants.JsonParseError()));
                }
            } else {
                if (res.getStatusCode() == 404) {
                    Window.Location.assign(Application.LOGIN_PAGE);
                } else {
                    showError(new Exception(constants.JsonRetrieveError() + " " + res.getStatusCode()));
                }
            }
        }
    }
}
