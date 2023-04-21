package org.openremote.modeler.client.widget.buildingmodeler;

import java.util.ArrayList;
import java.util.List;
import org.openremote.modeler.client.event.SubmitEvent;
import org.openremote.modeler.client.gxtextends.NestedJsonLoadResultReader;
import org.openremote.modeler.client.proxy.DeviceCommandBeanModelProxy;
import org.openremote.modeler.client.rpc.AsyncSuccessCallback;
import org.openremote.modeler.client.rpc.ConfigurationRPCService;
import org.openremote.modeler.client.rpc.ConfigurationRPCServiceAsync;
import org.openremote.modeler.client.widget.CommonForm;
import org.openremote.modeler.client.widget.RemoteJsonComboBox;
import org.openremote.modeler.domain.Device;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.DataField;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelType;
import com.extjs.gxt.ui.client.data.ScriptTagProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ListModelPropertyEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.google.gwt.core.client.GWT;

/**
 * IR Command Import Form.
 * 
 * @author Dan 2009-8-21
 */
public class IRCommandImportForm extends CommonForm {

    /** The beehive rest url. */
    public static String beehiveLircRestUrl = null;

    /** The configuration service. */
    private ConfigurationRPCServiceAsync configurationService = (ConfigurationRPCServiceAsync) GWT.create(ConfigurationRPCService.class);

    /** The device. */
    protected Device device = null;

    /** The select container. */
    private LayoutContainer selectContainer = new LayoutContainer();

    /** The command container. */
    private LayoutContainer commandContainer = new LayoutContainer();

    /** The import button. */
    protected Button importButton;

    /** The vendor list. */
    private RemoteJsonComboBox<ModelData> vendorList = null;

    /** The model list. */
    private RemoteJsonComboBox<ModelData> modelList = null;

    /** The section list. */
    private RemoteJsonComboBox<ModelData> sectionList = null;

    /** The code grid. */
    protected Grid<ModelData> codeGrid = null;

    /** The code type. */
    private ModelType codeType = null;

    /** The cm. */
    private ColumnModel cm = null;

    protected Component wrapper;

    /** The section id. */
    private String sectionId;

