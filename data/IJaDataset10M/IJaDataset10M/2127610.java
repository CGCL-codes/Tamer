package de.dermoba.srcp.devices.listener;

public interface POWERInfoListener {

    public void POWERset(double timestamp, int bus, boolean powerOn);

    public void POWERterm(double timestamp, int bus);
}
