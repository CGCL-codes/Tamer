package com.googlecode.semrs.client.screens.patient;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.googlecode.semrs.client.MainPanel;
import com.googlecode.semrs.client.ShowcasePanel;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.HttpProxy;
import com.gwtext.client.data.JsonReader;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.RowParams;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridCellListener;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtextux.client.widgets.form.MultiSelect;

public class ListPatientsScreen extends ShowcasePanel {

    private com.gwtext.client.widgets.TabPanel tabPanel;

    public static boolean reloadFlag = false;

    FieldDef[] fieldDefs = new FieldDef[] { new StringFieldDef("id"), new StringFieldDef("name"), new StringFieldDef("lastName"), new StringFieldDef("sex"), new StringFieldDef("birthDate"), new StringFieldDef("age"), new StringFieldDef("phoneNumber"), new StringFieldDef("mobile"), new StringFieldDef("creationDate"), new StringFieldDef("lastEditDate"), new StringFieldDef("lastEditUser"), new StringFieldDef("provider"), new StringFieldDef("voided"), new StringFieldDef("lastEncounterDate"), new StringFieldDef("edit") };

    RecordDef recordDef = new RecordDef(fieldDefs);

    JsonReader reader = new JsonReader("response.value.items", recordDef);

    HttpProxy proxy = new HttpProxy("/semrs/patientServlet?patientAction=listPatients", Connection.GET);

    final Store store = new Store(proxy, reader, true);

    final PagingToolbar pagingToolbar = new PagingToolbar(store);

    public ListPatientsScreen() {
        reader.setTotalProperty("response.value.total_count");
        reader.setId("id");
    }

    protected void onActivate() {
        if (reloadFlag) {
            store.load(0, pagingToolbar.getPageSize());
            reloadFlag = false;
        }
    }

