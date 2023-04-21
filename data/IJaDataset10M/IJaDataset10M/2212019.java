package org.openremote.modeler.service;

import java.util.List;
import org.openremote.modeler.domain.Account;
import org.openremote.modeler.domain.Sensor;

public interface SensorService {

    List<Sensor> loadAll(Account account);

    Sensor saveSensor(Sensor sensor);

    Sensor updateSensor(Sensor sensor);

    Boolean deleteSensor(long id);

    Sensor loadById(long id);

    List<Sensor> loadSameSensors(Sensor sensor);
}
