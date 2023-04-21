package com.jorgealtamirano.lomalarga.client.content.management;

import java.util.Vector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.jorgealtamirano.lomalarga.client.Application;
import com.jorgealtamirano.lomalarga.client.ContentWidget;
import com.jorgealtamirano.lomalarga.client.Lomalarga;

/**
 * Example file.
 */
public class SwServiceView extends ContentWidget {

    /**
   * The constants used in this Content Widget.
   */
    public static interface CwConstants extends Constants, ContentWidget.CwConstants, Services.ServicesConstants {

        /**
     * Description of the Widget
     * @return
     */
        String cwServiceViewDescription();

        /**
     * Name of the Widget
     * @return
     */
        String cwServiceViewName();

        /**
     * Service search
     * @return
     */
        String cwServiceSearch();

        String cwServiceSearchButton();

        String cwServiceShowAllButton();

        String cwServiceName();

        String cwServiceType();

        String cwServiceHost();

        String cwServicePort();

        String cwServicePortName();

        String cwCreateNewService();

        String cwClearServiceFields();

        String cwServiceEdit();

        String cwDeleteService();

        String cwServiceNewService();

        String cwServiceEmptyName();

        String cwServiceEmptyHost();

        String cwServiceEmptyPort();
    }

    /**
   * The messages used in this Content Widget 
   * @author jorge3a
   */
    public static interface CwMessages extends Messages, ContentWidget.CwMessages {

        String cwServicesEditService(String name);

        String cwConfirmDeleteService(String name);
    }

    /**
   * URL of the JSON path, prependded to the Application.JSON_PREPATH 
   */
    public static final String JSON_SERVICES_PATH = "services.php";

    /**
   * An instance of the constants.
   */
    private CwConstants constants;

    /**
   * An instance of the Messages.
   */
    private CwMessages messages;

    /**
   * Instance of services  table
   */
    private FlexTable ftServices;

    /**
   * Search box
   */
    public SuggestBox txtSearch;

    /**
   * Button search
   */
    public Button btnSearchServices;

    /**
   * Button search all
   */
    public Button btnShowAllServices;

    /**
   * Search form panel
   */
    public FormPanel frmSearchServices;

    /**
   * Constructor.
   * 
   * @param constants the constants
   */
    public SwServiceView(CwConstants constants, CwMessages messages) {
        super(constants);
        this.constants = constants;
        this.messages = messages;
    }

    @Override
    public String getDescription() {
        return constants.cwServiceViewDescription();
    }

    @Override
    public String getName() {
        return constants.cwServiceViewName();
    }

    WServiceEdit wServiceEdit;

