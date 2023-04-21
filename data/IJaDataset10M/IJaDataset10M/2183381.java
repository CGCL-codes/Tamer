package net.sf.adastra.el.test;

import net.sf.adastra.el.ElAware;

/**
 * I am a throwaway class for testing.
 * @author David Dunwoody (david.dunwoody@gmail.com)
 * @version $Id: TestVehicle.java 65 2006-10-14 00:48:32Z ddunwoody $
 *
 */
@ElAware
public class TestVehicle {

    private float throttle;

    private float speed;

    public void update() {
        speed += throttle;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getThrottle() {
        return throttle;
    }

    public void setThrottle(float thottle) {
        this.throttle = thottle;
    }
}
