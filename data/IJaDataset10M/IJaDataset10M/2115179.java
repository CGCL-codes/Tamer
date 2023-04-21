package com.grt192.demos;

import com.grt192.core.GRTObject;
import com.grt192.event.SensorChangeListener;
import com.grt192.event.SensorEvent;
import com.grt192.spot.mechanism.MaxbotixSoftConstantLoop;
import com.grt192.spot.sensor.GRTPulseSwitch;
import com.grt192.spot.sensor.GRTSonar;
import com.sun.spot.resources.transducers.IIOPin;
import com.sun.spot.sensorboard.EDemoBoard;

/**
 *
 * @author ajc
 */
public class MaxbotixSoftConstantLoopDemo extends GRTObject implements SensorChangeListener {

    public MaxbotixSoftConstantLoopDemo() {
        setPrinting(true);
        log("building rx pin on d4...");
        IIOPin rx = EDemoBoard.getInstance().getIOPins()[4];
        log("building sonars...");
        GRTSonar[] sonars = new GRTSonar[3];
        for (int i = 0; i < sonars.length; i++) {
            sonars[i] = GRTSonar.fromAnalog(i, 50, "a" + i);
            sonars[i].addSensorChangeListener(this);
            sonars[i].start();
        }
        log("building pulseSwitch on d0...");
        GRTPulseSwitch p = new GRTPulseSwitch(0, 5, "pswitch");
        MaxbotixSoftConstantLoop c = new MaxbotixSoftConstantLoop(sonars, rx, p);
        c.startRanging();
    }

    public void sensorStateChanged(SensorEvent e, String key) {
        log(e.getSource().getId() + ":\t" + key + ":\t" + e.getData());
    }
}
