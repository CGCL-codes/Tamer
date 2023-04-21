package org.iqual.chaplin.example.jukebox;

import java.awt.*;

/**
 * @author Zbynek Slajchrt
 * @since Apr 9, 2009 12:11:51 AM
 */
public class DisplayImpl implements Display {

    private int brightness;

    private int contrast;

    public DisplayImpl(int brightness) {
        this.brightness = brightness;
    }

    public DisplayImpl(String brightness) {
        this(Integer.parseInt(brightness));
    }

    public void display(String message, Image image) {
        System.out.println(message);
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public int getContrast() {
        return contrast;
    }
}