    public Panel getViewPanel() {
        if (panel == null) {
            panel = new Panel();
            MainPanel.resetTimer();
            store.setDefaultSort("id", SortDir.ASC);
            store.addStoreListener(new StoreListenerAdapter() {

                public void onLoadException(Throwable error) {
                    RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, "/semrs/index.jsp");
                    try {
                        rb.sendRequest(null, new RequestCallback() {

                            public void onError(Request request, Throwable exception) {
                                MessageBox.alert("Error", "Ha ocurrido un error al tratar de obtener la lista de pacientes.");
                            }

                            public void onResponseReceived(Request arg0, Response arg1) {
                                String errorMessage = arg1.getText();
                                if (errorMessage.indexOf("login") != -1) {
                                    MessageBox.alert("Error", "Su sesi&oacute;n de usuario ha expirado, presione OK para volver a loguearse.", new MessageBox.AlertCallback() {

                                        public void execute() {
                                            redirect("/semrs/");
                                        }
                                    });
                                } else {
                                    MessageBox.alert("Error", "Ha ocurrido un error al tratar de obtener la lista de pacientes.");
                                }
                            }
                        });
                    } catch (RequestException e) {
                        MessageBox.alert("Error", "Ha ocurrido un error al tratar de conectarse con el servidor.");
                    }
                }

                public void onDataChanged(Store store) {
                    MainPanel.resetTimer();
                }
            });
            final FormPanel formPanel = new FormPanel();
            formPanel.setFrame(true);
            formPanel.setTitle("Administrar Pacientes");
            formPanel.setWidth(900);
            formPanel.setLabelWidth(100);
            formPanel.setPaddings(5);
            formPanel.setLabelAlign(Position.TOP);
            formPanel.setIconCls("patients-icon");
            Panel topPanel = new Panel();
            topPanel.setLayout(new ColumnLayout());
            topPanel.setBorder(false);
            Panel columnOnePanel = new Panel();
            columnOnePanel.setLayout(new FormLayout());
            TextField patientId = new TextField("N&uacute;mero de C&eacute;dula", "id");
            patientId.setStyle("textTransform: uppercase;");
            patientId.addListener(new FieldListenerAdapter() {

                public void onBlur(Field field) {
                    String value = field.getValueAsString();
                    field.setValue(value.toUpperCase());
                }
            });
            columnOnePanel.add(patientId, new AnchorLayoutData("25%"));
            TextField name = new TextField("Nombres", "name");
            columnOnePanel.add(name, new AnchorLayoutData("65%"));
            topPanel.add(columnOnePanel, new ColumnLayoutData(.5));
            Panel columnTwoPanel = new Panel();
            columnTwoPanel.setLayout(new FormLayout());
            Store sexStore = new SimpleStore(new String[] { "abbr", "sex" }, new String[][] { new String[] { "M", "Masculino" }, new String[] { "F", "Femenino" } });
            sexStore.load();
            final ComboBox sexCB = new ComboBox();
            sexCB.setFieldLabel("Sexo");
            sexCB.setHiddenName("sex");
            sexCB.setStore(sexStore);
            sexCB.setDisplayField("sex");
            sexCB.setTypeAhead(true);
            sexCB.setMode(ComboBox.LOCAL);
            sexCB.setTriggerAction(ComboBox.ALL);
            sexCB.setSelectOnFocus(true);
            sexCB.setWidth(190);
            sexCB.setName("sex");
            sexCB.setReadOnly(true);
            columnTwoPanel.add(sexCB, new AnchorLayoutData("25%"));
            TextField lastName = new TextField("Apellidos", "lastName");
            columnTwoPanel.add(lastName, new AnchorLayoutData("65%"));
            topPanel.add(columnTwoPanel, new ColumnLayoutData(0.5));
            Panel columnThreePanel = new Panel();
            columnThreePanel.setLayout(new FormLayout());
            final NumberField ageFrom = new NumberField("Edad desde", "ageFrom");
            final NumberField ageTo = new NumberField("Edad hasta", "ageTo");
            com.gwtext.client.widgets.form.Validator ageValidator = new com.gwtext.client.widgets.form.Validator() {

                public boolean validate(String value) throws com.gwtext.client.widgets.form.ValidationException {
                    int ageFromValue = Integer.parseInt(ageFrom.getValueAsString());
                    int ageToValue = Integer.parseInt(ageTo.getValueAsString());
                    if ((ageFromValue > ageToValue) || (ageFrom.getValueAsString().equals("") && !ageTo.getValueAsString().equals("")) || (!ageFrom.getValueAsString().equals("") && ageTo.getValueAsString().equals(""))) {
                        throw new com.gwtext.client.widgets.form.ValidationException("La edad desde no debe ser mayor a la edad hasta");
                    } else {
                        return true;
                    }
                }
            };
            ageFrom.setValidator(ageValidator);
            ageTo.setValidator(ageValidator);
            columnThreePanel.add(ageFrom, new AnchorLayoutData("15%"));
            columnThreePanel.add(ageTo, new AnchorLayoutData("15%"));
            topPanel.add(columnThreePanel, new ColumnLayoutData(0.5));
            final DateField dobFrom = new DateField("Fecha de Nacimiento Desde", "birthdayFrom", 190);
            dobFrom.setReadOnly(true);
            final DateField dobTo = new DateField("Fecha de Nacimiento Hasta", "birthdayTo", 190);
            dobTo.setReadOnly(true);
            final DateField encounterDateFrom = new DateField("Fecha de Consulta Desde", "encounterDateFrom", 190);
            encounterDateFrom.setReadOnly(true);
            final DateField encounterDateTo = new DateField("Fecha de Consulta Hasta", "encounterDateTo", 190);
            encounterDateTo.setReadOnly(true);
            final DateField creationDateFrom = new DateField("Fecha de Ingreso Desde", "creationDateFrom", 190);
            creationDateFrom.setReadOnly(true);
            final DateField creationDateTo = new DateField("Fecha de Ingreso Hasta", "creationDateTo", 190);
            creationDateTo.setReadOnly(true);
            Panel columnFourPanel = new Panel();
            columnFourPanel.setLayout(new FormLayout());
            columnFourPanel.add(dobFrom, new AnchorLayoutData("25%"));
            columnFourPanel.add(dobTo, new AnchorLayoutData("25%"));
            topPanel.add(columnFourPanel, new ColumnLayoutData(0.5));
            Panel columnFivePanel = new Panel();
            columnFivePanel.setLayout(new FormLayout());
            columnFivePanel.add(encounterDateFrom, new AnchorLayoutData("25%"));
            columnFivePanel.add(encounterDateTo, new AnchorLayoutData("25%"));
            topPanel.add(columnFivePanel, new ColumnLayoutData(0.5));
            Panel columnSixPanel = new Panel();
            columnSixPanel.setLayout(new FormLayout());
            columnSixPanel.add(creationDateFrom, new AnchorLayoutData("25%"));
            columnSixPanel.add(creationDateTo, new AnchorLayoutData("25%"));
            topPanel.add(columnSixPanel, new ColumnLayoutData(0.5));
            FieldDef[] gridFieldDefsD = new FieldDef[] { new StringFieldDef("id"), new StringFieldDef("name"), new StringFieldDef("description"), new StringFieldDef("lastEditDate"), new StringFieldDef("lastEditUser") };
            RecordDef gridRecordDefD = new RecordDef(gridFieldDefsD);
            JsonReader gridReaderD = new JsonReader("response.value.items", gridRecordDefD);
            HttpProxy gridProxyD = new HttpProxy("", Connection.GET);
            final Store gridStoreD = new Store(gridProxyD, gridReaderD, true);
            gridStoreD.setRemoteSort(false);
            FieldDef[] gridFieldDefsD2 = new FieldDef[] { new StringFieldDef("id"), new StringFieldDef("name"), new StringFieldDef("description"), new StringFieldDef("lastEditDate"), new StringFieldDef("lastEditUser") };
            RecordDef gridRecordDefD2 = new RecordDef(gridFieldDefsD2);
            JsonReader gridReaderD2 = new JsonReader("response.value.items", gridRecordDefD2);
            HttpProxy gridProxyD2 = new HttpProxy("/semrs/diseaseServlet", Connection.GET);
            final Store innerGridStoreD = new Store(gridProxyD2, gridReaderD2, true);
            FieldDef[] gridFieldDefsM = new FieldDef[] { new StringFieldDef("id"), new StringFieldDef("name"), new StringFieldDef("description"), new StringFieldDef("lastEditDate"), new StringFieldDef("lastEditUser") };
            RecordDef gridRecordDefM = new RecordDef(gridFieldDefsM);
            JsonReader gridReaderM = new JsonReader("response.value.items", gridRecordDefM);
            HttpProxy gridProxyM = new HttpProxy("", Connection.GET);
            final Store gridStoreM = new Store(gridProxyM, gridReaderM, true);
            gridStoreM.setRemoteSort(false);
            FieldDef[] gridFieldDefsM2 = new FieldDef[] { new StringFieldDef("id"), new StringFieldDef("name"), new StringFieldDef("description"), new StringFieldDef("lastEditDate"), new StringFieldDef("lastEditUser") };
            RecordDef gridRecordDefM2 = new RecordDef(gridFieldDefsM2);
            JsonReader gridReaderM2 = new JsonReader("response.value.items", gridRecordDefM2);
            HttpProxy gridProxyM2 = new HttpProxy("/semrs/drugServlet", Connection.GET);
            final Store innerGridStoreM = new Store(gridProxyM2, gridReaderM2, true);
            final TextField providerId = new TextField("providerId", "providerId", 190);
            providerId.setVisible(false);
            Checkbox myPatientsCb = new Checkbox("Filtrar mis pacientes");
            myPatientsCb.addListener(new CheckboxListenerAdapter() {

                public void onCheck(Checkbox field, boolean checked) {
                    if (checked) {
                        providerId.setValue("myPatients");
                    } else {
                        providerId.setValue("");
                    }
                }
            });
            final TextField voided = new TextField("voided", "voided", 190);
            voided.setVisible(false);
            Checkbox voidedCb = new Checkbox("Filtrar pacientes inactivos");
            voidedCb.addListener(new CheckboxListenerAdapter() {

                public void onCheck(Checkbox field, boolean checked) {
                    if (checked) {
                        voided.setValue("false");
                    } else {
                        voided.setValue("");
                    }
                }
            });
            Panel columnSevenPanel = new Panel();
            columnSevenPanel.setLayout(new FormLayout());
            columnSevenPanel.add(myPatientsCb, new AnchorLayoutData("35%"));
            columnSevenPanel.add(getSearchGrid(gridStoreD, innerGridStoreD, createColModel(true, true), "Enfermedades", "disease-icon"), new AnchorLayoutData("85%"));
            topPanel.add(columnSevenPanel, new ColumnLayoutData(0.5));
            Panel columnEightPanel = new Panel();
            columnEightPanel.setLayout(new FormLayout());
            columnEightPanel.add(voidedCb, new AnchorLayoutData("45%"));
            columnEightPanel.add(getSearchGrid(gridStoreM, innerGridStoreM, createColModel(true, true), "Medicamentos", "drugs-icon"), new AnchorLayoutData("85%"));
            columnEightPanel.add(providerId, new AnchorLayoutData("15%"));
            columnEightPanel.add(voided, new AnchorLayoutData("15%"));
            topPanel.add(columnEightPanel, new ColumnLayoutData(0.5));
            FieldSet fieldSet = new FieldSet();
            fieldSet.add(topPanel);
            fieldSet.setTitle("B&uacute;squeda de Pacientes");
            fieldSet.setCollapsible(true);
            fieldSet.setAnimCollapse(true);
            fieldSet.setCollapsed(true);
            fieldSet.setFrame(false);
            Panel proxyPanel = new Panel();
            proxyPanel.setBorder(true);
            proxyPanel.setBodyBorder(false);
            proxyPanel.setCollapsible(false);
            proxyPanel.setLayout(new FormLayout());
            proxyPanel.setButtonAlign(Position.CENTER);
            Button clear = new Button("Limpiar");
            clear.setIconCls("clear-icon");
            clear.addListener(new ButtonListenerAdapter() {

                public void onClick(Button button, EventObject e) {
                    formPanel.getForm().reset();
                    gridStoreD.removeAll();
                    gridStoreM.removeAll();
                }
            });
            proxyPanel.addButton(clear);
            final Button search = new Button("Buscar");
            search.setIconCls("search-icon");
            search.addListener(new ButtonListenerAdapter() {

                public void onClick(Button button, EventObject e) {
                    UrlParam[] formParams = getFormData(formPanel.getForm());
                    UrlParam[] searchParams = null;
                    searchParams = new UrlParam[formParams.length + 3];
                    for (int i = 0; i <= formParams.length; i++) {
                        searchParams[i] = formParams[i];
                        if (i == formParams.length) {
                            searchParams[i + 1] = new UrlParam("diseases", getRecordValues(gridStoreD.getRecords()));
                            searchParams[i + 2] = new UrlParam("drugs", getRecordValues(gridStoreM.getRecords()));
                            break;
                        }
                    }
                    store.setBaseParams(searchParams);
                    store.load(0, pagingToolbar.getPageSize());
                    pagingToolbar.updateInfo();
                    MainPanel.resetTimer();
                }
            });
            proxyPanel.addButton(search);
            fieldSet.add(proxyPanel);
            formPanel.add(fieldSet);
            formPanel.setMonitorValid(true);
            formPanel.addListener(new FormPanelListenerAdapter() {

                public void onClientValidation(FormPanel formPanel, boolean valid) {
                    search.setDisabled(!valid);
                }
            });
            GridView view = new GridView() {

                public String getRowClass(Record record, int index, RowParams rowParams, Store store) {
                    if (record.getAsString("voided").startsWith("N")) {
                        return "redClass";
                    }
                    return "";
                }
            };
            view.setEmptyText("No hay Registros");
            view.setAutoFill(true);
            view.setForceFit(true);
            GridPanel grid = new GridPanel(store, createColModel());
            grid.setEnableDragDrop(false);
            grid.setWidth(850);
            grid.setHeight(500);
            grid.setTitle("Lista de Pacientes");
            grid.setLoadMask(true);
            grid.setSelectionModel(new RowSelectionModel());
            grid.setFrame(true);
            grid.setView(view);
            grid.setAutoExpandColumn("id");
            grid.addGridCellListener(new GridCellListener() {

                public void onCellDblClick(GridPanel grid, int rowIndex, int colIndex, EventObject e) {
                    Record r = grid.getStore().getAt(rowIndex);
                    ShowcasePanel.adminPatientScreen.setPatientId(r.getAsString("id"));
                    String voided = r.getAsString("voided");
                    if (voided.startsWith("S")) {
                        voided = "false";
                    } else {
                        voided = "true";
                    }
                    String lastEncounterDate = r.getAsString("lastEncounterDate");
                    if (lastEncounterDate == null || lastEncounterDate.trim().equals("")) {
                        lastEncounterDate = "";
                    }
                    ShowcasePanel.adminPatientScreen.flag1 = false;
                    ShowcasePanel.adminPatientScreen.setLastEncounterDate(lastEncounterDate.trim());
                    ShowcasePanel.adminPatientScreen.setVoided(voided);
                    ShowcasePanel.adminPatientScreen.setTabPanel(tabPanel);
                    showScreen(getTabPanel(), ShowcasePanel.adminPatientScreen, "Detalle de Paciente", "patient-detail-icon", "admPatientDetail");
                }

                public void onCellClick(GridPanel grid, int rowIndex, int colIndex, EventObject e) {
                }

                public void onCellContextMenu(GridPanel grid, int rowIndex, int cellIndex, EventObject e) {
                }
            });
            pagingToolbar.setPageSize(10);
            pagingToolbar.setDisplayInfo(true);
            pagingToolbar.setEmptyMsg("No hay registros");
            NumberField pageSizeField = new NumberField();
            pageSizeField.setWidth(40);
            pageSizeField.setSelectOnFocus(true);
            pageSizeField.addListener(new FieldListenerAdapter() {

                public void onSpecialKey(Field field, EventObject e) {
                    if (e.getKey() == EventObject.ENTER) {
                        int pageSize = Integer.parseInt(field.getValueAsString());
                        pagingToolbar.setPageSize(pageSize);
                    }
                }
            });
            ToolTip toolTip = new ToolTip("Introduzca el tama&ntilde;o de p&aacute;gina");
            toolTip.applyTo(pageSizeField);
            pagingToolbar.addField(pageSizeField);
            pagingToolbar.addSeparator();
            ToolbarButton newPatientButton = new ToolbarButton("Nuevo Paciente", new ButtonListenerAdapter() {

                public void onClick(Button button, EventObject e) {
                    showScreen(getTabPanel(), ShowcasePanel.addPatientScreen, "Nuevo Paciente", "patient-icon", "addPatient");
                }
            });
            newPatientButton.setIconCls("add-icon");
            pagingToolbar.addButton(newPatientButton);
            pagingToolbar.addSeparator();
            ToolbarButton exportButton = new ToolbarButton("Exportar", new ButtonListenerAdapter() {

                public void onClick(Button button, EventObject e) {
                    Window.open("/semrs/patientServlet?patientAction=exportPatients", "_self", "");
                }
            });
            exportButton.setIconCls("excel-icon");
            pagingToolbar.addButton(exportButton);
            pagingToolbar.addSeparator();
            pagingToolbar.setDisplayMsg("Mostrando Registros {0} - {1} de {2}");
            grid.setBottomToolbar(pagingToolbar);
            grid.addListener(new PanelListenerAdapter() {

                public void onRender(Component component) {
                    store.load(0, pagingToolbar.getPageSize());
                }
            });
            formPanel.add(grid, new AnchorLayoutData("100%"));
            panel.add(formPanel);
        }
        return panel;
    }

    static ColumnModel createColModel() {
        ColumnModel colModel = new ColumnModel(new ColumnConfig[] { new ColumnConfig("C&eacute;dula de Identidad", "id"), new ColumnConfig("Nombres", "name"), new ColumnConfig("Apellidos", "lastName"), new ColumnConfig("Sexo", "sex"), new ColumnConfig("Fecha de Nacimiento", "birthDate"), new ColumnConfig("Edad", "age"), new ColumnConfig("Telef&oacute;no", "phoneNumber"), new ColumnConfig("M&oacute;vil", "mobile"), new ColumnConfig("Fecha de Ingreso", "creationDate"), new ColumnConfig("Fecha Ult.Modificaci&oacute;n?", "lastEditDate"), new ColumnConfig("Usuario Ult.Modificaci&oacute;n", "lastEditUser"), new ColumnConfig("M&eacute;dico Actual", "provider"), new ColumnConfig("Activo?", "voided"), new ColumnConfig("Fecha Ult.Consulta", "lastEncounterDate") });
        for (int i = 0; i < colModel.getColumnConfigs().length; i++) {
            ((ColumnConfig) colModel.getColumnConfigs()[i]).setSortable(true);
        }
        return colModel;
    }

    public com.gwtext.client.widgets.TabPanel getTabPanel() {
        return tabPanel;
    }

    public void setTabPanel(com.gwtext.client.widgets.TabPanel tabPanel) {
        this.tabPanel = tabPanel;
    }
}
