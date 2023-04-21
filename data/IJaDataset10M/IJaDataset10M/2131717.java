package com.googlecode.semrs.client.screens.admin;

import java.util.Date;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.semrs.client.ExtendedMessageBox;
import com.googlecode.semrs.client.MainPanel;
import com.googlecode.semrs.client.ShowcasePanel;
import com.googlecode.semrs.client.screens.patient.ListPatientsScreen;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.GenericConfig;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.RegionPosition;
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
import com.gwtext.client.widgets.DefaultsHandler;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.WaitConfig;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;
import com.gwtextux.client.widgets.form.ItemSelector;
import com.gwtextux.client.widgets.image.Image;
import com.gwtextux.client.widgets.upload.UploadDialog;
import com.gwtextux.client.widgets.upload.UploadDialogListenerAdapter;

public class AddEditUserScreen extends ShowcasePanel {

    private String userId;

    FormPanel formPanel = null;

    FormPanel formPanelItems = null;

    boolean flag1 = false;

    protected void onActivate() {
        if (!flag1) {
            getViewPanel();
        }
        flag1 = false;
    }

    public Panel getViewPanel() {
        flag1 = true;
        if (formPanel != null) {
            formPanel.clear();
        }
        if (formPanelItems != null) {
            formPanelItems.clear();
        }
        panel = new Panel();
        MainPanel.resetTimer();
        formPanel = new FormPanel();
        formPanel.setId("userForm");
        formPanel.setTitle("Agregar/Editar Usuario");
        formPanel.setFrame(true);
        formPanel.setPaddings(5, 0, 5, 5);
        formPanel.setWidth(958);
        formPanel.setLabelWidth(100);
        formPanel.setIconCls("user-add-icon");
        Panel proxyPanel = new Panel();
        proxyPanel.setBorder(true);
        proxyPanel.setBodyBorder(false);
        proxyPanel.setCollapsible(false);
        proxyPanel.setLayout(new FormLayout());
        proxyPanel.setButtonAlign(Position.CENTER);
        final TextField usernameText = new TextField("Usuario", "username", 190);
        usernameText.setAllowBlank(false);
        if (!userId.equals("")) {
            usernameText.setReadOnly(true);
        }
        FieldSet userFS = new FieldSet("Informaci&oacute;n de Usuario");
        userFS.setFrame(false);
        userFS.setAutoWidth(true);
        userFS.setAutoHeight(true);
        Panel proxy = new Panel();
        proxy.setBorder(true);
        proxy.setBodyBorder(false);
        proxy.setCollapsible(false);
        final String imgSrc = "/semrs/imageServlet?type=user&id=" + userId;
        final Image userImage = new Image("", imgSrc + "&op=load&date=" + new Date().getSeconds());
        HTML newImage = new HTML("<a href='javascript:;'>Nueva Imagen</a>");
        newImage.addClickListener(new ClickListener() {

            public void onClick(Widget widget) {
                UploadDialog dialog = new UploadDialog();
                dialog.setUrl(imgSrc + "&op=upload");
                dialog.setPermittedExtensions(new String[] { "jpg", "gif", "png", "jpeg" });
                dialog.addListener(new UploadDialogListenerAdapter() {

                    public boolean onBeforeAdd(UploadDialog source, String filename) {
                        if (source.getQueuedCount() > 0) {
                            return false;
                        } else {
                            return true;
                        }
                    }

                    public void onUploadComplete(UploadDialog source) {
                        userImage.setSrc(imgSrc + "&op=load&date=" + new Date().getSeconds());
                        MainPanel.resetTimer();
                    }
                });
                dialog.show();
            }
        });
        if (!userId.equals("")) {
            proxy.add(userImage);
            proxy.add(newImage);
        }
        userFS.add(proxy);
        userFS.add(usernameText, new BorderLayoutData(RegionPosition.CENTER));
        final TextField email = new TextField("Email", "email", 190);
        email.setVtype(VType.EMAIL);
        email.setAllowBlank(false);
        userFS.add(email);
        Store activeStore = new SimpleStore(new String[] { "id", "enabled" }, new String[][] { new String[] { "Si", "Si" }, new String[] { "No", "No" } });
        activeStore.load();
        final ComboBox enabledCB = new ComboBox();
        enabledCB.setFieldLabel("Activo?");
        enabledCB.setHiddenName("enabled");
        enabledCB.setStore(activeStore);
        enabledCB.setDisplayField("enabled");
        enabledCB.setTypeAhead(true);
        enabledCB.setMode(ComboBox.LOCAL);
        enabledCB.setTriggerAction(ComboBox.ALL);
        enabledCB.setSelectOnFocus(true);
        enabledCB.setWidth(85);
        enabledCB.setEmptyText("Si");
        enabledCB.setName("enabled");
        enabledCB.setReadOnly(true);
        userFS.add(enabledCB);
        final TextField password = new TextField("Password", "password", 190);
        final TextField passwordRetype = new TextField("Confirmar", "passwordRetype", 190);
        password.setPassword(true);
        passwordRetype.setPassword(true);
        FieldSet passwordFS = new FieldSet();
        passwordFS.setCheckboxToggle(true);
        passwordFS.setFrame(false);
        passwordFS.setTitle("Nuevo Password?");
        passwordFS.setCollapsed(true);
        passwordFS.add(password);
        passwordFS.add(passwordRetype);
        passwordFS.setAutoWidth(true);
        passwordFS.setAutoHeight(true);
        com.gwtext.client.widgets.form.Validator validatePasswordRetype = new com.gwtext.client.widgets.form.Validator() {

            public boolean validate(String value) throws com.gwtext.client.widgets.form.ValidationException {
                String passwordText = password.getText();
                if (!value.equals(passwordText)) {
                    throw new com.gwtext.client.widgets.form.ValidationException("Los passwords no son iguales");
                } else if (value.length() < 6) {
                    throw new com.gwtext.client.widgets.form.ValidationException("El password debe contener almenos 6 caracteres.");
                } else {
                    return true;
                }
            }
        };
        passwordRetype.setValidator(validatePasswordRetype);
        password.setValidator(new com.gwtext.client.widgets.form.Validator() {

            public boolean validate(String value) throws com.gwtext.client.widgets.form.ValidationException {
                if (value.length() < 6) {
                    throw new com.gwtext.client.widgets.form.ValidationException("El password debe contener almenos 6 caracteres.");
                } else {
                    return true;
                }
            }
        });
        if (!userId.equals("")) {
            password.setAllowBlank(true);
            passwordRetype.setAllowBlank(true);
            userFS.add(passwordFS);
        } else {
            password.setAllowBlank(false);
            passwordRetype.setAllowBlank(false);
            userFS.add(password);
            userFS.add(passwordRetype);
        }
        FieldSet personalFS = new FieldSet("Informaci&oacute;n Personal");
        personalFS.setAutoWidth(true);
        personalFS.setAutoHeight(true);
        personalFS.setFrame(false);
        final TextField nameText = new TextField("Nombre", "name", 190);
        nameText.setAllowBlank(false);
        personalFS.add(nameText);
        final TextField lastNameText = new TextField("Apellido", "lastName", 190);
        lastNameText.setAllowBlank(false);
        personalFS.add(lastNameText);
        final DateField birthDate = new DateField("F. Nacimiento", "birthDate", 190);
        birthDate.setAllowBlank(true);
        birthDate.setMaxValue(new Date());
        birthDate.setReadOnly(true);
        personalFS.add(birthDate);
        Store store = new SimpleStore(new String[] { "abbr", "sex" }, new String[][] { new String[] { "M", "Masculino" }, new String[] { "F", "Femenino" } });
        store.load();
        final ComboBox sexCB = new ComboBox();
        sexCB.setFieldLabel("Sexo");
        sexCB.setHiddenName("sex");
        sexCB.setStore(store);
        sexCB.setDisplayField("sex");
        sexCB.setTypeAhead(true);
        sexCB.setMode(ComboBox.LOCAL);
        sexCB.setTriggerAction(ComboBox.ALL);
        sexCB.setSelectOnFocus(true);
        sexCB.setWidth(190);
        sexCB.setEmptyText("N.D");
        sexCB.setName("sex");
        sexCB.setReadOnly(true);
        personalFS.add(sexCB);
        FieldSet contactFS = new FieldSet("Informaci&oacute;n de Contacto");
        contactFS.setAutoWidth(true);
        contactFS.setAutoHeight(true);
        contactFS.setFrame(false);
        final TextField phone = new TextField("Telef&oacute;no", "phoneNumber", 190);
        phone.setVtype(VType.ALPHANUM);
        contactFS.add(phone);
        final TextField mobile = new TextField("M&oacute;vil", "mobile", 190);
        mobile.setVtype(VType.ALPHANUM);
        contactFS.add(mobile);
        final TextArea addressTextArea = new TextArea("Direcci&oacute;n", "address");
        addressTextArea.setHideLabel(false);
        addressTextArea.setWidth(190);
        addressTextArea.setHeight(80);
        contactFS.add(addressTextArea);
        final ItemSelector itemSelector = new ItemSelector();
        final String saveURL = "/semrs/userServlet?userEdit=submit&isNew=" + userId + "&";
        final Button saveButton = new Button("Guardar");
        saveButton.setId("saveUserButton");
        saveButton.addListener(new ButtonListenerAdapter() {

            public void onClick(final Button button, EventObject e) {
                MessageBox.show(new MessageBoxConfig() {

                    {
                        setMsg("Guardando los cambios, por favor espere...");
                        setProgressText("Guardando...");
                        setWidth(300);
                        setWait(true);
                        setWaitConfig(new WaitConfig() {

                            {
                                setInterval(200);
                            }
                        });
                        setAnimEl(button.getId());
                    }
                });
                RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, saveURL + formPanel.getForm().getValues());
                try {
                    rb.sendRequest(null, new RequestCallback() {

                        public void onResponseReceived(Request req, final Response res) {
                            MessageBox.hide();
                            MessageBox.getDialog().close();
                            if (res.getText().indexOf("errores") != -1) {
                                MessageBox.show(new MessageBoxConfig() {

                                    {
                                        setTitle("Error");
                                        setMsg(res.getText());
                                        setIconCls(MessageBox.ERROR);
                                        setModal(true);
                                        setButtons(MessageBox.OK);
                                    }
                                });
                                if (usernameText.getValueAsString().equals("")) {
                                    usernameText.markInvalid("Este campo es obligatorio");
                                }
                                if (email.getValueAsString().equals("")) {
                                    email.markInvalid("Este campo es obligatorio");
                                }
                                if (nameText.getValueAsString().equals("")) {
                                    nameText.markInvalid("Este campo es obligatorio");
                                }
                                if (lastNameText.getValueAsString().equals("")) {
                                    lastNameText.markInvalid("Este campo es obligatorio");
                                }
                                if (password.getValueAsString().equals("")) {
                                    password.markInvalid("Este campo es obligatorio");
                                }
                                if (passwordRetype.getValueAsString().equals("")) {
                                    passwordRetype.markInvalid("Este campo es obligatorio");
                                }
                                if (res.getText().indexOf("Usuario") != -1) {
                                    usernameText.markInvalid("Este usuario ya existe");
                                }
                                if (res.getText().indexOf("Email suministrado") != -1) {
                                    email.markInvalid("El email suministrado ya existe en el sistema");
                                }
                                if (res.getText().indexOf("password") != -1) {
                                    password.markInvalid("Por favor introduzca un password valido");
                                }
                            } else if (res.getText().equals("")) {
                                MessageBox.hide();
                                MessageBox.alert("Error", "Error interno");
                            } else {
                                MainPanel.resetTimer();
                                AdminUsersScreen.reloadFlag = true;
                                setUserId(usernameText.getValueAsString());
                                MessageBox.alert("Operaci&oacute;n Ex&iacute;tosa", res.getText(), new MessageBox.AlertCallback() {

                                    public void execute() {
                                        MainPanel.centerPanel.remove(MainPanel.centerPanel.getActiveTab());
                                    }
                                });
                            }
                        }

                        public void onError(Request req, Throwable exception) {
                            MessageBox.hide();
                            MessageBox.getDialog().close();
                            MessageBox.alert("Error", "Error interno");
                        }
                    });
                } catch (RequestException re) {
                    MessageBox.hide();
                    MessageBox.getDialog().close();
                    MessageBox.alert("Error", "Error interno");
                }
            }
        });
        saveButton.setIconCls("save-icon");
        proxyPanel.addButton(saveButton);
        final RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, "/semrs/userServlet?userEdit=delete&username=" + userId);
        final Button delete = new Button("Eliminar");
        delete.setId("deleteUserButton");
        delete.addListener(new ButtonListenerAdapter() {

            public void onClick(final Button button, EventObject e) {
                ExtendedMessageBox.confirmlg("Confirmar", "Esta seguro que desea eliminar este usuario?", "Si", "No", new MessageBox.ConfirmCallback() {

                    public void execute(String btnID) {
                        if (btnID.equals("yes")) {
                            MessageBox.show(new MessageBoxConfig() {

                                {
                                    setMsg("Eliminando registro, por favor espere...");
                                    setProgressText("Eliminando...");
                                    setWidth(300);
                                    setWait(true);
                                    setWaitConfig(new WaitConfig() {

                                        {
                                            setInterval(200);
                                        }
                                    });
                                    setAnimEl(button.getId());
                                }
                            });
                            try {
                                rb.sendRequest(null, new RequestCallback() {

                                    public void onResponseReceived(Request req, final Response res) {
                                        MessageBox.hide();
                                        MessageBox.getDialog().close();
                                        if (res.getText().indexOf("errores") != -1) {
                                            MessageBox.show(new MessageBoxConfig() {

                                                {
                                                    setTitle("Error");
                                                    setMsg(res.getText());
                                                    setIconCls(MessageBox.ERROR);
                                                    setModal(true);
                                                    setButtons(MessageBox.OK);
                                                }
                                            });
                                        } else if (res.getText().equals("")) {
                                            MessageBox.hide();
                                            MessageBox.alert("Error", "Error interno");
                                        } else {
                                            MainPanel.resetTimer();
                                            AdminUsersScreen.reloadFlag = true;
                                            setUserId("");
                                            MessageBox.alert("Operaci&oacute;n Ex&iacute;tosa", res.getText(), new MessageBox.AlertCallback() {

                                                public void execute() {
                                                    MainPanel.centerPanel.remove(MainPanel.centerPanel.getActiveTab());
                                                }
                                            });
                                        }
                                    }

                                    public void onError(Request req, Throwable exception) {
                                        MessageBox.hide();
                                        MessageBox.getDialog().close();
                                        MessageBox.alert("Error", "Error interno");
                                    }
                                });
                            } catch (RequestException re) {
                                MessageBox.hide();
                                MessageBox.getDialog().close();
                                MessageBox.alert("Error", "Error interno");
                            }
                        }
                    }
                });
            }
        });
        delete.setIconCls("delete-icon");
        delete.setDisabled(false);
        if (userId.equals("")) {
            delete.setDisabled(true);
        }
        proxyPanel.addButton(delete);
        Button cancel = new Button("Cancelar");
        cancel.setId("cancelUserButton");
        cancel.addListener(new ButtonListenerAdapter() {

            public void onClick(Button button, EventObject e) {
                MainPanel.centerPanel.remove(MainPanel.centerPanel.getActiveTab());
            }
        });
        cancel.setIconCls("cancel-icon");
        proxyPanel.addButton(cancel);
        RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("id"), new StringFieldDef("desc") });
        final JsonReader reader = new JsonReader("data", recordDef);
        reader.setSuccessProperty("success");
        reader.setId("id");
        HttpProxy availableRoles = new HttpProxy("/semrs/userServlet?userEdit=getRoles&username=" + userId, Connection.GET);
        Store fromStore = new Store(availableRoles, reader);
        fromStore.load();
        itemSelector.setName("roles");
        itemSelector.setId("roles");
        itemSelector.setFieldLabel("Roles");
        if (!userId.equals("")) {
            HttpProxy userRoles = new HttpProxy("/semrs/userServlet?userEdit=getRoles&username=" + userId + "&userRoles=true", Connection.GET);
            Store toStore = new Store(userRoles, reader);
            toStore.load();
            itemSelector.setToStore(toStore);
        } else {
            Store toStore = new Store(recordDef);
            toStore.add(recordDef.createRecord(new Object[] { "", "" }));
            toStore.commitChanges();
            itemSelector.setToStore(toStore);
        }
        itemSelector.setMsWidth(160);
        itemSelector.setMsHeight(200);
        itemSelector.setValueField("id");
        itemSelector.setDisplayField("desc");
        itemSelector.setFromStore(fromStore);
        Toolbar fromToolbar = new Toolbar();
        ToolbarButton addButton = new ToolbarButton();
        addButton.setDisabled(true);
        addButton.setIconCls("user-delete-icon");
        fromToolbar.addButton(addButton);
        fromToolbar.addSpacer();
        fromToolbar.addItem(new ToolbarTextItem("Roles Disponibles"));
        itemSelector.setFromToolbar(fromToolbar);
        Toolbar toToolbar = new Toolbar();
        ToolbarButton clearButton = new ToolbarButton("Roles del Usuario");
        clearButton.setIconCls("user-add-icon");
        toToolbar.addButton(clearButton);
        itemSelector.setToToolbar(toToolbar);
        RecordDef formRecordDef = new RecordDef(new FieldDef[] { new StringFieldDef("username"), new StringFieldDef("name"), new StringFieldDef("lastName"), new StringFieldDef("email"), new StringFieldDef("birthDate"), new StringFieldDef("sex"), new StringFieldDef("phoneNumber"), new StringFieldDef("mobile"), new StringFieldDef("address"), new StringFieldDef("password"), new StringFieldDef("passwordRetype"), new StringFieldDef("enabled"), new StringFieldDef("loadSuccess") });
        final JsonReader formReader = new JsonReader("data", formRecordDef);
        formReader.setSuccessProperty("success");
        formReader.setId("username");
        HttpProxy loadProxy = new HttpProxy("/semrs/userServlet?userEdit=load&id=" + userId, Connection.GET);
        Store formStore = new Store(loadProxy, formReader);
        formStore.load();
        formStore.addStoreListener(new StoreListenerAdapter() {

            public void onLoad(Store store, Record[] records) {
                for (int i = 0; i < records.length; i++) {
                    if (store.getRecordAt(i).getAsString("loadSuccess").equals("false") && !userId.equals("")) {
                        MessageBox.show(new MessageBoxConfig() {

                            {
                                setTitle("Error");
                                setMsg("Este usuario no existe");
                                setIconCls(MessageBox.ERROR);
                                setModal(true);
                                setButtons(MessageBox.OK);
                                setCallback(new MessageBox.PromptCallback() {

                                    public void execute(String btnID, String text) {
                                        AdminUsersScreen.reloadFlag = true;
                                        MainPanel.centerPanel.remove(MainPanel.centerPanel.getActiveTab());
                                    }
                                });
                            }
                        });
                        break;
                    } else {
                        usernameText.setValue(store.getRecordAt(i).getAsString("username"));
                        email.setValue(store.getRecordAt(i).getAsString("email"));
                        enabledCB.setValue(store.getRecordAt(i).getAsString("enabled"));
                        nameText.setValue(store.getRecordAt(i).getAsString("name"));
                        lastNameText.setValue(store.getRecordAt(i).getAsString("lastName"));
                        birthDate.setValue(store.getRecordAt(i).getAsString("birthDate"));
                        sexCB.setValue(store.getRecordAt(i).getAsString("sex"));
                        phone.setValue(store.getRecordAt(i).getAsString("phoneNumber"));
                        mobile.setValue(store.getRecordAt(i).getAsString("mobile"));
                        addressTextArea.setValue(store.getRecordAt(i).getAsString("address"));
                    }
                }
            }

            public void onLoadException(Throwable error) {
                if (!userId.equals("")) {
                    MessageBox.show(new MessageBoxConfig() {

                        {
                            setTitle("Error");
                            setMsg("Ocurrio un error al tratar de obtener este usuario");
                            setIconCls(MessageBox.ERROR);
                            setModal(true);
                            setButtons(MessageBox.OK);
                            setCallback(new MessageBox.PromptCallback() {

                                public void execute(String btnID, String text) {
                                    AdminUsersScreen.reloadFlag = true;
                                    MainPanel.centerPanel.remove(MainPanel.centerPanel.getActiveTab());
                                }
                            });
                        }
                    });
                }
            }
        });
        FieldSet rolesFS = new FieldSet("Roles de Usuario");
        rolesFS.setFrame(false);
        rolesFS.add(itemSelector);
        rolesFS.setAutoWidth(true);
        rolesFS.setAutoHeight(true);
        Panel firstColumn = new Panel();
        firstColumn.setLayout(new FormLayout());
        firstColumn.setBorder(true);
        firstColumn.setFrame(true);
        firstColumn.add(userFS, new AnchorLayoutData("100%"));
        firstColumn.add(contactFS, new AnchorLayoutData("100%"));
        Panel secondColumn = new Panel();
        secondColumn.setLayout(new FormLayout());
        secondColumn.setBorder(true);
        secondColumn.setFrame(true);
        secondColumn.add(personalFS, new AnchorLayoutData("100%"));
        secondColumn.add(rolesFS, new AnchorLayoutData("100%"));
        Panel columnPanel = new Panel();
        columnPanel.setLayout(new ColumnLayout());
        columnPanel.setButtonAlign(Position.CENTER);
        columnPanel.add(firstColumn, new ColumnLayoutData(0.5));
        columnPanel.add(secondColumn, new ColumnLayoutData(0.5));
        formPanel.add(columnPanel);
        formPanel.add(proxyPanel);
        formPanel.doLayout();
        panel.add(formPanel);
        panel.doLayout();
        return panel;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }
}
