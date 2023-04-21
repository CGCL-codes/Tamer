package org.openremote.modeler.client.widget.buildingmodeler;

import java.util.List;
import org.openremote.modeler.client.event.SubmitEvent;
import org.openremote.modeler.client.listener.FormResetListener;
import org.openremote.modeler.client.listener.FormSubmitListener;
import org.openremote.modeler.client.listener.SubmitListener;
import org.openremote.modeler.client.model.ComboBoxDataModel;
import org.openremote.modeler.client.proxy.BeanModelDataBase;
import org.openremote.modeler.client.proxy.SwitchBeanModelProxy;
import org.openremote.modeler.client.rpc.AsyncSuccessCallback;
import org.openremote.modeler.client.utils.DeviceCommandSelectWindow;
import org.openremote.modeler.client.widget.FormWindow;
import org.openremote.modeler.client.widget.ComboBoxExt;
import org.openremote.modeler.domain.Device;
import org.openremote.modeler.domain.DeviceCommand;
import org.openremote.modeler.domain.DeviceCommandRef;
import org.openremote.modeler.domain.Sensor;
import org.openremote.modeler.domain.Switch;
import org.openremote.modeler.domain.SwitchCommandOffRef;
import org.openremote.modeler.domain.SwitchCommandOnRef;
import org.openremote.modeler.domain.SwitchSensorRef;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

/**
 * The window to creates or updates a switch in a device.
 */
public class SwitchWindow extends FormWindow {

    public static final String SWITCH_NAME_FIELD_NAME = "name";

    public static final String SWITCH_SENSOR_FIELD_NAME = "sensor";

    public static final String SWITCH_ON_COMMAND_FIELD_NAME = "command(on)";

    public static final String SWITCH_OFF_COMMAND_FIELD_NAME = "command(off)";

    protected Switch switchToggle = null;

    private TextField<String> nameField = new TextField<String>();

    protected ComboBox<ModelData> sensorField = new ComboBoxExt();

    protected Button switchOnBtn = new Button("select");

    protected Button switchOffBtn = new Button("select");

    private boolean edit = false;

    /**
    * Instantiates a window to edit the switch.
    * 
    * @param switchToggle the switch toggle
    */
    public SwitchWindow(Switch switchToggle) {
        this(switchToggle, null);
    }

    /**
    * Instantiates a window to create or edit a switch.
    * 
    * @param switchToggle the switch toggle
    * @param device the device
    */
    public SwitchWindow(Switch switchToggle, Device device) {
        super();
        if (null != switchToggle) {
            this.switchToggle = switchToggle;
            edit = true;
        } else {
            this.switchToggle = new Switch();
            edit = false;
        }
        if (device != null) {
            this.switchToggle.setDevice(device);
        }
        this.setHeading(edit ? "Edit Switch" : "New Switch");
        this.setSize(320, 240);
        createField();
        show();
    }

    /**
    * Creates the switch's fields, which includes name, sensor, on command and off command.
    */
    private void createField() {
        setWidth(380);
        setAutoHeight(true);
        setLayout(new FlowLayout());
        form.setWidth(370);
        nameField.setFieldLabel(SWITCH_NAME_FIELD_NAME);
        nameField.setName(SWITCH_NAME_FIELD_NAME);
        nameField.setAllowBlank(false);
        sensorField.setFieldLabel(SWITCH_SENSOR_FIELD_NAME);
        sensorField.setName(SWITCH_SENSOR_FIELD_NAME);
        ListStore<ModelData> sensorStore = new ListStore<ModelData>();
        List<BeanModel> sensors = BeanModelDataBase.sensorTable.loadAll();
        for (BeanModel sensorBean : sensors) {
            Sensor sensor = sensorBean.getBean();
            if (sensor.getDevice().equals(switchToggle.getDevice())) {
                ComboBoxDataModel<Sensor> sensorRefSelector = new ComboBoxDataModel<Sensor>(sensor.getName(), sensor);
                sensorStore.add(sensorRefSelector);
            }
        }
        sensorField.setStore(sensorStore);
        sensorField.addSelectionChangedListener(new SensorSelectChangeListener());
        if (edit) {
            nameField.setValue(switchToggle.getName());
            if (switchToggle.getSwitchSensorRef() != null) {
                sensorField.setValue(new ComboBoxDataModel<Sensor>(switchToggle.getSwitchSensorRef().getSensor().getDisplayName(), switchToggle.getSwitchSensorRef().getSensor()));
            }
            switchOnBtn.setText(switchToggle.getSwitchCommandOnRef().getDisplayName());
            switchOffBtn.setText(switchToggle.getSwitchCommandOffRef().getDisplayName());
        }
        AdapterField switchOnAdapter = new AdapterField(switchOnBtn);
        switchOnAdapter.setFieldLabel(SWITCH_ON_COMMAND_FIELD_NAME);
        AdapterField switchOffAdapter = new AdapterField(switchOffBtn);
        switchOffAdapter.setFieldLabel(SWITCH_OFF_COMMAND_FIELD_NAME);
        Button submitBtn = new Button("Submit");
        Button resetButton = new Button("Reset");
        form.add(nameField);
        form.add(sensorField);
        form.add(switchOnAdapter);
        form.add(switchOffAdapter);
        form.addButton(submitBtn);
        form.addButton(resetButton);
        submitBtn.addSelectionListener(new FormSubmitListener(form, submitBtn));
        resetButton.addSelectionListener(new FormResetListener(form));
        switchOnBtn.addSelectionListener(new CommandSelectListener(true));
        switchOffBtn.addSelectionListener(new CommandSelectListener(false));
        form.addListener(Events.BeforeSubmit, new SwitchSubmitListener());
        add(form);
    }

