package com.davydov.suabl.model;

public class EngineProperties {

    private int PixelsPerMeter;

    private double G;

    private double SecPerTick;

    EngineProperties() {
        PixelsPerMeter = 100;
        G = 6.67428 * Math.pow(10, -11);
        SecPerTick = 50 * Math.pow(10, -3);
    }

    public int getPixelsPerMeter() {
        return PixelsPerMeter;
    }

    public double getG() {
        return G;
    }

    public double getSecPerTick() {
        return SecPerTick;
    }

    public void setPixelsPerMeter(int pixelsPerMeter) {
        PixelsPerMeter = pixelsPerMeter;
    }

    public void setG(double g) {
        G = g;
    }

    public void setSecPerTick(double secPerTick) {
        SecPerTick = secPerTick;
    }
}
