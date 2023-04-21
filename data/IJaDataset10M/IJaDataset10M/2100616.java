package com.grt192.sensor.canjaguar;

import java.util.Vector;
import com.grt192.actuator.GRTCANJaguar;
import com.grt192.core.Sensor;
import com.grt192.event.component.JagPowerEvent;
import com.grt192.event.component.JagPowerListener;

public class GRTJagPowerSensor extends Sensor {

    private GRTCANJaguar jaguar;

    private Vector powerListeners;

    public GRTJagPowerSensor(GRTCANJaguar jag, int pollTime, String id) {
        jaguar = jag;
        this.id = id;
        this.setSleepTime(pollTime);
        powerListeners = new Vector();
    }

    public void poll() {
        double previous = getState("Voltage");
        setState("Voltage", jaguar.getOutputVoltage());
        if (previous != getState("Voltage")) {
            notifyVoltageChange();
        }
        previous = getState("Current");
        setState("Current", jaguar.getOutputCurrent());
        if (previous != getState("Current")) {
            notifyCurrentChange();
        }
        previous = getState("Temperature");
        setState("Temperature", jaguar.getTemperature());
        if (previous != getState("Temperature")) {
            notifyTemperatureChange();
        }
    }

    public void addPowerListener(JagPowerListener a) {
        powerListeners.addElement(a);
    }

    public void removePowerListener(JagPowerListener a) {
        powerListeners.removeElement(a);
    }

    protected void notifyVoltageChange() {
        for (int i = 0; i < powerListeners.size(); i++) {
            ((JagPowerListener) powerListeners.elementAt(i)).voltageChanged(new JagPowerEvent(this, JagPowerEvent.VOLTAGE_CHANGE, getState("Voltage")));
        }
    }

    protected void notifyCurrentChange() {
        for (int i = 0; i < powerListeners.size(); i++) {
            ((JagPowerListener) powerListeners.elementAt(i)).currentChanged(new JagPowerEvent(this, JagPowerEvent.CURRENT_CHANGE, getState("Current")));
        }
    }

    protected void notifyTemperatureChange() {
        for (int i = 0; i < powerListeners.size(); i++) {
            ((JagPowerListener) powerListeners.elementAt(i)).temperatureChanged(new JagPowerEvent(this, JagPowerEvent.TEMPERATURE_CHANGE, getState("Temperature")));
        }
    }
}
