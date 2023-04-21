package org.openremote.modeler.shared.dto;

import java.io.Serializable;
import com.extjs.gxt.ui.client.data.BeanModelTag;

@SuppressWarnings("serial")
public class SliderDTO implements Serializable, BeanModelTag {

    private String displayName;

    private String commandName;

    private String sensorName;

    private String deviceName;

    private Long oid;

    public SliderDTO() {
        super();
    }

    public SliderDTO(Long oid, String displayName, String commandName, String sensorName, String deviceName) {
        super();
        this.oid = oid;
        this.displayName = displayName;
        this.commandName = commandName;
        this.sensorName = sensorName;
        this.deviceName = deviceName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
