package com.astrientlabs.ui;

import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import com.astrientlabs.fonts.Fonts;

public class SplashCanvas extends Canvas {

    private Image splash;

    private int x;

    private int y;

    private int progressBarHeight;

    private int progressBarWidth;

    private double percentDone = 0.0;

    private int progressBarIndent = 15;

    private int padding = 10;

    public SplashCanvas() {
        setFullScreenMode(true);
        try {
            splash = Image.createImage("/splash.png");
            y = (getHeight() >> 1) - (splash.getHeight() >> 1);
            x = getWidth() >> 1;
        } catch (IOException e) {
        }
        progressBarHeight = Fonts.TEXT.getHeight();
        progressBarWidth = getWidth() - (2 * progressBarIndent);
    }

    protected void paint(Graphics g) {
        g.setColor(0xEFEFEF);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (splash == null) {
            g.drawString("(splash image)", getWidth() >> 1, getHeight() >> 1, Graphics.HCENTER | Graphics.TOP);
        } else {
            g.setColor(0xFFFFFF);
            g.fillRect(0, y, getWidth(), splash.getHeight());
            g.drawImage(splash, x, y, Graphics.HCENTER | Graphics.TOP);
            g.setColor(0x777777);
            g.drawLine(0, y, getWidth(), y);
            g.drawLine(0, y + splash.getHeight(), getWidth(), y + splash.getHeight());
        }
        g.setColor(0xCCCCCC);
        g.fillRoundRect(progressBarIndent, getHeight() - progressBarHeight - padding, (int) (progressBarWidth * (percentDone / 100.0)), progressBarHeight, padding, progressBarHeight);
        g.setFont(Fonts.TEXT);
        g.setColor(0xEFEFEF);
        g.drawString(percentDone + "%", x, getHeight() - progressBarHeight - padding, Graphics.HCENTER | Graphics.TOP);
    }

    public void setPercentDone(double percentDone) {
        this.percentDone = percentDone;
        repaint();
    }
}
