package org.spruice;

/**
 * User: mrettig
 * Date: Feb 7, 2009
 * Time: 3:21:00 PM
 */
public class Car {

    private boolean started;

    private Engine e;

    public Car(Engine e) {
        this.e = e;
    }

    public Engine getEngine() {
        return e;
    }

    public boolean isStarted() {
        return started;
    }

    public void start() {
        started = true;
    }

    public void turnOff() {
        started = false;
    }
}
