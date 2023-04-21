package org.openremote.modeler.client.widget.buildingmodeler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openremote.modeler.client.event.SubmitEvent;
import org.openremote.modeler.client.listener.FormResetListener;
import org.openremote.modeler.client.listener.FormSubmitListener;
import org.openremote.modeler.client.model.ComboBoxDataModel;
import org.openremote.modeler.client.proxy.DeviceCommandBeanModelProxy;
import org.openremote.modeler.client.rpc.AsyncServiceFactory;
import org.openremote.modeler.client.rpc.AsyncSuccessCallback;
import org.openremote.modeler.client.utils.Protocols;
import org.openremote.modeler.client.widget.ComboBoxExt;
import org.openremote.modeler.client.widget.FormWindow;
import org.openremote.modeler.domain.Device;
import org.openremote.modeler.domain.DeviceCommand;
import org.openremote.modeler.domain.Protocol;
import org.openremote.modeler.domain.ProtocolAttr;
import org.openremote.modeler.protocol.ProtocolAttrDefinition;
import org.openremote.modeler.protocol.ProtocolDefinition;
import org.openremote.modeler.protocol.ProtocolValidator;
import org.openremote.modeler.selenium.DebugId;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

/**
 * The window creates or updates a deviceCommand into server.
 */
public class DeviceCommandWindow extends FormWindow {

    /** The Constant DEVICE_COMMAND_NAME. */
    public static final String DEVICE_COMMAND_NAME = "device_command_name";

    /** The Constant DEVICE_COMMAND_PROTOCOL. */
    public static final String DEVICE_COMMAND_PROTOCOL = "protocol";

    /** The device command. */
    private DeviceCommand deviceCommand = null;

    /** The device. */
    private Device device = null;

    protected boolean hideWindow = true;

    protected LabelField info;

    protected static final String INFO_FIELD = "infoField";

    /**
    * Instantiates a new device command window.
    * 
    * @param device the device
    */
    public DeviceCommandWindow(Device device) {
        super();
        this.device = device;
        setHeading("New command");
        initial();
        show();
    }

    /**
    * Instantiates a new device command window.
    * 
    * @param command the command
    */
    public DeviceCommandWindow(final DeviceCommand command) {
        super();
        AsyncServiceFactory.getDeviceCommandServiceAsync().loadById(command.getOid(), new AsyncSuccessCallback<DeviceCommand>() {

            public void onSuccess(DeviceCommand cmd) {
                command.setProtocol(cmd.getProtocol());
                deviceCommand = command;
                setHeading("Edit command");
                initial();
                show();
            }
        });
    }