    /**
   * Initialize this users ContentWidget.
   */
    @Override
    public Widget onInitialize() {
        VerticalPanel vPanel = new VerticalPanel();
        HorizontalPanel hpSearch = new HorizontalPanel();
        hpSearch.setSpacing(10);
        HTML htmlSearchServices = new HTML(constants.cwServiceSearch());
        hpSearch.add(htmlSearchServices);
        frmSearchServices = new FormPanel();
        MultiWordSuggestOracle servicesOracle = new MultiWordSuggestOracle();
        txtSearch = new SuggestBox(servicesOracle);
        frmSearchServices.add(txtSearch);
        hpSearch.add(frmSearchServices);
        btnSearchServices = new Button(constants.cwServiceSearchButton());
        btnSearchServices.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                frmSearchServices.submit();
            }
        });
        hpSearch.add(btnSearchServices);
        btnShowAllServices = new Button(constants.cwServiceShowAllButton());
        btnShowAllServices.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                txtSearch.setText("");
                frmSearchServices.submit();
            }
        });
        hpSearch.add(btnShowAllServices);
        frmSearchServices.addSubmitHandler(new SubmitHandler() {

            public void onSubmit(SubmitEvent event) {
                makeTable(txtSearch.getText());
                event.cancel();
            }
        });
        vPanel.add(hpSearch);
        ftServices = new FlexTable();
        ftServices.setText(0, 0, constants.cwServiceName());
        ftServices.setText(0, 1, constants.cwServiceType());
        ftServices.setText(0, 2, constants.cwServiceHost());
        ftServices.setText(0, 3, constants.cwServicePort());
        ftServices.setText(0, 4, constants.cwServicePortName());
        ftServices.setText(0, 5, "");
        ftServices.getRowFormatter().addStyleName(0, "cw-TableUsersHeader");
        ftServices.getRowFormatter().addStyleName(0, "Application-content-title");
        ftServices.addStyleName("cw-TableUsers");
        vPanel.add(ftServices);
        makeTable("");
        WServiceNew wServiceNew = new WServiceNew(constants, messages);
        wServiceNew.setParent(this);
        addTab(wServiceNew, constants.cwServiceNewService());
        wServiceEdit = new WServiceEdit(constants, messages, this);
        return vPanel;
    }

    private void setLoading(boolean b) {
        if (b) {
            btnShowAllServices.setHTML(Application.LOADING_IMG_HTML);
            btnSearchServices.setHTML(Application.LOADING_IMG_HTML);
        } else {
            btnSearchServices.setText(constants.cwServiceSearchButton());
            btnShowAllServices.setText(constants.cwServiceShowAllButton());
        }
        btnSearchServices.setEnabled(!b);
        btnShowAllServices.setEnabled(!b);
    }

    private void makeTable(String q) {
        setLoading(true);
        ErrorCallback errorCallback = new ErrorCallback() {

            public void onError(Exception e) {
                showError(e.getMessage());
            }
        };
        try {
            ServicesTableCallback callback = new ServicesTableCallback();
            AjaxGetRequest(Application.JSON_PREPATH + JSON_SERVICES_PATH + "?q=" + q, callback, errorCallback);
        } catch (Exception e) {
            showError(e);
        }
    }

    public void showError(Exception e) {
        super.showError(e);
        setLoading(false);
    }

    private class ServicesTableCallback implements RequestCallback {

        public void onError(Request req, Throwable exception) {
            showError(new Exception(constants.JsonRetrieveError()));
        }

        public void onResponseReceived(Request req, Response res) {
            if (res.getStatusCode() == 200) {
                try {
                    JSONValue jsonValue = parseSafely(res.getText());
                    JSONArray jsonArray = jsonValue.isArray();
                    if (jsonArray != null) {
                        Vector<Services> services = new Vector<Services>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONValue jsValueA;
                            Services srv = new Services();
                            JSONObject jsSrv;
                            JSONString jsName, jsType, jsHost, jsPortName;
                            JSONNumber jsPort;
                            if ((jsSrv = jsonArray.get(i).isObject()) == null) continue;
                            if ((jsValueA = jsSrv.get("name")) == null) continue;
                            if ((jsName = jsValueA.isString()) == null) continue;
                            srv.setName(jsName.stringValue());
                            if ((jsValueA = jsSrv.get("type")) == null) continue;
                            if ((jsType = jsValueA.isString()) == null) continue;
                            srv.setType(jsType.stringValue().charAt(0));
                            if ((jsValueA = jsSrv.get("host")) == null) continue;
                            if ((jsHost = jsValueA.isString()) == null) continue;
                            srv.setHost(jsHost.stringValue());
                            if ((jsValueA = jsSrv.get("portname")) == null) continue;
                            if ((jsPortName = jsValueA.isString()) == null) continue;
                            srv.setPortName(jsPortName.stringValue());
                            try {
                                if ((jsValueA = jsSrv.get("port")) == null) continue;
                                if ((jsPort = jsValueA.isNumber()) == null) continue;
                                srv.setPort((int) jsPort.doubleValue());
                            } catch (Exception e) {
                                showError(e);
                            }
                            services.add(srv);
                        }
                        ftServices.clear();
                        for (int i = ftServices.getRowCount() - 1; i > 0; i--) {
                            ftServices.removeRow(i);
                        }
                        int i = 1;
                        for (final Services service : services) {
                            ftServices.setText(i, 0, service.getName());
                            ftServices.setText(i, 1, Services.getTypeName(service.getType(), constants));
                            ftServices.setText(i, 2, service.getHost());
                            ftServices.setText(i, 3, Integer.toString(service.getPort()));
                            ftServices.setText(i, 4, service.getPortName());
                            Button btnService = new Button(AbstractImagePrototype.create(Lomalarga.images.zoomIn()).getHTML());
                            btnService.addClickHandler(new ClickHandler() {

                                public void onClick(ClickEvent event) {
                                    wServiceEdit.setService(service);
                                    wServiceEdit.RemoveOtherTabs();
                                    addTab(wServiceEdit, messages.cwServicesEditService(service.getName()));
                                    getTabBar().selectTab(getTabBar().getTabCount() - 1);
                                }
                            });
                            ftServices.setWidget(i, 5, btnService);
                            i++;
                        }
                        setLoading(false);
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
