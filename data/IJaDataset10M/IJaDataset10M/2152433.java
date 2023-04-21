package com.grt192.controller.spot;

import java.util.Vector;
import com.grt192.core.Sensor;
import com.grt192.core.controller.EventController;
import com.grt192.event.SensorChangeListener;
import com.grt192.event.SensorEvent;
import com.grt192.networking.spot.GRTServant;
import com.grt192.prototyper.PrototyperFactory;

/**
 * Broadcasts sensor data from a single sensor on a given port through a
 * RadioStreamServent
 * 
 * @author ajc
 */
public class ServantedSensorService extends EventController implements SensorChangeListener {

    /** Separates key from value in sending UTF data */
    public static final char SEPARATOR = ':';

    private final Sensor s;

    private GRTServant server;

    private Vector remoteListeners;

    /**
	 * Constructs a new telemetry server
	 * 
	 * @param source
	 *            Sensor to send data from
	 * @param port
	 *            port to broadcast on
	 */
    public ServantedSensorService(Sensor source, int port) {
        this.s = source;
        server = new GRTServant(port);
        remoteListeners = new Vector();
    }

    /**
	 * Sets this SensorService to attempt to send data to the provided
	 * prototype.
	 * 
	 * TODO listen to all prototyped hosts?
	 * 
	 * @param prototype
	 */
    public void addRemoteListener(int prototype) {
        remoteListeners.addElement(new Integer(prototype));
    }

    /**
	 * Sets this SensorService to attempt to send data to the provided
	 * prototypes
	 * 
	 * @param prototypers
	 */
    public void addRemoteListeners(int[] prototypers) {
        for (int i = 0; i < prototypers.length; i++) {
            remoteListeners.addElement(new Integer(prototypers[i]));
        }
    }

    public void startListening() {
        s.addSensorChangeListener(this);
    }

    public void stopListening() {
        s.removeSensorChangeListener(this);
    }

    public void sensorStateChanged(SensorEvent e, String key) {
        String data = key + SEPARATOR + e.getData(key);
        for (int i = 0; i < remoteListeners.size(); i++) {
            int prototype = ((Integer) remoteListeners.elementAt(i)).intValue();
            String address = PrototyperFactory.getPrototyper().getHost(prototype).getAddress();
            if (address != null) {
                server.getServentOut(address).sendData(data);
            } else {
            }
        }
    }
}
