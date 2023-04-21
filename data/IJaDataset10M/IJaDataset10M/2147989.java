package pl.edu.pw.polygen.modeler.client.utils;

import java.util.ArrayList;
import java.util.List;

public class Scale {

    private double multiplier;

    private static Scale instance = null;

    private int xOffset;

    private int yOffset;

    private static List<Scale> scaleList = null;

    private Scale() {
        multiplier = 0.001;
        xOffset = 0;
        yOffset = 0;
    }

    public static Scale getInstance() {
        if (instance == null) {
            instance = new Scale();
        }
        return instance;
    }

    public int getViewXValue(double v) {
        return (int) ((v) / multiplier + xOffset);
    }

    public double getModelXValue(int v) {
        return (double) ((v - xOffset) * multiplier);
    }

    public int getViewYValue(double v) {
        return (int) ((v / multiplier) + yOffset);
    }

    public double getModelYValue(int v) {
        return (double) ((v - yOffset) * multiplier);
    }

    public double getDXValue(int v) {
        return (double) ((v) * multiplier);
    }

    public double getDYValue(int v) {
        return (double) ((v) * multiplier);
    }

    public void zoom(int value) {
        if (value < 0) {
            value = Math.abs(value);
            multiplier /= ((value / 3) * 1.1);
        } else {
            value = Math.abs(value);
            multiplier *= ((value / 3) * 1.1);
        }
    }

    public void shiftOffsetX(int v) {
        xOffset += v;
    }

    public void shiftOffsetY(int v) {
        yOffset += v;
    }

    public void reset() {
        multiplier = 0.001;
        xOffset = 0;
        yOffset = 0;
    }
}
