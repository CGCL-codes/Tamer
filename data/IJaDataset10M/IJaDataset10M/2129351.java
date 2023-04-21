package org.openintents.tools.sensorsimulator;

import java.util.ArrayList;

/**
 * SensorSimulatorInstances class holds an ArrayList of all currently
 * running sensor simulator instances. This class is used to merge LIS
 * emulator with our simulators.
 *
 * @author Josip Balic
 */
public class SensorSimulatorInstances {

    public ISensorSimulator mSensorSimulator;

    public ArrayList<ISensorSimulator> simulators;

    /**
	 * Method that adds instance of sensorSimulator to ArrayList.
	 *
	 * @param sensorSimulator, SensorSimulator instance we want to add to ArrayList
	 */
    public void addSimulator(ISensorSimulator sensorSimulator) {
        if (simulators == null) {
            simulators = new ArrayList<ISensorSimulator>();
            simulators.add(sensorSimulator);
        } else {
            simulators.add(sensorSimulator);
        }
    }
}
