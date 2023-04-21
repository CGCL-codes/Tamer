package com.yxl.project.java3d.test.life3d_6;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class ClockAnimation extends Thread {

    private static final int MAX_REPEATS = 100;

    private static final int TIME_DELAY = 250;

    private static final int NUM_CLOCKS = 8;

    private ImageIcon clockImages[];

    private int currClock = 0;

    private int clockWidth, clockHeight;

    private int offsetX, offsetY;

    public ClockAnimation(int oX, int oY) {
        offsetX = oX;
        offsetY = oY;
        clockImages = new ImageIcon[NUM_CLOCKS];
        for (int i = 0; i < NUM_CLOCKS; i++) clockImages[i] = new ImageIcon(getClass().getResource("clocks/clock" + i + ".gif"));
        clockWidth = clockImages[0].getIconWidth();
        clockHeight = clockImages[0].getIconHeight();
    }

    public void run() {
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("No splashscreen found");
            return;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            System.out.println("No graphics context for splash");
            return;
        }
        Dimension splashDim = splash.getSize();
        int xPosn = splashDim.width - clockWidth - offsetX;
        int yPosn = splashDim.height - clockHeight - offsetY;
        boolean splashVisible = true;
        for (int i = 0; ((i < MAX_REPEATS) && splashVisible); i++) {
            clockImages[currClock].paintIcon(null, g, xPosn, yPosn);
            currClock = (currClock + 1) % NUM_CLOCKS;
            if (splash.isVisible()) splash.update(); else splashVisible = false;
            try {
                Thread.sleep(TIME_DELAY);
            } catch (InterruptedException e) {
            }
        }
    }
}