    /**
    * Instantiates a new iR command import form.
    * 
    * @param wrapper the wrapper
    * @param deviceBeanModel the device bean model
    */
    public IRCommandImportForm(final Component wrapper, BeanModel deviceBeanModel) {
        super();
        setHeight(300);
        this.wrapper = wrapper;
        setLayout(new RowLayout(Orientation.VERTICAL));
        HBoxLayout selectContainerLayout = new HBoxLayout();
        selectContainerLayout.setPadding(new Padding(5));
        selectContainerLayout.setHBoxLayoutAlign(HBoxLayoutAlign.TOP);
        selectContainer.setLayout(selectContainerLayout);
        selectContainer.setLayoutOnChange(true);
        add(selectContainer, new RowData(1, 35));
        commandContainer.setLayout(new CenterLayout());
        commandContainer.setLayoutOnChange(true);
        add(commandContainer, new RowData(1, 1));
        device = (Device) deviceBeanModel.getBean();
        if (beehiveLircRestUrl == null) {
            configurationService.beehiveRESTRootUrl(new AsyncSuccessCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    beehiveLircRestUrl = result + "lirc/";
                    addVendorsList();
                }
            });
        } else {
            addVendorsList();
        }
        onSubmit(wrapper);
    }

    /**
    * On submit.
    * 
    * @param wrapper the wrapper
    */
    protected void onSubmit(final Component wrapper) {
        addListener(Events.BeforeSubmit, new Listener<FormEvent>() {

            public void handleEvent(FormEvent be) {
                wrapper.mask("Please Wait...");
                if (importButton != null) {
                    importButton.setEnabled(false);
                }
                if (codeGrid != null) {
                    List<ModelData> modelDatas = codeGrid.getSelectionModel().getSelectedItems();
                    if (modelDatas.isEmpty()) {
                        modelDatas = codeGrid.getStore().getModels();
                    }
                    for (ModelData modelData : modelDatas) {
                        modelData.set("sectionId", sectionId);
                    }
                    DeviceCommandBeanModelProxy.saveAllDeviceCommands(device, modelDatas, new AsyncSuccessCallback<List<BeanModel>>() {

                        @Override
                        public void onSuccess(List<BeanModel> deviceCommandModels) {
                            wrapper.fireEvent(SubmitEvent.SUBMIT, new SubmitEvent(deviceCommandModels));
                        }
                    });
                } else {
                    MessageBox.alert("Warn", "Please select vendor, model first.", null);
                    wrapper.unmask();
                }
            }
        });
    }

    @Override
    protected void addButtons() {
        importButton = new Button("Import");
        importButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                submit();
            }
        });
        addButton(importButton);
    }

    /**
    * Adds the vendors list.
    */
    private void addVendorsList() {
        ModelType vendorType = new ModelType();
        vendorType.setRoot("vendors.vendor");
        DataField idField = new DataField("id");
        idField.setType(Long.class);
        vendorType.addField(idField);
        vendorType.addField("name");
        final String emptyText = "Please Select Vendor ...";
        vendorList = new RemoteJsonComboBox<ModelData>(beehiveLircRestUrl, vendorType);
        vendorList.setEmptyText(emptyText);
        vendorList.setDisplayField("name");
        vendorList.setValueField("name");
        setStyleOfComboBox(vendorList);
        vendorList.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                addModelList(se.getSelectedItem().get("name").toString());
            }
        });
        selectContainer.add(vendorList);
    }

    /**
    * Adds the model list.
    * 
    * @param vendor the vendor
    */
    private void addModelList(final String vendor) {
        ModelType modelType = new ModelType();
        modelType.setRoot("models.model");
        DataField idField = new DataField("id");
        idField.setType(Long.class);
        modelType.addField(idField);
        modelType.addField("name");
        modelType.addField("fileName");
        final String emptyText = "Please Select Model ...";
        String url = beehiveLircRestUrl + vendor;
        if (modelList != null) {
            clearComboBox(modelList);
            clearComboBox(sectionList);
            beginUpdate(modelList, url);
        } else {
            modelList = new RemoteJsonComboBox<ModelData>(url, modelType);
            modelList.setEmptyText(emptyText);
            modelList.setDisplayField("name");
            modelList.setValueField("name");
            setStyleOfComboBox(modelList);
            modelList.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

                @Override
                public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                    addSectionList(vendorList.getRawValue(), se.getSelectedItem().get("name").toString());
                }
            });
            modelList.addListener(ListStore.DataChanged, new Listener<BaseEvent>() {

                public void handleEvent(BaseEvent be) {
                    endUpdate(modelList, emptyText);
                }
            });
            selectContainer.add(modelList);
        }
    }

    /**
    * Adds the section list.
    * 
    * @param venderName the vender name
    * @param modelName the model name
    */
    private void addSectionList(String venderName, String modelName) {
        ModelType sectionType = new ModelType();
        sectionType.setRoot("sections.section");
        DataField idField = new DataField("id");
        idField.setType(Long.class);
        sectionType.addField(idField);
        sectionType.addField("name");
        String url = beehiveLircRestUrl + venderName + "/" + modelName;
        final String emptyText = "Please Select Section ...";
        if (sectionList != null) {
            clearComboBox(sectionList);
            beginUpdate(sectionList, url);
        } else {
            sectionList = new RemoteJsonComboBox<ModelData>(url, sectionType);
            sectionList.setEmptyText(emptyText);
            sectionList.setValueField("id");
            setStyleOfComboBox(sectionList);
            sectionList.setPropertyEditor(new ListModelPropertyEditor<ModelData>() {

                @Override
                public String getStringValue(ModelData value) {
                    Object obj = value.get(displayProperty);
                    String id = value.get("id").toString();
                    if (obj != null) {
                        return obj.toString() + " [" + id + "]";
                    }
                    return null;
                }

                @Override
                public ModelData convertStringValue(String value) {
                    for (ModelData d : models) {
                        Object val = d.get("id");
                        int left = value.lastIndexOf("[");
                        int right = value.lastIndexOf("]");
                        String id = value.substring(left + 1, right);
                        if (id.equals(val != null ? val.toString() : null)) {
                            return d;
                        }
                    }
                    return null;
                }
            });
            sectionList.setDisplayField("name");
            sectionList.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

                @Override
                public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                    long idstr = se.getSelectedItem().get("id");
                    showCodesGrid(vendorList.getRawValue(), modelList.getRawValue(), idstr);
                }
            });
            sectionList.addListener(ListStore.DataChanged, new Listener<BaseEvent>() {

                public void handleEvent(BaseEvent be) {
                    endUpdate(sectionList, emptyText);
                }
            });
            selectContainer.add(sectionList);
        }
    }

    /**
    * Sets the style of combo box.
    * 
    * @param box the new style of combo box
    */
    private void setStyleOfComboBox(RemoteJsonComboBox<ModelData> box) {
        box.setWidth(170);
        box.setMaxHeight(250);
    }

    /**
    * Clear combo box.
    * 
    * @param box the box
    */
    private void clearComboBox(RemoteJsonComboBox<ModelData> box) {
        if (box != null) {
            box.clearSelections();
            box.getStore().removeAll();
        }
    }

    /**
    * Begin update.
    * 
    * @param box the box
    * @param url the url
    */
    private void beginUpdate(RemoteJsonComboBox<ModelData> box, String url) {
        if (box != null) {
            box.reloadListStoreWithUrl(url);
        }
    }

    /**
    * End update.
    * 
    * @param box the box
    * @param emptyStr the empty str
    */
    private void endUpdate(RemoteJsonComboBox<ModelData> box, String emptyStr) {
        if (box != null) {
        }
    }

    /**
    * Show codes grid.
    * 
    * @param vendor the vendor
    * @param model the model
    * @param sectionId the section id
    */
    private void showCodesGrid(String vendor, String model, long sectionId) {
        if (importButton != null) {
            importButton.setEnabled(true);
        }
        if (codeType == null) {
            codeType = new ModelType();
            codeType.setRoot("codes.code");
            DataField idField = new DataField("id");
            idField.setType(Long.class);
            codeType.addField(idField);
            codeType.addField("name");
            codeType.addField("remoteName");
            codeType.addField("value");
            codeType.addField("comment");
        }
        StringBuffer url = new StringBuffer(beehiveLircRestUrl);
        url.append(vendor);
        url.append("/");
        url.append(model);
        url.append("/");
        url.append(sectionId);
        url.append("/");
        url.append("codes");
        if (codeGrid == null) {
            addCodeGrid(url.toString());
        } else {
            reloadGrid(url.toString());
        }
        this.sectionId = String.valueOf(sectionId);
    }

    /**
    * Adds the code grid.
    * 
    * @param url the url
    */
    private void addCodeGrid(String url) {
        ScriptTagProxy<ListLoadResult<ModelData>> scriptTagProxy = new ScriptTagProxy<ListLoadResult<ModelData>>(url.toString());
        NestedJsonLoadResultReader<ListLoadResult<ModelData>> reader = new NestedJsonLoadResultReader<ListLoadResult<ModelData>>(codeType);
        final BaseListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(scriptTagProxy, reader);
        ListStore<ModelData> listStore = new ListStore<ModelData>(loader);
        if (cm == null) {
            List<ColumnConfig> codeGridColumns = new ArrayList<ColumnConfig>();
            codeGridColumns.add(new ColumnConfig("name", "Name", 120));
            codeGridColumns.add(new ColumnConfig("remoteName", "Remote Name", 150));
            codeGridColumns.add(new ColumnConfig("value", "Value", 250));
            cm = new ColumnModel(codeGridColumns);
        }
        codeGrid = new Grid<ModelData>(listStore, cm);
        codeGrid.setLoadMask(true);
        codeGrid.setHeight(200);
        commandContainer.add(codeGrid);
        loader.load();
        if (importButton != null) {
            importButton.setEnabled(true);
        }
    }

    /**
    * Reload grid.
    * 
    * @param url the url
    */
    private void reloadGrid(String url) {
        codeGrid.getStore().removeAll();
        codeGrid.setLoadMask(true);
        ScriptTagProxy<ListLoadResult<ModelData>> scriptTagProxy = new ScriptTagProxy<ListLoadResult<ModelData>>(url.toString());
        NestedJsonLoadResultReader<ListLoadResult<ModelData>> reader = new NestedJsonLoadResultReader<ListLoadResult<ModelData>>(codeType);
        final BaseListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(scriptTagProxy, reader);
        ListStore<ModelData> listStore = new ListStore<ModelData>(loader);
        codeGrid.reconfigure(listStore, cm);
        loader.load();
        importButton.setEnabled(true);
    }

    /**
    * Gets the device.
    * 
    * @return the device
    */
    public Device getDevice() {
        return device;
    }

    /**
    * Sets the device.
    * 
    * @param device the new device
    */
    public void setDevice(Device device) {
        this.device = device;
    }

    /**
    * Gets the section id.
    * 
    * @return the section id
    */
    public String getSectionId() {
        return sectionId;
    }

    /**
    * Sets the section id.
    * 
    * @param sectionId the new section id
    */
    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
