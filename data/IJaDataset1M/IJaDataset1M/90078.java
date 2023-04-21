package com.javable.dataview.plots;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import com.javable.dataview.DataChannel;
import com.javable.dataview.DataView;

/**
 * Plot that uses styled lines to connect data points
 */
public class LinePlot extends AbstractPlot {

    private GeneralPath gp = new GeneralPath();

    private Point2D.Double tp = new Point2D.Double();

    private Point2D.Double ta = new Point2D.Double();

    private Point2D.Double tt = new Point2D.Double();

    /**
     * Creates new LinePlot
     * 
     * @param v data view
     */
    public LinePlot(DataView v) {
        super(v);
        clipping = GRAPHICS_CLIP;
    }

    /**
     * Plots data (as a set of channels) using <code>plotChannel</code>
     * 
     * @param g2 graphics
     */
    public final void plotData(Graphics2D g2) {
        com.javable.dataview.DataStorage storage = view.getStorage();
        com.javable.dataview.DataGroup group = null;
        com.javable.dataview.DataChannel xchannel = null;
        com.javable.dataview.DataChannel ychannel = null;
        java.awt.geom.Rectangle2D.Double clip = getClip(g2);
        double xmin = clip.getX();
        double xmax = clip.getX() + clip.getWidth();
        try {
            for (int i = 0; i < storage.getGroupsSize(); i++) {
                group = storage.getGroup(i);
                xchannel = storage.getChannel(i, group.getXChannel());
                for (int j = 0; j < storage.getChannelsSize(i); j++) {
                    if (j != group.getXChannel()) {
                        ychannel = storage.getChannel(i, j);
                        plotChannel(g2, xchannel, ychannel, xmin, xmax);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * Plots content of the single channel
     * 
     * @param g2 graphics
     * @param xChannel channel containing independent variable values
     * @param yChannel channel containing dependent variable values
     * @param xMin minimal value for plotting range
     * @param xMax maximal value for plotting range
     */
    public final void plotChannel(Graphics2D g2, DataChannel xChannel, DataChannel yChannel, double xMin, double xMax) {
        try {
            transform = view.getTransform();
            if (yChannel.getAttribute().isNormal()) {
                g2.setColor(yChannel.getAttribute().getColor());
                g2.setStroke(yChannel.getAttribute().getStroke());
                gp.reset();
                tp = null;
                for (int k = 0; k < xChannel.size(); k++) {
                    double xval = xChannel.getData(k);
                    if ((xval >= xMin) && (xval <= xMax) && (k < yChannel.size())) {
                        double yval = yChannel.getData(k);
                        if (tp == null) {
                            tp = new Point2D.Double(xval, yval);
                            ta = new Point2D.Double(xval, yval);
                            transform.transform(ta, ta);
                            gp.moveTo((float) xval, (float) yval);
                        } else {
                            tp.setLocation(xval, yval);
                            transform.transform(tp, tt);
                            if (ta.distance(tt) >= distance) {
                                gp.lineTo((float) xval, (float) yval);
                                ta.setLocation(xval, yval);
                                transform.transform(ta, ta);
                            }
                        }
                    }
                }
                g2.draw(gp.createTransformedShape(transform));
            }
        } catch (Exception e) {
        }
    }

    /**
     * Selects a channel according to given coordinates (usually) in response to
     * the MouseEvent
     * 
     * @param x x coordinate
     * @param y y coordinate
     */
    public void selectChannel(double x, double y) {
        com.javable.dataview.DataStorage storage = view.getStorage();
        Point2D.Double dstTop = new Point2D.Double();
        Point2D.Double dstBtm = new Point2D.Double();
        try {
            transform = view.getTransform();
            transform.inverseTransform((Point2D) (new Point2D.Double(x, y - hotspot)), dstTop);
            transform.inverseTransform((Point2D) (new Point2D.Double(x, y + hotspot)), dstBtm);
            for (int i = 0; i < storage.getGroupsSize(); i++) {
                com.javable.dataview.DataGroup group = storage.getGroup(i);
                com.javable.dataview.DataChannel xchannel = storage.getChannel(i, group.getXChannel());
                for (int j = 0; j < storage.getChannelsSize(i); j++) {
                    double tst = com.javable.dataview.analysis.ChannelStats.getValueAtX(dstTop.getX(), xchannel, storage.getChannel(i, j));
                    if (tst > dstBtm.getY() && tst < dstTop.getY()) view.fireSelectChannel(storage.getChannelNode(i, j));
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * Returns new instance of the plot
     * 
     * @param v DataView to use
     * @return new instance of the current plot
     */
    public AbstractPlot createPlot(DataView v) {
        return new LinePlot(v);
    }
}
