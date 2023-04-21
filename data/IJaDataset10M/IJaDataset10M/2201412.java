package org.appspy.admin.client.scheduling;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.appspy.admin.client.AdminModule;
import org.appspy.admin.client.reports.form.ReportParamChooserForm;
import org.appspy.admin.client.scheduling.form.JobDeclarationParamForm;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.ObjectFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class ListJobDeclarationParamPanel extends Panel {

    protected AdminModule mAdminModule = null;

    protected GridPanel mGridPanel = null;

    protected RecordDef mRecordDef = new RecordDef(new FieldDef[] { new ObjectFieldDef("object"), new IntegerFieldDef("id"), new StringFieldDef("name"), new IntegerFieldDef("chooserId"), new StringFieldDef("chooserName") });

    protected RecordDef mChooserRecordDef = new RecordDef(new FieldDef[] { new ObjectFieldDef("object"), new IntegerFieldDef("id"), new StringFieldDef("name") });

    protected Store mStore = null;

    protected Store mChooserStore = null;

    public ListJobDeclarationParamPanel(AdminModule adminModule) {
        mAdminModule = adminModule;
        setLayout(new RowLayout());
        mStore = new Store(mRecordDef);
        mChooserStore = new Store(mChooserRecordDef);
        ColumnConfig idColumnConfig = new ColumnConfig("ID", "id", 100, true);
        ColumnConfig nameColumnConfig = new ColumnConfig("NAME", "name", 100, true);
        ColumnConfig chooserColumnConfig = new ColumnConfig("CHOOSER", "chooserName", 100, true);
        ColumnConfig[] columns = new ColumnConfig[] { idColumnConfig, nameColumnConfig, chooserColumnConfig };
        ColumnModel columnModel = new ColumnModel(columns);
        columnModel.setDefaultSortable(true);
        columnModel.setHidden(0, true);
        mGridPanel = new EditorGridPanel();
        mGridPanel.setColumnModel(columnModel);
        mGridPanel.setStore(mStore);
        mGridPanel.setFrame(true);
        mGridPanel.setStripeRows(true);
        mGridPanel.setAutoExpandColumn(1);
        mGridPanel.setAutoScroll(true);
        final RowSelectionModel rowSelectionModel = new RowSelectionModel();
        mGridPanel.setSelectionModel(rowSelectionModel);
        Toolbar toolbar = new Toolbar();
        ToolbarButton newButton = new ToolbarButton("Add", new ButtonListenerAdapter() {

            public void onClick(Button button, EventObject e) {
                final Window window = new Window();
                window.setTitle("New Parameter");
                window.setClosable(true);
                window.setFrame(true);
                window.setWidth(450);
                window.setHeight(350);
                window.setAutoScroll(true);
                window.setPlain(true);
                window.setLayout(new FitLayout());
                window.setCloseAction(Window.CLOSE);
                window.setModal(true);
                final FormPanel formPanel = new FormPanel();
                formPanel.setPaddings(10, 10, 10, 10);
                formPanel.setFrame(true);
                final JobDeclarationParamForm param = new JobDeclarationParamForm();
                final Record record = mRecordDef.createRecord(new Object[] { param, null, "", null, "" });
                TextField nameTextField = new TextField("Name", "name");
                formPanel.add(nameTextField, new AnchorLayoutData("100%"));
                final ComboBox chooserComboBox = new ComboBox("Chooser", "chooserId");
                formPanel.add(chooserComboBox, new AnchorLayoutData("100%"));
                chooserComboBox.setStore(mChooserStore);
                chooserComboBox.setValueField("id");
                chooserComboBox.setDisplayField("name");
                chooserComboBox.setMode(ComboBox.LOCAL);
                chooserComboBox.setForceSelection(true);
                chooserComboBox.setDisableKeyFilter(true);
                chooserComboBox.setLazyRender(true);
                chooserComboBox.setTriggerAction(ComboBox.ALL);
                final Button createButton = new Button("Create");
                formPanel.addButton(createButton);
                formPanel.getForm().loadRecord(record);
                createButton.addListener(new ButtonListenerAdapter() {

                    @Override
                    public void onClick(Button button, EventObject e) {
                        formPanel.getForm().updateRecord(record);
                        param.setName(record.getAsString("name"));
                        ReportParamChooserForm chooser = (ReportParamChooserForm) mChooserStore.query("id", chooserComboBox.getValueAsString())[0].getAsObject("object");
                        record.set("chooserName", chooser.getName());
                        param.setChooser(chooser);
                        mStore.add(record);
                        record.commit();
                        window.close();
                    }
                });
                window.add(formPanel);
                window.show();
            }
        });
        newButton.setIcon("images/silk/add.gif");
        toolbar.addButton(newButton);
        toolbar.addSeparator();
        ToolbarButton editButton = new ToolbarButton("Edit", new ButtonListenerAdapter() {

            public void onClick(Button button, EventObject e) {
                final Window window = new Window();
                window.setTitle("Edit Parameter");
                window.setClosable(true);
                window.setFrame(true);
                window.setWidth(450);
                window.setHeight(350);
                window.setAutoScroll(true);
                window.setPlain(true);
                window.setLayout(new FitLayout());
                window.setCloseAction(Window.CLOSE);
                window.setModal(true);
                final FormPanel formPanel = new FormPanel();
                formPanel.setPaddings(10, 10, 10, 10);
                formPanel.setFrame(true);
                final Record record = mGridPanel.getSelectionModel().getSelected();
                final JobDeclarationParamForm param = (JobDeclarationParamForm) record.getAsObject("object");
                final FieldListener fieldListener = new FieldListenerAdapter() {

                    @Override
                    public void onRender(Component component) {
                        formPanel.getForm().loadRecord(record);
                    }
                };
                TextField nameTextField = new TextField("Name", "name");
                formPanel.add(nameTextField, new AnchorLayoutData("100%"));
                nameTextField.addListener(fieldListener);
                final ComboBox chooserComboBox = new ComboBox("Chooser", "chooserId");
                formPanel.add(chooserComboBox, new AnchorLayoutData("100%"));
                chooserComboBox.setStore(mChooserStore);
                chooserComboBox.setValueField("id");
                chooserComboBox.setDisplayField("name");
                chooserComboBox.setMode(ComboBox.LOCAL);
                chooserComboBox.setForceSelection(true);
                chooserComboBox.setDisableKeyFilter(true);
                chooserComboBox.setLazyRender(true);
                chooserComboBox.setTriggerAction(ComboBox.ALL);
                chooserComboBox.addListener(fieldListener);
                final Button saveButton = new Button("Save");
                formPanel.addButton(saveButton);
                saveButton.addListener(new ButtonListenerAdapter() {

                    @Override
                    public void onClick(Button button, EventObject e) {
                        formPanel.getForm().updateRecord(record);
                        param.setName(record.getAsString("name"));
                        ReportParamChooserForm chooser = (ReportParamChooserForm) mChooserStore.query("id", chooserComboBox.getValueAsString())[0].getAsObject("object");
                        record.set("chooserName", chooser.getName());
                        param.setChooser(chooser);
                        record.commit();
                        window.close();
                    }
                });
                final Button cancelButton = new Button("Cancel");
                formPanel.addButton(cancelButton);
                cancelButton.addListener(new ButtonListenerAdapter() {

                    @Override
                    public void onClick(Button button, EventObject e) {
                        formPanel.getForm().loadRecord(record);
                    }
                });
                window.add(formPanel);
                window.show();
            }
        });
        editButton.setIcon("images/silk/add.gif");
        toolbar.addButton(editButton);
        toolbar.addSeparator();
        ToolbarButton delButton = new ToolbarButton("Del", new ButtonListenerAdapter() {

            public void onClick(Button button, EventObject e) {
                try {
                    final Record record = mGridPanel.getSelectionModel().getSelected();
                    if (record != null) {
                        mStore.remove(record);
                    }
                } catch (Exception ex) {
                    MessageBox.alert(ex.getMessage());
                }
            }
        });
        delButton.setIcon("images/silk/delete.gif");
        toolbar.addButton(delButton);
        toolbar.addSeparator();
        toolbar.addSpacer();
        mGridPanel.setTopToolbar(toolbar);
        toolbar.addFill();
        mGridPanel.setTitle("Parameters");
        add(mGridPanel);
    }

    @Override
    protected void initComponent() {
        refreshDatas();
    }

    public void refreshDatas() {
        AsyncCallback<Collection<ReportParamChooserForm>> callback = new AsyncCallback<Collection<ReportParamChooserForm>>() {

            public void onSuccess(Collection<ReportParamChooserForm> choosers) {
                try {
                    mChooserStore.removeAll();
                    mChooserStore.commitChanges();
                    for (ReportParamChooserForm chooser : choosers) {
                        mChooserStore.add(mChooserRecordDef.createRecord(new Object[] { chooser, chooser.getId(), chooser.getName() }));
                    }
                    mChooserStore.commitChanges();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", caught.getLocalizedMessage());
            }
        };
        try {
            mAdminModule.getAdminModuleService().getReportParamChoosers(callback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Set<JobDeclarationParamForm> getParameters() {
        HashSet<JobDeclarationParamForm> params = new HashSet<JobDeclarationParamForm>();
        for (Record record : mStore.getRecords()) {
            params.add((JobDeclarationParamForm) record.getAsObject("object"));
        }
        return params;
    }

    public void setParameters(Set<JobDeclarationParamForm> params) {
        mStore.removeAll();
        for (JobDeclarationParamForm param : params) {
            mStore.add(mRecordDef.createRecord(new Object[] { param, param.getId(), param.getName(), param.getChooser() == null ? null : param.getChooser().getId(), param.getChooser() == null ? "" : param.getChooser().getName() }));
        }
    }
}
