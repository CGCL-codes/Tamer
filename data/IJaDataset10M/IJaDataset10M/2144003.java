package com.barteo.emulator.device.applet;

import java.awt.Color;
import java.awt.image.RGBImageFilter;
import com.barteo.emulator.device.DeviceFactory;

public final class GrayImageFilter extends RGBImageFilter {

    private double Yr, Yg, Yb;

    private double Rr, Rg, Rb;

    public GrayImageFilter() {
        this(0.2126d, 0.7152d, 0.0722d);
    }

    public GrayImageFilter(double Yr, double Yg, double Yb) {
        this.Yr = Yr;
        this.Yg = Yg;
        this.Yb = Yb;
        canFilterIndexColorModel = true;
        Color backgroundColor = ((AppletDeviceDisplay) DeviceFactory.getDevice().getDeviceDisplay()).getBackgroundColor();
        Color foregroundColor = ((AppletDeviceDisplay) DeviceFactory.getDevice().getDeviceDisplay()).getForegroundColor();
        Rr = (backgroundColor.getRed() - foregroundColor.getRed()) / 256d;
        Rg = (backgroundColor.getGreen() - foregroundColor.getGreen()) / 256d;
        Rb = (backgroundColor.getBlue() - foregroundColor.getBlue()) / 256d;
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = (rgb & 0xFF000000);
        int r = (rgb & 0x00FF0000) >>> 16;
        int g = (rgb & 0x0000FF00) >>> 8;
        int b = (rgb & 0x000000FF);
        int Y = (int) (Yr * r + Yg * g + Yb * b) % 256;
        if (Y > 255) {
            Y = 255;
        }
        Color foregroundColor = ((AppletDeviceDisplay) DeviceFactory.getDevice().getDeviceDisplay()).getForegroundColor();
        r = (int) (Rr * Y) + foregroundColor.getRed();
        g = (int) (Rg * Y) + foregroundColor.getGreen();
        b = (int) (Rb * Y) + foregroundColor.getBlue();
        return a | (r << 16) | (g << 8) | b;
    }
}
