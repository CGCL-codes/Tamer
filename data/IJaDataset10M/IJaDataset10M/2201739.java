package org.openremote.web.console.client.window;

import java.util.ArrayList;
import java.util.List;
import org.openremote.web.console.client.Constants;
import org.openremote.web.console.client.event.SubmitEvent;
import org.openremote.web.console.client.gxtextends.StringModelData;
import org.openremote.web.console.client.icon.Icons;
import org.openremote.web.console.client.listener.FormSubmitListener;
import org.openremote.web.console.client.listener.SubmitListener;
import org.openremote.web.console.client.rpc.AsyncServiceFactory;
import org.openremote.web.console.client.rpc.AsyncSuccessCallback;
import org.openremote.web.console.client.utils.ClientDataBase;
import org.openremote.web.console.domain.AppSetting;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Manage the application's configuration.
 * Include auto discovery, manage custom servers, select panel identity and configure security. 
 */
public class SettingsWindow extends FormWindow {

    private Icons icons = GWT.create(Icons.class);

    private ToggleButton autoButton;

    private ContentPanel customServerContainer;

    private ContentPanel autoServerContainer;

    private ComboBox<ModelData> panelListCombo;

    private boolean requestOnePanel;

    private String selectedPanel = "";

    public SettingsWindow() {
        super();
        setStyle();
        addAutoField();
        createCustomServerGrid();
        addPanelIdentityField();
        addSecurityFields();
        addButtons();
        addListener();
        show();
    }

    private void setStyle() {
        setHeading("Settings");
        setSize(400, 450);
        form.setLabelWidth(100);
        form.setFieldWidth(200);
    }

    /**
    * Create Auto discovery toggle button and add it into form.
    * 
    * Override toggle button's toggle method, if state is true, make the text "ON" and show auto discovery server grid,
    * otherwise make the text "OFF" and show custom server grid.
    */
    private void addAutoField() {
        autoButton = new ToggleButton("OFF") {

            @Override
            protected void toggle(boolean state, boolean silent) {
                super.toggle(state, silent);
                if (rendered) {
                    if (state) {
                        this.setText("ON");
                        if (customServerContainer != null) {
                            customServerContainer.hide();
                        }
                        if (autoServerContainer != null) {
                            autoServerContainer.show();
                        } else {
                            createAutoServerGrid();
                        }
                        ClientDataBase.appSetting.setAutoDiscovery(true);
                    } else {
                        this.setText("OFF");
                        if (customServerContainer != null) {
                            customServerContainer.show();
                        }
                        if (autoServerContainer != null) {
                            autoServerContainer.hide();
                        }
                        ClientDataBase.appSetting.setAutoDiscovery(false);
                    }
                }
            }
        };
        autoButton.setWidth(200);
        autoButton.toggle(ClientDataBase.appSetting.isAutoDiscovery());
        AdapterField autoField = new AdapterField(autoButton);
        autoField.setFieldLabel("Auto Discovery");
        form.add(autoField);
    }

