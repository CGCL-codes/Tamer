package de.iritgo.openmetix.app.gagingstation.sensorbehaviour;

import de.iritgo.openmetix.app.gagingstation.Measurement;

/**
 * Behaviour of system sensors.
 *
 * @version $Id: SystemSensorBehaviour.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class SystemSensorBehaviour extends GagingSensorBehaviour {

    /**
	 * Receive a new measurement.
	 *
	 * @param measurement The measurement.
	 */
    public void receiveSensorValue(Measurement measurement) {
        super.receiveSensorValue(measurement);
    }
}
