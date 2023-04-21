package com.grt192.mechanism;

import com.grt192.core.Mechanism;
import com.grt192.sensor.spot.NetSensor;

/**
 * A SPOTDriverStation 
 * @author Andrew Chen <andrewtheannihilator@gmail.com>
 */
public class SPOTDriverStation extends Mechanism {

    private final NetSensor joystick;

    public SPOTDriverStation(NetSensor joystick) {
        this.joystick = joystick;
    }

    /**
     * Gets the network-read joystick
     * @return 
     */
    public NetSensor getJoystick() {
        return joystick;
    }
}
