package spidr.applets.ptolemy.plot;

import java.applet.Applet;

public class PlotRegistryEvent {

    public double minX;

    public double maxX;

    public PlotRegistryEvent() {
    }

    public PlotRegistryEvent(double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX;
        System.out.println("event " + minX + " " + maxX);
    }
}
