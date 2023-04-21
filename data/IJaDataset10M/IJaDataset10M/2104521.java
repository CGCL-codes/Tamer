package org.nakedobjects.persistence.file;

public class TestClock implements Clock {

    long time = 0;

    public synchronized long getTime() {
        time += 1;
        return time;
    }
}
