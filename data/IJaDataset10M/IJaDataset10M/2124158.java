package com.grt192.event.component;

/**
 *
 * @author anand
 */
public interface GyroListener {

    public void didReceiveAngle(GyroEvent e);

    public void didAngleChange(GyroEvent e);

    public void didAngleSpike(GyroEvent e);
}
