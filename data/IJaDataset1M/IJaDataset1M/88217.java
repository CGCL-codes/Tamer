package com.rbnb.plot;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import javax.swing.JComponent;

public class PlotOrdinate extends JComponent {

    private PlotAbscissa pTitle = null;

    private Dimension oldSize = new Dimension(0, 0);

    private String plottitle;

    private double ordMin = -1;

    private double ordMax = 1;

    private int ordPow = 0;

    private int numLines = 1;

    private Image bufferImage = null;

    private boolean newLimits = true;

    private Font f = Environment.FONT10;

    private FontMetrics fm = null;

    private int fascent = 5;

    private int fadvance = 20;

    public PlotOrdinate(PlotAbscissa pt) {
        pTitle = pt;
    }

    public void setOrdinate(double min, double max, int num) {
        double minPow, maxPow, pow;
        String label = null;
        if (min == max) {
            System.out.println("PlotOrdinate: zero range");
            synchronized (this) {
                numLines = 0;
                ordMin = min;
                ordMax = max + 1;
                newLimits = true;
            }
            repaint();
            return;
        }
        if (min == 0) pow = Math.log(Math.abs(max)) / Math.log(10); else if (max == 0) pow = Math.log(Math.abs(min)) / Math.log(10); else {
            minPow = Math.log(Math.abs(min)) / Math.log(10);
            maxPow = Math.log(Math.abs(max)) / Math.log(10);
            pow = Math.max(minPow, maxPow);
        }
        if (pow < -12) {
            label = new String("E" + Math.ceil(pow) + " ");
            min = min / Math.pow(10, Math.ceil(pow));
            max = max / Math.pow(10, Math.ceil(pow));
        } else if (pow < -9) {
            label = new String("pico ");
            min = min / 1e-12;
            max = max / 1e-12;
        } else if (pow < -6) {
            label = new String("nano ");
            min = min / 1e-9;
            max = max / 1e-9;
        } else if (pow < -3) {
            label = new String("micro ");
            min = min / 1e-6;
            max = max / 1e-6;
        } else if (pow < 0) {
            label = new String("milli ");
            min = min / 1e-3;
            max = max / 1e-3;
        } else if (pow < 3) {
            label = new String(" ");
            min = min;
            max = max;
        } else if (pow < 6) {
            label = new String("kilo ");
            min = min / 1e3;
            max = max / 1e3;
        } else if (pow < 9) {
            label = new String("mega ");
            min = min / 1e6;
            max = max / 1e6;
        } else if (pow < 12) {
            label = new String("giga ");
            min = min / 1e9;
            max = max / 1e9;
        } else if (pow < 15) {
            label = new String("tera ");
            min = min / 1e12;
            max = max / 1e12;
        } else {
            label = new String("E" + Math.floor(pow) + " ");
            min = min / Math.pow(10, Math.floor(pow));
            max = max / Math.pow(10, Math.floor(pow));
        }
        pTitle.setOrdScale(label);
        synchronized (this) {
            numLines = num;
            newLimits = true;
            ordMin = min;
            ordMax = max;
        }
        repaint();
    }

    public void paint(Graphics g) {
        boolean newSize = false;
        boolean lLimits;
        double min, max;
        int lLines;
        synchronized (this) {
            lLimits = newLimits;
            lLines = numLines;
            newLimits = false;
            min = ordMin;
            max = ordMax;
        }
        Dimension size = getSize();
        if (size.width != oldSize.width || size.height != oldSize.height) newSize = true;
        if (size.width <= 0 || size.height <= 0) {
            super.paint(g);
            return;
        }
        if (newSize || lLimits) {
            if (newSize) {
                bufferImage = createImage(size.width, size.height);
                oldSize.width = size.width;
                oldSize.height = size.height;
            }
            Graphics bi = bufferImage.getGraphics();
            bi.setFont(f);
            bi.setColor(java.awt.SystemColor.textText);
            bi.clearRect(0, 0, size.width, size.height);
            String s = Double.toString(Math.rint(min * 1000) / 1000);
            if (min < 0) s = s.substring(0, Math.min(s.length(), 7)); else s = s.substring(0, Math.min(s.length(), 6));
            int advance = fm.stringWidth(s);
            bi.drawString(s, size.width - advance, size.height);
            s = Double.toString(Math.rint(max * 1000) / 1000);
            if (max < 0) s = s.substring(0, Math.min(s.length(), 7)); else s = s.substring(0, Math.min(s.length(), 6));
            advance = fm.stringWidth(s);
            bi.drawString(s, size.width - advance, fascent);
            double increment = (max - min) / numLines;
            for (int i = 1; i < lLines; i++) {
                s = Double.toString(Math.rint((min + i * increment) * 1000) / 1000);
                if (min + i * increment < 0) s = s.substring(0, Math.min(s.length(), 7)); else s = s.substring(0, Math.min(s.length(), 6));
                advance = fm.stringWidth(s);
                bi.drawString(s, size.width - advance, (numLines - i) * size.height / numLines + fascent / 2);
            }
            bi.setColor(Color.white);
            bi.drawLine(0, 0, 0, size.height - 1);
            bi.dispose();
        }
        g.drawImage(bufferImage, 0, 0, null);
        super.paint(g);
    }

    public Dimension getMinimumSize() {
        fm = getFontMetrics(f);
        fascent = fm.getAscent();
        fadvance = fm.stringWidth("-0.2345");
        return new Dimension(fadvance, 0);
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }
}
