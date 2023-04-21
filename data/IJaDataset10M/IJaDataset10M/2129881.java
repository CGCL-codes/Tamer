package net.villonanny;

import java.util.Calendar;

public class Synchronizer {

    private int numberOfObjects = 0;

    private Calendar runtime;

    public Synchronizer(int numberOfObjects, Calendar c) {
        super();
        this.numberOfObjects = numberOfObjects;
        this.runtime = c;
    }

    public int getNumberOfObjects() {
        return numberOfObjects;
    }

    public Calendar getRuntime() {
        return runtime;
    }

    public synchronized void dec() {
        numberOfObjects--;
    }

    public synchronized void inc() {
        numberOfObjects++;
    }
}
