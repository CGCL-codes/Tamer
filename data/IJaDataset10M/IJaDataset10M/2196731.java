package org.openremote.modeler.client.widget.buildingmodeler;

import java.util.List;
import org.openremote.modeler.client.event.DeviceWizardEvent;
import org.openremote.modeler.client.event.SubmitEvent;
import org.openremote.modeler.client.listener.DeviceWizardListener;
import org.openremote.modeler.client.listener.SubmitListener;
import org.openremote.modeler.client.model.ComboBoxDataModel;
import org.openremote.modeler.client.utils.DeviceCommandWizardSelectWindow;
import org.openremote.modeler.domain.Device;
import org.openremote.modeler.domain.DeviceCommand;
import org.openremote.modeler.domain.Sensor;
import org.openremote.modeler.domain.SwitchCommandOffRef;
import org.openremote.modeler.domain.SwitchCommandOnRef;
import org.openremote.modeler.domain.SwitchSensorRef;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.Field;

/**
 * The wizard window to create a new switch for the current device.
 */
public class SwitchWizardWindow extends SwitchWindow {

    private Device device;

    public SwitchWizardWindow(Device device) {
        super(null, device);
        this.device = device;
        initSensorFiled();
        addNewSensorButton();
        addCommandSelectListeners();
        form.removeAllListeners();
        onSubmit();
    }

    private void addNewSensorButton() {
        Button newSensorButton = new Button("New sensor..");
        newSensorButton.addSelectionListener(new NewSensorListener());
        AdapterField newSensorField = new AdapterField(newSensorButton);
        newSensorField.setLabelSeparator("");
        form.insert(newSensorField, 2);
        layout();
    }

    private void initSensorFiled() {
        ListStore<ModelData> sensorStore = sensorField.getStore();
        sensorStore.removeAll();
        for (Sensor sensor : device.getSensors()) {
            ComboBoxDataModel<Sensor> sensorRefSelector = new ComboBoxDataModel<Sensor>(sensor.getName(), sensor);
            sensorStore.add(sensorRefSelector);
        }
    }

    private void addCommandSelectListeners() {
        switchOnBtn.removeAllListeners();
        switchOffBtn.removeAllListeners();
        switchOnBtn.addSelectionListener(new CommandSelectionListener(true));
        switchOffBtn.addSelectionListener(new CommandSelectionListener(false));
    }

    private void onSubmit() {
        form.addListener(Events.BeforeSubmit, new Listener<FormEvent>() {

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
                switchToggle.setAccount(DeviceContentWizardForm.account);
                fireEvent(SubmitEvent.SUBMIT, new SubmitEvent(switchToggle));
            }
        });
    }

    private final class NewSensorListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            final SensorWizardWindow sensorWizardWindow = new SensorWizardWindow(device);
            sensorWizardWindow.addListener(SubmitEvent.SUBMIT, new SubmitListener() {

                @Override
                public void afterSubmit(SubmitEvent be) {
                    Sensor sensor = be.getData();
                    device.getSensors().add(sensor);
                    SwitchSensorRef switchSensorRef = new SwitchSensorRef(switchToggle);
                    switchSensorRef.setSensor(sensor);
                    switchToggle.setSwitchSensorRef(switchSensorRef);
                    ComboBoxDataModel<Sensor> sensorRefSelector = new ComboBoxDataModel<Sensor>(sensor.getName(), sensor);
                    sensorField.getStore().add(sensorRefSelector);
                    sensorField.setValue(sensorRefSelector);
                    sensorWizardWindow.hide();
                    fireEvent(DeviceWizardEvent.ADD_CONTENT, new DeviceWizardEvent(sensor.getBeanModel()));
                }
            });
            sensorWizardWindow.addListener(DeviceWizardEvent.ADD_CONTENT, new DeviceWizardListener() {

                @Override
                public void afterAdd(DeviceWizardEvent be) {
                    fireEvent(DeviceWizardEvent.ADD_CONTENT, new DeviceWizardEvent(be.getData()));
                }
            });
        }
    }

    private final class CommandSelectionListener extends SelectionListener<ButtonEvent> {

        private boolean forSwitchOn = true;

        public CommandSelectionListener(boolean forSwitchOn) {
            this.forSwitchOn = forSwitchOn;
        }

        @Override
        public void componentSelected(ButtonEvent ce) {
            final DeviceCommandWizardSelectWindow selectCommandWindow = new DeviceCommandWizardSelectWindow(switchToggle.getDevice());
            final Button command = ce.getButton();
            selectCommandWindow.addListener(SubmitEvent.SUBMIT, new SubmitListener() {

                @Override
                public void afterSubmit(SubmitEvent be) {
                    BeanModel dataModel = be.<BeanModel>getData();
                    DeviceCommand deviceCommand = null;
                    if (dataModel.getBean() instanceof DeviceCommand) {
                        deviceCommand = dataModel.getBean();
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
}
