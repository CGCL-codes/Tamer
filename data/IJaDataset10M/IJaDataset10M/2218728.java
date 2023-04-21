package org.openremote.modeler.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The Class Account.
 * 
 * @author Dan 2009-7-7
 */
@Entity
@Table(name = "account")
public class Account extends BusinessEntity {

    private static final long serialVersionUID = 4565186362957664336L;

    /** The users. */
    private List<User> users;

    /** The devices. */
    private List<Device> devices;

    /** The device macros. */
    private List<DeviceMacro> deviceMacros;

    private List<Sensor> sensors;

    private List<Switch> switches;

    private List<ControllerConfig> configs;

    private List<Slider> sliders;

    /**
    * Instantiates a new account.
    */
    public Account() {
        devices = new ArrayList<Device>();
        deviceMacros = new ArrayList<DeviceMacro>();
        sensors = new ArrayList<Sensor>();
        configs = new ArrayList<ControllerConfig>();
    }

    @OneToMany(mappedBy = "account")
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
    * Gets the devices.
    * 
    * @return the devices
    */
    @OneToMany(mappedBy = "account")
    public List<Device> getDevices() {
        return devices;
    }

    /**
    * Sets the devices.
    * 
    * @param devices the new devices
    */
    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    /**
    * Gets the device macros.
    * 
    * @return the device macros
    */
    @OneToMany(mappedBy = "account")
    public List<DeviceMacro> getDeviceMacros() {
        return deviceMacros;
    }

    /**
    * Sets the device macros.
    * 
    * @param deviceMacros the new device macros
    */
    public void setDeviceMacros(List<DeviceMacro> deviceMacros) {
        this.deviceMacros = deviceMacros;
    }

    /**
    * Adds the device.
    * 
    * @param device the device
    */
    public void addDevice(Device device) {
        devices.add(device);
    }

    @OneToMany(mappedBy = "account")
    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    @OneToMany(mappedBy = "account")
    public List<Switch> getSwitches() {
        return switches;
    }

    public void setSwitches(List<Switch> switches) {
        this.switches = switches;
    }

    @OneToMany(mappedBy = "account")
    public List<Slider> getSliders() {
        return sliders;
    }

    public void setSliders(List<Slider> sliders) {
        this.sliders = sliders;
    }

    @OneToMany(mappedBy = "account")
    public List<ControllerConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<ControllerConfig> configs) {
        this.configs = configs;
    }
}