    /**
    * Initial.
    */
    private void initial() {
        setWidth(380);
        setAutoHeight(true);
        setLayout(new FlowLayout());
        form.setWidth(370);
        Button submitBtn = new Button("Submit");
        form.addButton(submitBtn);
        submitBtn.addSelectionListener(new FormSubmitListener(form, submitBtn));
        if (deviceCommand == null) {
            info = new LabelField();
            info.setName(INFO_FIELD);
            form.add(info);
            info.hide();
            Button continueButton = new Button("Submit and Continue");
            form.addButton(continueButton);
            continueButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

                public void componentSelected(ButtonEvent ce) {
                    if (form.isValid()) {
                        hideWindow = false;
                        mask("Saving command......");
                        form.submit();
                    }
                }
            });
        }
        Button resetButton = new Button("Reset");
        resetButton.addSelectionListener(new FormResetListener(form));
        form.addButton(resetButton);
        form.addListener(Events.BeforeSubmit, new Listener<FormEvent>() {

            @SuppressWarnings("unchecked")
            public void handleEvent(FormEvent be) {
                List<Field<?>> list = form.getFields();
                Map<String, String> attrMap = new HashMap<String, String>();
                for (Field<?> f : list) {
                    if (DEVICE_COMMAND_PROTOCOL.equals(f.getName())) {
                        Field<BaseModelData> p = (Field<BaseModelData>) f;
                        attrMap.put(DEVICE_COMMAND_PROTOCOL, p.getValue().get(ComboBoxDataModel.getDisplayProperty()).toString());
                    } else {
                        if (f.getValue() != null && !"".equals(f.getValue().toString()) && !INFO_FIELD.equals(f.getName())) {
                            attrMap.put(f.getName(), f.getValue().toString());
                        }
                    }
                }
                AsyncSuccessCallback<BeanModel> callback = new AsyncSuccessCallback<BeanModel>() {

                    @Override
                    public void onSuccess(BeanModel deviceCommandModel) {
                        fireEvent(SubmitEvent.SUBMIT, new SubmitEvent(deviceCommandModel));
                        if (hideWindow) {
                            hide();
                        } else {
                            hideWindow = true;
                            unmask();
                            info.setText("Command '" + deviceCommandModel.get("name") + "' is saved.");
                            info.show();
                        }
                    }
                };
                if (deviceCommand == null) {
                    DeviceCommandBeanModelProxy.saveDeviceCommand(device, attrMap, callback);
                } else {
                    DeviceCommandBeanModelProxy.updateDeviceCommand(deviceCommand, attrMap, callback);
                }
            }
        });
        createFields(Protocols.getInstance());
        add(form);
    }

    /**
    * Creates the fields.
    * 
    * @param protocols the protocols
    */
    private void createFields(Map<String, ProtocolDefinition> protocols) {
        TextField<String> nameField = new TextField<String>();
        nameField.setName(DEVICE_COMMAND_NAME);
        nameField.setFieldLabel("Name");
        nameField.setAllowBlank(false);
        nameField.ensureDebugId(DebugId.DEVICE_COMMAND_NAME_FIELD);
        ComboBoxExt protocol = new ComboBoxExt();
        protocol.setFieldLabel("Protocol");
        protocol.setName(DEVICE_COMMAND_PROTOCOL);
        protocol.setAllowBlank(false);
        protocol.ensureDebugId(DebugId.DEVICE_COMMAND_PROTOCOL_FIELD);
        for (String key : protocols.keySet()) {
            if (!key.equalsIgnoreCase(Protocol.INFRARED_TYPE)) {
                ComboBoxDataModel<ProtocolDefinition> data = new ComboBoxDataModel<ProtocolDefinition>(key, protocols.get(key));
                protocol.getStore().add(data);
            }
        }
        protocol.setDisplayField(ComboBoxDataModel.getDisplayProperty());
        protocol.setEmptyText("Please Select Protocol...");
        protocol.setValueField(ComboBoxDataModel.getDisplayProperty());
        form.add(nameField);
        form.add(protocol);
        protocol.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @SuppressWarnings("unchecked")
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                if (form.getItems().size() > 2) {
                    if (info == null) {
                        form.getItem(2).removeFromParent();
                    } else if (form.getItems().size() > 3) {
                        form.getItem(3).removeFromParent();
                    }
                }
                addAttrs((ComboBoxDataModel<ProtocolDefinition>) se.getSelectedItem());
            }
        });
        if (deviceCommand != null) {
            String protocolName = deviceCommand.getProtocol().getType();
            nameField.setValue(deviceCommand.getName());
            if (protocols.containsKey(protocolName)) {
                ComboBoxDataModel<ProtocolDefinition> data = new ComboBoxDataModel<ProtocolDefinition>(protocolName, protocols.get(protocolName));
                protocol.setValue(data);
            }
        }
        form.layout();
    }

    /**
    * Adds the attrs.
    * 
    * @param data the data
    */
    private void addAttrs(ComboBoxDataModel<ProtocolDefinition> data) {
        FieldSet attrSet = new FieldSet();
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(80);
        attrSet.setLayout(layout);
        attrSet.setHeading(data.getLabel() + " attributes");
        for (ProtocolAttrDefinition attrDefinition : data.getData().getAttrs()) {
            List<String> options = attrDefinition.getOptions();
            String value = "";
            if (attrDefinition.getValue() != null) {
                value = attrDefinition.getValue();
            } else if (deviceCommand != null) {
                for (ProtocolAttr attr : deviceCommand.getProtocol().getAttributes()) {
                    if (attrDefinition.getName().equals(attr.getName())) {
                        value = attr.getValue();
                    }
                }
            }
            if (options.size() > 0) {
                ComboBoxExt comboAttrField = new ComboBoxExt();
                comboAttrField.setName(attrDefinition.getName());
                comboAttrField.setFieldLabel(attrDefinition.getLabel());
                ComboBoxExt.ComboBoxMessages comboBoxMessages = comboAttrField.getMessages();
                for (String option : options) {
                    if (!"".equals(option)) {
                        StringComboBoxData comboData = new StringComboBoxData(option, option);
                        comboAttrField.getStore().add(comboData);
                        if (value.equals(option)) {
                            comboAttrField.setValue(comboData);
                        }
                    }
                }
                setComboBoxValidators(comboAttrField, comboBoxMessages, attrDefinition.getValidators());
                attrSet.add(comboAttrField);
            } else {
                TextField<String> attrField = new TextField<String>();
                attrField.setName(attrDefinition.getName());
                TextField<String>.TextFieldMessages<String> messages = attrField.getMessages();
                attrField.setFieldLabel(attrDefinition.getLabel());
                if (!"".equals(value)) {
                    attrField.setValue(value);
                }
                setValidators(attrField, messages, attrDefinition.getValidators());
                attrSet.add(attrField);
            }
        }
        form.add(attrSet);
        form.layout();
        if (isRendered()) {
            center();
        }
    }

    /**
    * Sets the validators.
    * 
    * @param attrField the attr file
    * @param messages the messages
    * @param protocolValidators the protocol validators
    */
    private void setValidators(TextField<String> attrField, TextField<String>.TextFieldMessages<String> messages, List<ProtocolValidator> protocolValidators) {
        for (ProtocolValidator protocolValidator : protocolValidators) {
            if (protocolValidator.getType() == ProtocolValidator.ALLOW_BLANK_TYPE) {
                if (Boolean.valueOf(protocolValidator.getValue())) {
                    attrField.setAllowBlank(true);
                } else {
                    attrField.setAllowBlank(false);
                    messages.setBlankText(protocolValidator.getMessage());
                }
            } else if (protocolValidator.getType() == ProtocolValidator.MAX_LENGTH_TYPE) {
                attrField.setMaxLength(Integer.valueOf(protocolValidator.getValue()));
                messages.setMaxLengthText(protocolValidator.getMessage());
            } else if (protocolValidator.getType() == ProtocolValidator.MIN_LENGTH_TYPE) {
                attrField.setMinLength(Integer.valueOf(protocolValidator.getValue()));
                messages.setMinLengthText(protocolValidator.getMessage());
            } else if (protocolValidator.getType() == ProtocolValidator.REGEX_TYPE) {
                attrField.setRegex(protocolValidator.getValue());
                messages.setRegexText(protocolValidator.getMessage());
            }
        }
    }

    private void setComboBoxValidators(ComboBoxExt comboField, ComboBoxExt.ComboBoxMessages messages, List<ProtocolValidator> protocolValidators) {
        for (ProtocolValidator protocolValidator : protocolValidators) {
            if (protocolValidator.getType() == ProtocolValidator.ALLOW_BLANK_TYPE) {
                if (Boolean.valueOf(protocolValidator.getValue())) {
                    comboField.setAllowBlank(true);
                } else {
                    comboField.setAllowBlank(false);
                    messages.setBlankText(protocolValidator.getMessage());
                }
            } else if (protocolValidator.getType() == ProtocolValidator.MAX_LENGTH_TYPE) {
                comboField.setMaxLength(Integer.valueOf(protocolValidator.getValue()));
                messages.setMaxLengthText(protocolValidator.getMessage());
            } else if (protocolValidator.getType() == ProtocolValidator.MIN_LENGTH_TYPE) {
                comboField.setMinLength(Integer.valueOf(protocolValidator.getValue()));
                messages.setMinLengthText(protocolValidator.getMessage());
            } else if (protocolValidator.getType() == ProtocolValidator.REGEX_TYPE) {
                comboField.setRegex(protocolValidator.getValue());
                messages.setRegexText(protocolValidator.getMessage());
            }
        }
    }

    /**
    * The data model to store String value in ComboBoxExt.
    */
    private class StringComboBoxData extends ComboBoxDataModel<String> {

        private static final long serialVersionUID = 1L;

        public StringComboBoxData(String label, String t) {
            super(label, t);
        }

        public String toString() {
            return super.getData();
        }
    }
}
