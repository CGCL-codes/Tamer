package org.javamap.overlays;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;

/**
  * Superclass of overlays which consist of line segments.
  */
abstract class LineOverlay extends org.javamap.Overlay {

    protected abstract void drawOneLocation(Graphics2D g2d, Point p);

    protected abstract void drawLineSegment(Graphics2D g2d, Point oldP, Point p);

    /**
	  * Change any graphics settings necessary for overlay rendering.
	  */
    protected abstract void setupForDraw(Graphics2D g2d);

    /**
	  * Reset changes.  Stroke and Color are preserved by this class.
	  */
    protected void resetAfterDraw(Graphics2D g2d) {
    }

    /**
	  * Available to children during drawing, in case the drawing is 
	  *  dependent upon location order.
	  */
    protected final int getLocationIndex() {
        return locationIndex;
    }

    public final void draw(Graphics2D g2d, org.javamap.JavaMapPane jmp) {
        if (getLocationCount() == 0) return;
        boolean selected = jmp.getSelectedOverlay() == this;
        Stroke oldStroke = g2d.getStroke();
        Color oldColor = g2d.getColor();
        setupForDraw(g2d);
        Object oldAA = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        locationIndex = 0;
        int selectedNode = getSelectedIndex();
        org.javamap.Location oldLoc, loc = getLocation(0);
        final Point oldP = new Point(), p = jmp.locationToPoint(loc, jmp.getDepth(), null);
        if (selected) indicateSelected(g2d, p, oldStroke, selectedNode == 0);
        if (getLocationCount() == 1) drawOneLocation(g2d, p);
        for (locationIndex = 1; locationIndex < getLocationCount(); ++locationIndex) {
            oldLoc = loc;
            oldP.setLocation(p);
            loc = getLocation(locationIndex);
            jmp.locationToPoint(loc, jmp.getDepth(), p);
            drawLineSegment(g2d, oldP, p);
            if (selected) indicateSelected(g2d, p, oldStroke, selectedNode == locationIndex);
        }
        resetAfterDraw(g2d);
        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
    }

    /**
	  * Utility to indicate that this entire overlay is selected.
	  * @param nodeSelected  If true, the point is also part of a sub-selection.
	  */
    private void indicateSelected(Graphics2D g2d, Point p, Stroke prevStroke, boolean nodeSelected) {
        Stroke currStroke = g2d.getStroke();
        Color currColor = g2d.getColor();
        g2d.setStroke(prevStroke);
        if (nodeSelected) {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(p.x - NODE_SIZE / 2, p.y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE);
        } else {
            g2d.setColor(Color.WHITE);
            g2d.setXORMode(Color.BLACK);
            g2d.drawRect(p.x - NODE_SIZE / 2, p.y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE);
        }
        g2d.setPaintMode();
        g2d.setColor(currColor);
        g2d.setStroke(currStroke);
    }

    private int locationIndex;

    protected static final int NODE_SIZE = 6;
}