    /**
    * The listener to submit the window, save the switch data into device and server.
    */
    class SwitchSubmitListener implements Listener<FormEvent> {

        @Override
        public void handleEvent(FormEvent be) {
            if (switchToggle.getSwitchCommandOnRef() == null || switchToggle.getSwitchCommandOffRef() == null) {
                MessageBox.alert("Switch", "A switch must have the command to control its on and off", null);
                return;
            }
            List<Field<?>> fields = form.getFields();
            for (Field<?> field : fields) {
                if (SWITCH_NAME_FIELD_NAME.equals(field.getName())) {
                    switchToggle.setName(field.getValue().toString());
                    break;
                }
            }
            if (!edit) {
                SwitchBeanModelProxy.save(switchToggle.getBeanModel(), new AsyncSuccessCallback<Switch>() {

                    @Override
                    public void onSuccess(Switch result) {
                        fireEvent(SubmitEvent.SUBMIT, new SubmitEvent(result.getBeanModel()));
                    }

                    ;
                });
            } else {
                SwitchCommandOnRef onRef = new SwitchCommandOnRef();
                onRef.setDeviceCommand(switchToggle.getSwitchCommandOnRef().getDeviceCommand());
                onRef.setDeviceName(switchToggle.getDevice().getName());
                onRef.setOnSwitch(switchToggle);
                SwitchCommandOffRef offRef = new SwitchCommandOffRef();
                offRef.setDeviceCommand(switchToggle.getSwitchCommandOffRef().getDeviceCommand());
                offRef.setDeviceName(switchToggle.getDevice().getName());
                offRef.setOffSwitch(switchToggle);
                SwitchSensorRef sensorRef = new SwitchSensorRef(switchToggle);
                sensorRef.setSensor(switchToggle.getSwitchSensorRef().getSensor());
                sensorRef.setSwitchToggle(switchToggle);
                switchToggle.setSwitchCommandOnRef(onRef);
                switchToggle.setSwitchCommandOffRef(offRef);
                switchToggle.setSwitchSensorRef(sensorRef);
                SwitchBeanModelProxy.update(switchToggle.getBeanModel(), new AsyncSuccessCallback<Switch>() {

                    @Override
                    public void onSuccess(Switch result) {
                        fireEvent(SubmitEvent.SUBMIT, new SubmitEvent(result.getBeanModel()));
                    }

                    ;
                });
            }
        }
    }

    /**
    * The listener to select on or off command for the switch.
    * 
    */
    class CommandSelectListener extends SelectionListener<ButtonEvent> {

        private boolean forSwitchOn = true;

        /**
       * Instantiates a new command select listener by the boolean.
       * If true, select a on command, else select a off command.
       * 
       * @param forSwitchOn the for switch on
       */
        public CommandSelectListener(boolean forSwitchOn) {
            this.forSwitchOn = forSwitchOn;
        }

        @Override
        public void componentSelected(ButtonEvent ce) {
            final DeviceCommandSelectWindow selectCommandWindow = new DeviceCommandSelectWindow(SwitchWindow.this.switchToggle.getDevice());
            final Button command = ce.getButton();
            selectCommandWindow.addListener(SubmitEvent.SUBMIT, new SubmitListener() {

                @Override
                public void afterSubmit(SubmitEvent be) {
                    BeanModel dataModel = be.<BeanModel>getData();
                    DeviceCommand deviceCommand = null;
                    if (dataModel.getBean() instanceof DeviceCommand) {
                        deviceCommand = dataModel.getBean();
                    } else if (dataModel.getBean() instanceof DeviceCommandRef) {
                        deviceCommand = ((DeviceCommandRef) dataModel.getBean()).getDeviceCommand();
                    } else {
                        MessageBox.alert("error", "A switch can only have command instead of macor", null);
                        return;
                    }
                    command.setText(deviceCommand.getDisplayName());
                    if (forSwitchOn) {
                        SwitchCommandOnRef switchOnCmdRef = new SwitchCommandOnRef();
                        switchOnCmdRef.setOnSwitch(switchToggle);
                        switchOnCmdRef.setDeviceCommand(deviceCommand);
                        switchToggle.setSwitchCommandOnRef(switchOnCmdRef);
                    } else {
                        SwitchCommandOffRef switchOffCmdRef = new SwitchCommandOffRef();
                        switchOffCmdRef.setOffSwitch(switchToggle);
                        switchOffCmdRef.setDeviceCommand(deviceCommand);
                        switchToggle.setSwitchCommandOffRef(switchOffCmdRef);
                    }
                }
            });
        }
    }

    /**
    * The listener to select a sensor for the switch.
    */
    class SensorSelectChangeListener extends SelectionChangedListener<ModelData> {

        @SuppressWarnings("unchecked")
        @Override
        public void selectionChanged(SelectionChangedEvent<ModelData> se) {
            ComboBoxDataModel<Sensor> sensorItem;
            sensorItem = (ComboBoxDataModel<Sensor>) se.getSelectedItem();
            if (sensorItem != null) {
                Sensor sensor = sensorItem.getData();
                SwitchSensorRef switchSensorRef = new SwitchSensorRef(switchToggle);
                switchSensorRef.setSensor(sensor);
                switchToggle.setSwitchSensorRef(switchSensorRef);
            }
        }
    }
}
