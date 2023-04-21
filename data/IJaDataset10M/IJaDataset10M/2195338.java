package com.rbnb.plot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Hashtable;
import com.rbnb.utility.ToString;
import com.rbnb.utility.ByteConvert;
import com.rbnb.utility.KeyValueHash;

public class PlotContainerImage extends PlotContainer {

    private boolean beenPainted = false;

    private Dimension size = null;

    private String plottitle = null;

    private PlotTitle pTitle = null;

    private PlotImage pImage = null;

    private double start = 0;

    private double duration = 0;

    private int displayMode;

    private String tableUnits = null;

    private double tableFirst = 0;

    private double tableLast = 0;

    private double tableMin = 0;

    private double tableMax = 0;

    private double tableAve = 0;

    private double tableStdDev = 0;

    private Dimension oldSize = new Dimension(0, 0);

    private boolean newData = true;

    private Image bufferImage = null;

    private FontMetrics fm = getFontMetrics(Environment.FONT12);

    private Environment environment = null;

    public PlotContainerImage(RegChannel rc, int dm, PosDurCubby pdc, Environment e) {
        super();
        plottitle = rc.name;
        displayMode = dm;
        environment = e;
        setLayout(new BorderLayout());
        add(pTitle = new PlotTitle(plottitle), BorderLayout.NORTH);
        pTitle.setVisible(false);
        add(pImage = new PlotImage(this), BorderLayout.CENTER);
    }

    public synchronized void getConfig(Hashtable ht, String prefix) {
        ht.put(prefix + "name", plottitle);
    }

    public synchronized void setConfig(Hashtable ht, String prefix) {
        if (ht.containsKey(prefix + "name")) {
            plottitle = (String) (ht.get(prefix + "name"));
            if (pTitle != null) pTitle.setTitle(plottitle);
        }
    }

    public void setDisplayMode(int dm) {
        displayMode = dm;
        if (displayMode == LayoutCubby.TableMode) {
            pTitle.setVisible(false);
            pImage.setVisible(true);
            invalidate();
            validate();
        } else if (displayMode == LayoutCubby.PlotMode) {
            pTitle.setVisible(false);
            pImage.setVisible(true);
            invalidate();
            validate();
        } else {
            System.err.println("PlotContainerImage.setDisplayMode: unknown mode: " + displayMode);
            repaint();
        }
    }

    public Hashtable getParameters() {
        return new Hashtable();
    }

    public void setParameters(Hashtable ht) {
    }

    public void setAbscissa(Time durT) {
        duration = durT.getDoubleValue();
        pImage.setDuration(duration);
    }

    public void setOrdinate(double min, double max, int numLines) {
    }

    public void setChannelData(Channel ch, Time startT) {
        start = startT.getDoubleValue();
        beenPainted = false;
        if (ch.numberOfPoints < 1 || !ch.isByteArray) {
            pImage.setNoData();
            return;
        }
        byte[][] imdata = ch.getDataByteArray();
        DataTimeStamps dts = ch.getTimeStamp();
        double[] times = dts.getTimesDouble(0, ch.getNumberOfPoints());
        pImage.setData(imdata, times, start);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        if (displayMode == LayoutCubby.PlotMode) {
        } else if (displayMode == LayoutCubby.TableMode) {
        }
        beenPainted = true;
        super.paint(g);
    }

    public synchronized boolean hasBeenPainted() {
        if (displayMode == LayoutCubby.TableMode) repaint();
        return beenPainted;
    }
}
