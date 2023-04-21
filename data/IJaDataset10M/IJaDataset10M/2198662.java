package com.grt192.spot.mechanism;

import com.grt192.core.Mechanism;
import com.grt192.event.SensorChangeListener;
import com.grt192.spot.sensor.GRTSonar;
import com.sun.spot.resources.transducers.IIOPin;

/**
 * Maxbotix Sonars must be daisy-chained if more than 1 are in use on a robot.
 * This is because the clocks on each sonar will differ, and once one sonar
 * reads the wave from another, the readings become trash.
 *
 * Chaining requires use of the rx, tx, and bw pins.
 *
 * Chaining comes in 2 varieties: constant loop and command loop
 * 
 * Note: while this class does not directly use the sonars, they are required because:
 * 1. This class doesn't make sense without sonars to chain
 * 2. Future configurations may need direct control of the sensors.
 * @author ajc
 */
public abstract class MaxbotixDaisyChain extends Mechanism {

    private GRTSonar[] sonars;

    private IIOPin rx;

    public MaxbotixDaisyChain() {
    }

    public MaxbotixDaisyChain(GRTSonar[] sonars, IIOPin rx) {
        this.sonars = sonars;
        this.rx = rx;
        for (int i = 0; i < sonars.length; i++) {
            if (!sonars[i].isRunning()) {
                sonars[i].start();
            }
        }
        rx.setAsOutput(true);
        setRxLow();
    }

    /** Start ranging the sonars */
    public abstract void stopRanging();

    /** Stop ranging the sonars */
    public abstract void startRanging();

    protected void pulseRx() {
        rx.setHigh();
        rx.setLow();
    }

    protected void setRxHigh() {
        setRx(true);
    }

    protected void setRxLow() {
        setRx(false);
    }

    protected void setRx(boolean high) {
        rx.setHigh(high);
    }

    /** Access a particular sonar */
    public GRTSonar getSonar(int i) {
        return sonars[i];
    }

    /** Adds provided <code>SensorChangeListener</code> to all provided sonars */
    public void addSonarsListener(SensorChangeListener s) {
        for (int i = 0; i < sonars.length; i++) {
            sonars[i].addSensorChangeListener(s);
        }
    }

    /** Removes provided <code>SensorChangeListener</code> from all provided sonars */
    public void removeSonarsListener(SensorChangeListener s) {
        for (int i = 0; i < sonars.length; i++) {
            sonars[i].removeSensorChangeListener(s);
        }
    }
}
