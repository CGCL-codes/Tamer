package org.redpin.base.core;

import java.util.Vector;
import org.redpin.base.core.measure.BluetoothReading;
import org.redpin.base.core.measure.GSMReading;
import org.redpin.base.core.measure.WiFiReading;

/**
 * Describes a measurement containing several readings from the bluetooth-,
 * wifi- and gsm device. 
 * When extending it, you have to implement IMeasurement<T>
 * 
 * @author Philipp Bolliger (philipp@bolliger.name)
 * @author Simon Tobler (simon.p.tobler@gmx.ch)
 * @author Pascal Brogle (broglep@student.ethz.ch)
 * @version 0.2
 */
public abstract class Measurement {

    protected long timestamp = 0;

    protected Vector gsmReadings;

    protected Vector wifiReadings;

    protected Vector bluetoothReadings;

    public Measurement() {
        timestamp = System.currentTimeMillis();
        gsmReadings = new Vector();
        wifiReadings = new Vector();
        bluetoothReadings = new Vector();
    }

    public Measurement(Vector gsmReadings, Vector wifiReadings, Vector bluetoothReadings) {
        timestamp = System.currentTimeMillis();
        this.gsmReadings = gsmReadings;
        this.wifiReadings = wifiReadings;
        this.bluetoothReadings = bluetoothReadings;
    }

    /**
	 * @return the timestamp
	 */
    public long getTimestamp() {
        return timestamp;
    }

    /**
	 * @param timestamp
	 *            the timestamp to set
	 */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
	 * @return the gsm readings
	 */
    public Vector getGsmReadings() {
        return gsmReadings;
    }

    public void addGSMReading(GSMReading gsmReading) {
        this.gsmReadings.addElement(gsmReading);
    }

    /**
	 * @return the wifi readings
	 */
    public Vector getWiFiReadings() {
        return wifiReadings;
    }

    public void addWiFiReading(WiFiReading wiFiReading) {
        this.wifiReadings.addElement(wiFiReading);
    }

    /**
	 * @return the bluetooth readings
	 */
    public Vector getBluetoothReadings() {
        return bluetoothReadings;
    }

    public void addBluetoothReading(BluetoothReading bluetoothReading) {
        this.bluetoothReadings.addElement(bluetoothReading);
    }
}
