package org.epistasis.combinatoric.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.epistasis.gui.AbstractChart;
import org.epistasis.gui.SelectionEvent;
import org.epistasis.gui.SelectionListener;

public class LandscapeHistogram extends AbstractChart implements SelectionListener {

    private long[] bins;

    private double xmin;

    private double xmax;

    private long binmax = 1;

    private long adjustedmax;

    private Color barColor = Color.LIGHT_GRAY;

    private int xticks;

    private int yticks;

    private boolean zoom1d = true;

    public LandscapeHistogram() {
        setXTicks(10, false);
        setYTicks(10, false);
        addSelectionListener(this);
    }

    public Color getBarColor() {
        return barColor;
    }

    public void setBarColor(Color barColor) {
        setBarColor(barColor, true);
    }

    public void setBarColor(Color barColor, boolean repaint) {
        this.barColor = barColor;
        if (repaint && isVisible()) {
            repaint();
        }
    }

    public int getXTicks() {
        return xticks;
    }

    public void setXTicks(int xticks) {
        setXTicks(xticks, true);
    }

    public void setXTicks(int xticks, boolean repaint) {
        this.xticks = xticks;
        clearXLabels(false);
        NumberFormat nf = new DecimalFormat("0.0###");
        for (int i = 0; i <= xticks; ++i) {
            double x = (double) i / xticks * getViewport().getWidth() + getViewport().getMinX();
            addXLabel(x, nf.format(x * (xmax - xmin) + xmin), false);
        }
        if (repaint && isVisible()) {
            repaint();
        }
    }

    public int getYTicks() {
        return yticks;
    }

    public void setYTicks(int yticks) {
        setYTicks(yticks, true);
    }

    public void setYTicks(int yticks, boolean repaint) {
        this.yticks = yticks;
        calcAdjustedMax();
        if (repaint && isVisible()) {
            repaint();
        }
    }

    public boolean is1DZoom() {
        return zoom1d;
    }

    public void set1DZoom(boolean zoom1d) {
        cancelSelection();
        this.zoom1d = zoom1d;
    }

    public void setBins(long[] bins) {
        setBins(bins, true);
    }

    public void setBins(long[] bins, boolean repaint) {
        setBins(bins, 0, 1, repaint);
    }

    public void setBins(long[] bins, double xmin, double xmax) {
        setBins(bins, xmin, xmax, true);
    }

    public void setBins(long[] bins, double xmin, double xmax, boolean repaint) {
        binmax = 1;
        this.bins = bins;
        this.xmin = xmin;
        this.xmax = xmax;
        for (int i = 0; bins != null && i < bins.length; ++i) {
            if (bins[i] > binmax) {
                binmax = bins[i];
            }
        }
        setSelectionEnabled(bins != null && bins.length > 0);
        calcAdjustedMax();
        setXTicks(getXTicks(), false);
        if (repaint && isVisible()) {
            repaint();
        }
    }

    private void calcAdjustedMax() {
        if (yticks > 0) {
            adjustedmax = (long) Math.ceil((double) binmax / yticks) * yticks;
            clearYLabels(false);
            NumberFormat nf = new DecimalFormat("#");
            ((DecimalFormat) nf).setGroupingSize(3);
            nf.setGroupingUsed(true);
            for (int i = 0; i <= yticks; ++i) {
                double y = (double) i / yticks * getViewport().getHeight() + getViewport().getMinY();
                addYLabel(y, nf.format(y * adjustedmax), false);
            }
        } else {
            adjustedmax = binmax;
        }
    }

    public void paint(Graphics g) {
        if (bins == null || bins.length == 0) {
            g.clearRect(0, 0, getWidth(), getHeight());
        } else {
            super.paint(g);
        }
    }

    public void drawContents(Graphics2D g, int width, int height) {
        if (bins == null || bins.length == 0) {
            return;
        }
        int min = (int) Math.floor(getViewport().getMinX() * (bins.length - 1));
        int max = (int) Math.ceil(getViewport().getMaxX() * (bins.length - 1));
        for (int i = min; i <= max; ++i) {
            int xmin = (int) Math.round(Math.max((((double) i / bins.length) - getViewport().getMinX()) / getViewport().getWidth() * width, 0));
            int xmax = (int) Math.round(Math.min((((double) (i + 1) / bins.length) - getViewport().getMinX()) / getViewport().getWidth() * width, width));
            int binheight = (int) Math.round(((bins[i] / (double) adjustedmax) - getViewport().getMinY()) / getViewport().getHeight() * height);
            g.setColor(barColor);
            g.fillRect(xmin, 0, xmax - xmin, binheight);
            g.setColor(getForeground());
            g.drawRect(xmin, 0, xmax - xmin, binheight);
        }
    }

    protected void selectionChanged(Rectangle2D before, Rectangle2D after, boolean done) {
        if (zoom1d) {
            int ymin = getBorderInsets().top + getChartInsets().top;
            int ymax = getHeight() - getBorderInsets().bottom - getChartInsets().bottom;
            super.selectionChanged(before == null ? null : new Rectangle2D.Double(before.getMinX(), ymin, before.getWidth(), ymax - ymin), new Rectangle2D.Double(after.getMinX(), ymin, after.getWidth(), ymax - ymin), done);
        } else {
            super.selectionChanged(before, after, done);
        }
    }

    public void setViewport(Rectangle2D viewport, boolean repaint) {
        super.setViewport(viewport, false);
        setXTicks(getXTicks(), false);
        setYTicks(getYTicks(), repaint);
    }

    public void selectionChanged(SelectionEvent e) {
        Rectangle2D chart = getSelectable();
        AffineTransform xform = new AffineTransform();
        xform.translate(getViewport().getX(), getViewport().getY());
        xform.scale(getViewport().getWidth(), getViewport().getHeight());
        xform.translate(0, 1);
        xform.scale(1, -1);
        xform.scale(1.0 / chart.getWidth(), 1.0 / chart.getHeight());
        xform.translate(-chart.getX(), -chart.getY());
        Rectangle2D v = (Rectangle2D) xform.createTransformedShape(e.getSelection()).getBounds2D();
        setViewport(v);
    }
}