    /**
    * Creates the custom server grid and initial servers from AppSetting object.
    * 
    * Add "Add server" and "Delete" buttons before the custom server grid. "Add sever" button can add a custom server into the grid,
    * and the "Delete" button can delete a custom server from the grid.
    */
    private void createCustomServerGrid() {
        List<ColumnConfig> customServerConfigs = new ArrayList<ColumnConfig>();
        ColumnConfig serverColumn = new ColumnConfig("customServer", "Server", 338);
        serverColumn.setSortable(false);
        customServerConfigs.add(serverColumn);
        ListStore<StringModelData> customListStore = new ListStore<StringModelData>();
        AppSetting appSetting = ClientDataBase.appSetting;
        int tempserverIndex = -1;
        if (!"".equals(appSetting.getCurrentServer()) && appSetting.getCustomServers() != null) {
            String currentServer = appSetting.getCurrentServer();
            List<String> customServers = appSetting.getCustomServers();
            for (int i = 0; i < customServers.size(); i++) {
                customListStore.add(new StringModelData("customServer", customServers.get(i)));
                if (currentServer.equals(customServers.get(i))) {
                    tempserverIndex = i;
                }
            }
        }
        final int currentServerIndex = tempserverIndex;
        final Grid<StringModelData> customServerGrid = new Grid<StringModelData>(customListStore, new ColumnModel(customServerConfigs)) {

            @Override
            protected void afterRenderView() {
                super.afterRenderView();
                if (currentServerIndex != -1) {
                    this.getSelectionModel().select(currentServerIndex, false);
                }
            }
        };
        customServerGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        customServerGrid.setHideHeaders(true);
        customServerGrid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<StringModelData>() {

            public void selectionChanged(SelectionChangedEvent<StringModelData> se) {
                if (se.getSelectedItem() != null) {
                    ClientDataBase.appSetting.setCurrentServer(se.getSelectedItem().getValue());
                }
            }
        });
        customServerContainer = new ContentPanel() {

            @Override
            public void show() {
                super.show();
                if (customServerGrid.getSelectionModel().getSelectedItem() != null) {
                    ClientDataBase.appSetting.setCurrentServer(customServerGrid.getSelectionModel().getSelectedItem().getValue());
                    requestOnePanelIdentity();
                } else {
                    ClientDataBase.appSetting.setCurrentServer("");
                    panelListCombo.setValue(null);
                }
            }
        };
        customServerContainer.setHeaderVisible(false);
        customServerContainer.setBodyBorder(false);
        customServerContainer.setLayout(new FitLayout());
        customServerContainer.setStyleAttribute("marginTop", "5px");
        customServerContainer.setStyleAttribute("marginBottom", "5px");
        customServerContainer.setSize(360, 150);
        customServerContainer.setBorders(true);
        ToolBar toolBar = new ToolBar();
        Button addButton = new Button("Add server", icons.add());
        addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            public void componentSelected(ButtonEvent ce) {
                AddServerWindow addServerWindow = new AddServerWindow();
                addServerWindow.addListener(SubmitEvent.SUBMIT, new SubmitListener() {

                    @Override
                    public void afterSubmit(SubmitEvent be) {
                        if (be.getData() != null) {
                            String newCustomServer = be.getData().toString();
                            StringModelData newData = new StringModelData("customServer", newCustomServer);
                            customServerGrid.getStore().add(newData);
                            customServerGrid.getSelectionModel().select(newData, false);
                            ClientDataBase.appSetting.addCustomServer(newCustomServer);
                            ClientDataBase.appSetting.setCurrentServer(newCustomServer);
                            requestOnePanelIdentity();
                        }
                    }
                });
            }
        });
        toolBar.add(addButton);
        toolBar.add(new SeparatorToolItem());
        Button deleteButton = new Button("Delete", icons.delete());
        deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                StringModelData selectedModel = customServerGrid.getSelectionModel().getSelectedItem();
                if (selectedModel != null) {
                    customServerGrid.getStore().remove(selectedModel);
                    ClientDataBase.appSetting.removeCustomServer(selectedModel.getValue());
                    ClientDataBase.appSetting.setCurrentServer("");
                } else {
                    MessageBox.alert("Warn", "Please select a custom server url.", null);
                }
            }
        });
        toolBar.add(deleteButton);
        customServerContainer.setTopComponent(toolBar);
        customServerContainer.add(customServerGrid);
        form.add(customServerContainer);
    }

    /**
    * Creates grid contains auto discovery servers.
    */
    private void createAutoServerGrid() {
        List<ColumnConfig> autoServerConfigs = new ArrayList<ColumnConfig>();
        ColumnConfig serverColumn = new ColumnConfig("autoServer", "Auto Servers", 338);
        serverColumn.setSortable(false);
        autoServerConfigs.add(serverColumn);
        final ListStore<StringModelData> autoServersStore = new ListStore<StringModelData>();
        final Grid<StringModelData> autoServerGrid = new Grid<StringModelData>(autoServersStore, new ColumnModel(autoServerConfigs)) {

            @Override
            protected void afterRenderView() {
                super.afterRenderView();
                this.getSelectionModel().select(0, false);
            }
        };
        autoServerGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        autoServerGrid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<StringModelData>() {

            public void selectionChanged(SelectionChangedEvent<StringModelData> se) {
                if (se.getSelectedItem() != null) {
                    ClientDataBase.appSetting.setCurrentServer(se.getSelectedItem().getValue());
                    requestOnePanelIdentity();
                }
            }
        });
        autoServerContainer = new ContentPanel() {

            @Override
            public void show() {
                super.show();
                this.mask("Loading auto discovery servers...");
                autoButton.disable();
                AsyncServiceFactory.getIPAutoDiscoveryServiceAsync().getAutoDiscoveryServers(new AsyncCallback<List<String>>() {

                    public void onFailure(Throwable caught) {
                        autoServerContainer.unmask();
                        autoButton.enable();
                        autoServersStore.removeAll();
                    }

                    public void onSuccess(List<String> autoServers) {
                        autoServerContainer.unmask();
                        autoButton.enable();
                        autoServersStore.removeAll();
                        String currentServer = ClientDataBase.appSetting.getCurrentServer();
                        int currentServerIndex = 0;
                        for (int i = 0; i < autoServers.size(); i++) {
                            autoServersStore.add(new StringModelData("autoServer", autoServers.get(i)));
                            if (autoServers.get(i).equals(currentServer)) {
                                currentServerIndex = i;
                            }
                        }
                        autoServerGrid.getSelectionModel().select(currentServerIndex, false);
                        ClientDataBase.appSetting.setCurrentServer(autoServers.get(currentServerIndex));
                        requestOnePanelIdentity();
                    }
                });
            }
        };
        autoServerContainer.setHeaderVisible(false);
        autoServerContainer.setBodyBorder(false);
        autoServerContainer.setLayout(new FitLayout());
        autoServerContainer.setStyleAttribute("marginTop", "5px");
        autoServerContainer.setStyleAttribute("marginBottom", "5px");
        autoServerContainer.setSize(360, 150);
        autoServerContainer.setBorders(true);
        autoServerContainer.show();
        autoServerContainer.add(autoServerGrid);
        form.insert(autoServerContainer, 1);
        layout();
    }

    /**
    * Adds the panel identity comboBox field, for select panel identity.
    */
    private void addPanelIdentityField() {
        final ListStore<ModelData> store = new ListStore<ModelData>();
        panelListCombo = new ComboBox<ModelData>();
        panelListCombo.setStore(store);
        panelListCombo.setFieldLabel("Panel Identity");
        panelListCombo.setDisplayField("name");
        panelListCombo.setValueField("name");
        panelListCombo.setTriggerAction(TriggerAction.ALL);
        panelListCombo.setEditable(false);
        panelListCombo.setAllowBlank(false);
        panelListCombo.setEmptyText("Select your panel...");
        String currentPanel = ClientDataBase.appSetting.getCurrentPanelIdentity();
        if (!"".equals(currentPanel)) {
            StringModelData value = new StringModelData("name", currentPanel);
            store.add(value);
            panelListCombo.setValue(value);
        }
        panelListCombo.addListener(Events.TriggerClick, new Listener<FieldEvent>() {

            public void handleEvent(FieldEvent be) {
                String currentServer = ClientDataBase.getSecuredServer();
                store.removeAll();
                panelListCombo.setValue(null);
                if (!"".equals(currentServer)) {
                    mask("Loading panel identities...");
                    AsyncServiceFactory.getPanelIdentityServiceAsync().getPanelNames(currentServer, ClientDataBase.userInfo.getUsername(), ClientDataBase.userInfo.getPassword(), new AsyncSuccessCallback<List<String>>() {

                        public void onSuccess(List<String> panels) {
                            unmask();
                            if (panels != null) {
                                if (requestOnePanel) {
                                    if (panels.size() == 1) {
                                        StringModelData value = new StringModelData("name", panels.get(0));
                                        store.add(value);
                                        panelListCombo.setValue(value);
                                    }
                                } else {
                                    for (String panel : panels) {
                                        store.add(new StringModelData("name", panel));
                                    }
                                    panelListCombo.expand();
                                }
                            }
                            requestOnePanel = false;
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            super.onFailure(caught);
                            unmask();
                            requestOnePanel = false;
                        }
                    });
                } else {
                    if (!requestOnePanel) {
                        MessageBox.alert("WARN", "Please select a controller server.", null);
                    }
                }
            }
        });
        panelListCombo.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                if (se.getSelectedItem() != null) {
                    selectedPanel = se.getSelectedItem().get("name").toString();
                }
            }
        });
        form.add(panelListCombo);
    }

    /**
    * Adds the security fields.
    * 
    * It contains a toggle button to open or close the security, a text field to input ssl port.
    */
    private void addSecurityFields() {
        FieldSet securityField = new FieldSet();
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(85);
        layout.setDefaultWidth(200);
        securityField.setLayout(layout);
        securityField.setHeading("Security");
        final TextField<String> sslPortField = new TextField<String>();
        sslPortField.setFieldLabel("Port");
        sslPortField.setValue(Cookies.getCookie(Constants.SSL_PORT));
        sslPortField.setRegex(Constants.REG_POSITIVEINT);
        sslPortField.getMessages().setRegexText("The port must be a integer");
        sslPortField.addListener(Events.Blur, new Listener<BaseEvent>() {

            public void handleEvent(BaseEvent be) {
                String port = sslPortField.getValue();
                if (port != null && !"".equals(port)) {
                    Cookies.setCookie(Constants.SSL_PORT, port);
                }
            }
        });
        ToggleButton sslButton = new ToggleButton("OFF") {

            @Override
            protected void toggle(boolean state, boolean silent) {
                super.toggle(state, silent);
                if (rendered) {
                    if (state) {
                        this.setText("ON");
                        Cookies.setCookie(Constants.SSL_STATUS, "true");
                        sslPortField.show();
                    } else {
                        this.setText("OFF");
                        Cookies.setCookie(Constants.SSL_STATUS, Constants.SSL_DISABLED);
                        sslPortField.hide();
                    }
                }
            }
        };
        sslButton.setWidth(200);
        String sslStatus = Cookies.getCookie(Constants.SSL_STATUS);
        if (sslStatus.equals(Constants.SSL_DISABLED)) {
            sslButton.toggle(false);
            sslPortField.hide();
        } else {
            sslButton.toggle(true);
        }
        AdapterField sslField = new AdapterField(sslButton);
        sslField.setFieldLabel("SSL");
        securityField.add(sslField);
        securityField.add(sslPortField);
        form.add(securityField);
    }

    /**
    * Adds "OK"/"Cancel" buttons to hide the window.
    * 
    * If click "OK" button, save the settings into Cookies.
    */
    private void addButtons() {
        Button okButton = new Button("OK");
        okButton.addSelectionListener(new FormSubmitListener(form) {

            public void componentSelected(ButtonEvent ce) {
                if (!"".equals(selectedPanel)) {
                    ClientDataBase.appSetting.setCurrentPanelIdentity(selectedPanel);
                }
                super.componentSelected(ce);
            }
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });
        form.addButton(okButton);
        form.addButton(cancelButton);
    }

    private void addListener() {
        form.addListener(Events.BeforeSubmit, new Listener<FormEvent>() {

            public void handleEvent(FormEvent be) {
                Cookies.setCookie(Constants.CONSOLE_SETTINGS, ClientDataBase.appSetting.toJson());
                fireEvent(SubmitEvent.SUBMIT, new SubmitEvent());
                hide();
            }
        });
    }

    private void requestOnePanelIdentity() {
        requestOnePanel = true;
        panelListCombo.fireEvent(Events.TriggerClick, new FieldEvent(panelListCombo));
    }
}
