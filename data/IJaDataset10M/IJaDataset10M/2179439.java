package de.grogra.util;

import java.awt.*;
import java.net.URL;

public interface SplashScreen {

    void init(String title, URL background, Rectangle barBounds, URL barLeft, URL barRight, Point textLocation, Font textFont, Color textColor);

    void show();

    void toFront();

    void setInitializationProgress(float progress, String text);

    void close();
}
