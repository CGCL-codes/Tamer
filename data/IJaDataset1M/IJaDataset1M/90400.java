package de.iritgo.openmetix.app.gagingstation.sensorbehaviour;

import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.Measurement;
import de.iritgo.openmetix.app.gagingstation.manager.GagingStationManager;
import de.iritgo.openmetix.app.gagingstation.manager.MeasurementStorageManager;
import de.iritgo.openmetix.core.Engine;

/**
 * Gaging sensor behaviours define how sensors handle measurements.
 *
 * @version $Id: GagingSensorBehaviour.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public abstract class GagingSensorBehaviour {

    /** The sensor to which this behaviour belongs. */
    protected GagingSensor sensor;

    /** The station that contains the sensor. */
    protected GagingStation station;

    /** The measurement storage manager. */
    protected MeasurementStorageManager measurementStorageManager;

    /**
	 * Set the gaging sensor.
	 *
	 * @param sensor The new sensor.
	 */
    public void setSensor(GagingSensor sensor) {
        this.sensor = sensor;
        GagingStationManager gsm = (GagingStationManager) Engine.instance().getManagerRegistry().getManager("GagingStationManager");
        station = gsm.findStationOfSensor(sensor.getUniqueId());
    }

    /**
	 * Initialize the behaviour.
	 *
	 * This method is called after all behaviour paramaters have been set.
	 */
    public void init() {
        measurementStorageManager = (MeasurementStorageManager) Engine.instance().getManagerRegistry().getManager("MeasurementStorageManager");
    }

    /**
	 * Receive a new measurement.
	 *
	 * @param measurement The measurement.
	 */
    public void receiveSensorValue(Measurement measurement) {
        measurementStorageManager.receiveSensorValue(measurement);
        sensor.sendMeasurementToAgents(measurement);
    }
}
