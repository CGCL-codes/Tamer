package org.javason.test;

public class ElapsedTime {

    private long _beginTime = 0;

    private long _endTime = 0;

    public void begin() {
        _beginTime = System.currentTimeMillis();
    }

    public void end() {
        _endTime = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return _endTime - _beginTime;
    }

    public double getElaspedTimeSeconds() {
        return (getElapsedTime() * 0.001);
    }

    public void printElapsedTime() {
        System.out.println("\n" + toString() + "\n");
        System.out.flush();
    }

    public String toString() {
        return "Elapsed Time: " + getElapsedTime() + " ms";
    }
}
