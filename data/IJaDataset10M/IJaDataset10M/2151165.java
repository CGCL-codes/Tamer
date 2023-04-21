package com.jspx.graphics.keypoint;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;

/**
 * TestAlpha creates a PNG file that shows an analog clock
 * displaying the current time of day, with an alpha channel.
 * <p/>
 * This test program was written in a burning hurry, so
 * it is not a model of efficient, or even good, code.
 * Comments and bug fixes should be directed to:
 * <p/>
 * david@catcode.com
 *
 * @author J. David Eisenberg
 * @version 1.1, 11 Nov 1999
 */
public class TestAlpha extends Frame {

    String message;

    String timeStr;

    Image clockImage = null;

    int hour, minute;

    public TestAlpha(String s) {
        super(s);
        setSize(200, 200);
    }

    public void drawClockImage(int hour, int minute) {
        Graphics g;
        Font smallFont = new Font("Helvetica", Font.PLAIN, 9);
        FontMetrics fm;
        int x0, y0, x1, y1;
        double angle;
        g = clockImage.getGraphics();
        g.setFont(smallFont);
        fm = g.getFontMetrics();
        if (hour < 12) {
            g.setColor(new Color(255, 255, 192));
        } else {
            g.setColor(new Color(192, 192, 255));
        }
        g.fillOval(10, 10, 80, 80);
        g.setColor(Color.black);
        g.drawOval(10, 10, 80, 80);
        g.drawOval(48, 48, 4, 4);
        g.setFont(smallFont);
        g.drawString("12", 50 - fm.stringWidth("12") / 2, 11 + fm.getAscent());
        g.drawString("3", 88 - fm.stringWidth("3"), 50 + fm.getAscent() / 2);
        g.drawString("6", 50 - fm.stringWidth("6") / 2, 88);
        g.drawString("9", 12, 50 + fm.getAscent() / 2);
        x0 = 50;
        y0 = 50;
        hour %= 12;
        angle = -(hour * 30 + minute / 2) + 90;
        angle = angle * Math.PI / 180.0;
        x1 = (int) (x0 + 28 * (Math.cos(angle)));
        y1 = (int) (y0 - 28 * (Math.sin(angle)));
        g.drawLine(x0, y0, x1, y1);
        angle = -(minute * 6) + 90;
        angle = angle * Math.PI / 180.0;
        x1 = (int) (x0 + 35 * (Math.cos(angle)));
        y1 = (int) (y0 - 35 * (Math.sin(angle)));
        g.drawLine(x0, y0, x1, y1);
    }

    public void addAlphaToImage() {
        int width = 100;
        int height = 100;
        int alphaMask = 0;
        int[] pixels = new int[width * height];
        PixelGrabber pg = new PixelGrabber(clockImage, 0, 0, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("interrupted waiting for pixels!");
            return;
        }
        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            System.err.println("image fetch aborted or errored");
            return;
        }
        for (int i = 0; i < width * height; i++) {
            if ((i % width) == 0) {
                alphaMask = (alphaMask >> 24) & 0xff;
                alphaMask += 2;
                if (alphaMask > 255) {
                    alphaMask = 255;
                }
                alphaMask = (alphaMask << 24) & 0xff000000;
            }
            pixels[i] = (pixels[i] & 0xffffff) | alphaMask;
        }
        clockImage = createImage(new MemoryImageSource(width, height, pixels, 0, width));
    }

    public void paint(Graphics g) {
        if (clockImage == null) {
            clockImage = createImage(100, 100);
            drawClockImage(hour, minute);
            addAlphaToImage();
            saveClockImage();
        }
        g.drawImage(clockImage, 50, 20, null);
        if (message != null) {
            g.drawString(message, 10, 140);
        }
    }

    public static void main(String[] args) {
        TestAlpha te = new TestAlpha("Test PNG Alpha Encoder");
        te.do_your_thing();
    }

    public void do_your_thing() {
        Image img;
        Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR);
        if (cal.get(Calendar.AM_PM) == 1) {
            hour += 12;
        }
        hour %= 24;
        minute = cal.get(Calendar.MINUTE);
        timeStr = Integer.toString(minute);
        if (minute < 10) {
            timeStr = "0" + timeStr;
        }
        timeStr = Integer.toString(hour) + timeStr;
        if (hour < 10) {
            timeStr = "0" + timeStr;
        }
        message = "File name: alphaclock" + timeStr + ".png";
        WindowListener l = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        addWindowListener(l);
    }

    public void saveClockImage() {
        byte[] pngbytes;
        PngEncoder png = new PngEncoder(clockImage, PngEncoder.ENCODE_ALPHA);
        try {
            FileOutputStream outfile = new FileOutputStream("alphaclock" + timeStr + ".png");
            pngbytes = png.pngEncode();
            if (pngbytes == null) {
                System.out.println("Null image");
            } else {
                outfile.write(pngbytes);
            }
            outfile.flush();
            outfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
