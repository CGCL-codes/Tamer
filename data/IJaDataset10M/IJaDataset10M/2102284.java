package org.openremote.beehive.api.dto.modeler;

import org.openremote.beehive.domain.modeler.Switch;
import org.openremote.beehive.domain.modeler.SwitchCommandOffRef;

@SuppressWarnings("serial")
public class SwitchCommandOffRefDTO extends CommandRefItemDTO {

    private SwitchDTO offSwitch;

    public SwitchCommandOffRefDTO() {
    }

    public SwitchCommandOffRefDTO(SwitchCommandOffRef commandOffRef, String deviceName) {
        if (commandOffRef != null) {
            setId(commandOffRef.getOid());
            setDeviceCommand(commandOffRef.getDeviceCommand().toDTO());
            setDeviceName(deviceName);
        }
    }

    public SwitchDTO getOffSwitch() {
        return offSwitch;
    }

    public void setOffSwitch(SwitchDTO offSwitch) {
        this.offSwitch = offSwitch;
    }

    public SwitchCommandOffRef toSwitchCommandOffRef(Switch switchToggle) {
        SwitchCommandOffRef commandOffRef = new SwitchCommandOffRef();
        commandOffRef.setDeviceCommand(getDeviceCommand().toDeviceCommand());
        commandOffRef.setOffSwitch(switchToggle);
        return commandOffRef;
    }
}
