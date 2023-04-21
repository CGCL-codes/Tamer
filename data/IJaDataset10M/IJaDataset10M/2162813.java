package org.das2.event;

import java.util.logging.Level;
import org.das2.graph.DasCanvasComponent;
import org.das2.util.GrannyTextRenderer;
import org.das2.system.DasLogger;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 *
 * @author  Jeremy
 */
public class LabelDragRenderer implements DragRenderer {

    String label = "Label not set";

    GrannyTextRenderer gtr;

    DasCanvasComponent parent;

    InfoLabel infoLabel;

    int labelPositionX = 1;

    int labelPositionY = -1;

    Rectangle dirtyBounds;

    static final Logger logger = DasLogger.getLogger(DasLogger.GUI_LOG);

    int maxLabelWidth;

    public void clear(Graphics g) {
        if (dirtyBounds != null) parent.paintImmediately(dirtyBounds);
        dirtyBounds = null;
    }

    public LabelDragRenderer(DasCanvasComponent parent) {
        this.parent = parent;
        this.dirtyBounds = new Rectangle();
        gtr = new GrannyTextRenderer();
    }

    /**
     * This method is called by the DMIA on mouse release.  We use this to infer the mouse release
     * and hide the Window.  Note this assumes isUpdatingDragSelection is false!
     * TODO: DMIA should call clear so this is more explicit.
     */
    public MouseDragEvent getMouseDragEvent(Object source, java.awt.Point p1, java.awt.Point p2, boolean isModified) {
        maxLabelWidth = 0;
        if (tooltip) {
            if (infoLabel != null) infoLabel.hide(parent);
        }
        return null;
    }

    public boolean isPointSelection() {
        return true;
    }

    public boolean isUpdatingDragSelection() {
        return false;
    }

    public void setLabel(String s) {
        this.label = s;
    }

    private Rectangle paintLabel(Graphics g1, java.awt.Point p2) {
        if (label == null) return null;
        Graphics2D g = (Graphics2D) g1;
        g.setClip(null);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension d = parent.getCanvas().getSize();
        gtr.setString(g1, label);
        int dx = (int) gtr.getWidth() + 6;
        int dy = (int) gtr.getHeight();
        if (maxLabelWidth < dx) {
            maxLabelWidth = dx;
        }
        if ((p2.x + maxLabelWidth > d.width) && (p2.x - 3 - dx > 0)) {
            labelPositionX = -1;
        } else {
            labelPositionX = 1;
        }
        int xp;
        if (labelPositionX == 1) {
            xp = p2.x + 3;
        } else {
            xp = p2.x - 3 - dx;
        }
        int yp;
        if (p2.y - 3 - dy < 13) {
            labelPositionY = -1;
        } else {
            labelPositionY = 1;
        }
        if (labelPositionY == 1) {
            yp = p2.y - 3 - dy;
        } else {
            yp = p2.y + 3;
        }
        dirtyBounds = new Rectangle();
        Color color0 = g.getColor();
        g.setColor(new Color(255, 255, 255, 200));
        dirtyBounds.setRect(xp, yp, dx, dy);
        g.fill(dirtyBounds);
        g.setColor(new Color(20, 20, 20));
        gtr.draw(g, xp + 3, (float) (yp + gtr.getAscent()));
        g.setColor(color0);
        return dirtyBounds;
    }

    public Rectangle[] renderDrag(Graphics g, Point p1, Point p2) {
        logger.log(Level.FINEST, "renderDrag {0}", p2);
        Rectangle[] result;
        if (tooltip) {
            if (infoLabel == null) infoLabel = new InfoLabel();
            Point p = (Point) p2.clone();
            SwingUtilities.convertPointToScreen(p, parent.getCanvas());
            infoLabel.setText(label, p, parent, labelPositionX, labelPositionY);
            result = new Rectangle[0];
        } else {
            if (label == null) {
                result = new Rectangle[0];
            } else {
                Rectangle r = paintLabel(g, p2);
                result = new Rectangle[] { r };
            }
        }
        return result;
    }

    /**
     * added to more conveniently keep track of dirty bounds when subclassing.
     */
    ArrayList newDirtyBounds;

    protected void resetDirtyBounds() {
        newDirtyBounds = new ArrayList();
    }

    protected void addDirtyBounds(Rectangle[] dirty) {
        if (dirty != null && dirty.length > 0) newDirtyBounds.addAll(Arrays.asList(dirty));
    }

    protected void addDirtyBounds(Rectangle dirty) {
        if (dirty != null) newDirtyBounds.add(dirty);
    }

    protected Rectangle[] getDirtyBounds() {
        try {
            return (Rectangle[]) newDirtyBounds.toArray(new Rectangle[newDirtyBounds.size()]);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    boolean tooltip = false;

    public boolean isTooltip() {
        return tooltip;
    }

    public void setTooltip(boolean tooltip) {
        this.tooltip = tooltip;
        if (tooltip) {
            labelPositionX = 1;
            labelPositionY = -1;
        }
    }
}
