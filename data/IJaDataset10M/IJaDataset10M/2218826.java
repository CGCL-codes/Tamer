package de.ios.framework.graph;

import java.awt.*;
import java.util.*;

public class Graph {

    public DrawSystem sys;

    Point offs = new Point(0, 0);

    protected Dimension size = new Dimension();

    public static final boolean NET = true;

    public static final double AUTO = -1.1;

    protected Vector components = new Vector();

    protected GraphDrawParam parm = new GraphDrawParam();

    public int addComponent(GraphComponent comp) {
        components.addElement(comp);
        return components.size() - 1;
    }

    public void removeComponent(int idx) {
        components.removeElementAt(idx);
    }

    public int getComponentCount() {
        return components.size();
    }

    public void removeComponent(GraphComponent comp) {
        components.removeElement(comp);
    }

    public Graph() {
    }

    public Graph(Dimension size, double xmin, double ymin, double xmax, double ymax) {
        setSize(size);
        setSystem(NET, xmin, ymin, xmax, ymax);
    }

    /**
   * Reinitialise the draw-system.
   * All system-parameters are resetted (ticks, net etc.).
   */
    public DrawSystem setSystem(boolean net, double xmin, double ymin, double xmax, double ymax) {
        sys = new DrawSystem(net, xmin, ymin, xmax, ymax);
        return sys;
    }

    /**
   * Set the draw-system-bounds (minimum and maximum).
   */
    public void setSystemBounds(double xmin, double ymin, double xmax, double ymax) {
        sys.setValueBounds(xmin, ymin, xmax, ymax);
    }

    public void setWidth(int w) {
        size.width = w;
    }

    public int getWidth() {
        return size.width;
    }

    public void setHeight(int w) {
        size.height = w;
    }

    public int getHeight() {
        return size.height;
    }

    public void setSize(Dimension s) {
        size.width = s.width;
        size.height = s.height;
    }

    public void setTickXDist(double w) {
        sys.setTickXDist(w);
    }

    public double getTickXDist() {
        return sys.getTickXDist();
    }

    public void setTickYDist(double h) {
        sys.setTickYDist(h);
    }

    public double getTickYDist() {
        return sys.getTickYDist();
    }

    public void setNetDist(double w) {
        sys.setNetXDist(w);
        sys.setNetYDist(w);
    }

    public void setTickDist(double w) {
        sys.setTickXDist(w);
        sys.setTickYDist(w);
    }

    public void setOffset(Point off) {
        offs = new Point(off);
    }

    public Point getOffset() {
        return new Point(offs.x, offs.y);
    }

    public void paint(Graphics g) {
        Insets i = sys.getPreferredInsets(g);
        parm.xScale = ((double) size.width - i.left - i.right) / Math.abs(sys.getXMax() - sys.getXMin());
        parm.yScale = ((double) size.height - i.top - i.bottom) / Math.abs(sys.getYMax() - sys.getYMin());
        parm.offset.y = offs.y + i.top + (int) (sys.getYMax() * parm.yScale);
        parm.offset.x = offs.x + i.left + (int) (-sys.getXMin() * parm.xScale);
        if (sys.net) sys.drawNet(g, parm);
        Enumeration e = components.elements();
        while (e.hasMoreElements()) {
            ((GraphComponent) e.nextElement()).paint(g, parm);
        }
        sys.paint(g, parm);
    }
}
