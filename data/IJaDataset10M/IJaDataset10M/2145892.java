package org.openremote.modeler.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.openremote.modeler.domain.Sensor;

/**
 * It contains a list of sensors in an account.
 */
public class SensorList {

    private List<Sensor> sensors = new ArrayList<Sensor>();

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }
}
