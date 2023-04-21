package com.nokia.ats4.appmodel.main.swing.simulator;

import com.nokia.ats4.appmodel.grapheditor.swing.JGraphEditor;
import com.nokia.ats4.appmodel.grapheditor.swing.KendoGraphCell;
import com.nokia.ats4.appmodel.grapheditor.swing.SystemStateView;
import com.nokia.ats4.appmodel.model.KendoModel;
import com.nokia.ats4.appmodel.model.domain.State;
import com.nokia.ats4.appmodel.model.impl.MainApplicationModel;
import com.nokia.ats4.appmodel.perspective.DesignPerspective;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

/**
 * ZoomAnimationPainter, this paints animation to the screen
 * 
 * @author Hannu-Pekka Hakam&auml;ki.
 * @version $Revision: 2 $
 */
public class ZoomAnimationPainter extends JComponent {

    int x, y, w, h;

    Point s1 = new Point(0, 0);

    Point s2 = new Point(0, 0);

    Point s3 = new Point(0, 0);

    private Icon image = null;

    private DefaultGraphCell animatedCell;

    private int frame;

    public ZoomAnimationPainter() {
        this(0, 0, 0, 0, null);
    }

    /**
         * Creates the animation object
         */
    public ZoomAnimationPainter(int x, int y, int w, int h, Icon image) {
        super();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.image = image;
        this.frame = 1;
        this.animatedCell = null;
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
        this.w = 0;
        this.h = 0;
        s1 = new Point(0, 0);
        s2 = new Point(0, 0);
        s3 = new Point(0, 0);
        this.animatedCell = null;
        this.frame = 1;
        this.image = null;
        repaint();
    }

    /**
         * This is setter for drawing bounds
         */
    public void setDrawBounds(Rectangle2D r) {
        this.x = (int) r.getX();
        this.y = (int) r.getY();
        if (image != null) {
            double ratio = ((double) image.getIconWidth()) / image.getIconHeight();
            this.w = (int) Math.round(r.getHeight() * ratio);
        } else {
            this.w = (int) r.getWidth();
        }
        this.h = (int) r.getHeight();
        this.x = this.s2.x - this.w - this.frame / 8;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (this.animatedCell != null) {
            getBoundsForAnimatedState(this.animatedCell);
            final JGraphEditor activeEditor = getActiveJGraphEditor();
            activeEditor.getCellBounds(this.animatedCell);
            Rectangle2D cellBounds = activeEditor.getCellBounds(this.animatedCell);
            if (cellBounds != null) {
                double scale = activeEditor.getScale();
                Rectangle scaledBounds = new Rectangle((int) (cellBounds.getX() * scale), (int) (cellBounds.getY() * scale), (int) (cellBounds.getHeight() * scale), (int) (cellBounds.getWidth() * scale));
                if (activeEditor.getViewPortBounds().contains(scaledBounds)) {
                    Graphics2D g2 = (Graphics2D) g;
                    Color c = new Color(102, 102, 102, 100);
                    g2.setPaint(c);
                    g2.fillRoundRect(x, y, w, h, 5, 5);
                    paintImage(g2);
                    paintShadow(g2);
                    g2.setPaint(new Color(102, 102, 102, 190));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(x, y, w, h, 5, 5);
                }
            }
        }
    }

    public void setS1(Point s1) {
        this.s1 = s1;
    }

    public void setS2(Point s2) {
        this.s2 = s2;
    }

    public void setS3(Point s3) {
        this.s3 = s3;
    }

    /**
         * This paints tha shadow object
         *
         * @param g2 the graphics object that draws
         */
    private void paintShadow(Graphics2D g2) {
        Color c = new Color(140, 140, 140, 160);
        Polygon p = new Polygon();
        p.addPoint(s1.x, s1.y);
        p.addPoint(s2.x, s2.y);
        p.addPoint(s3.x, s3.y);
        p.addPoint(x + w, y + h);
        p.addPoint(x + w, y);
        p.addPoint(x, y);
        g2.setPaint(c);
        g2.fillPolygon(p);
        c = new Color(200, 200, 200, 140);
        g2.setPaint(c);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(s1.x, s1.y, x, y);
        g2.drawLine(s3.x, s3.y, x + w, y + h);
    }

    private void paintImage(Graphics2D g2) {
        ImageIcon image2 = (ImageIcon) this.image;
        if (image2 != null) {
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(image2.getImage(), x, y, w, h, g2.getColor(), this);
        } else {
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            int sw = g2.getFontMetrics().stringWidth("no pic");
            g2.drawString("no pic", x + ((w - sw) / 2), y + h / 2);
        }
    }

    /**
         * This method should normally return true if given coordinates are inside this component.
         * Currently this is hardcoded to return false to hide the glass pane
         * from Swing event handling and to let all events be handled directly
         * by components below the glass pane.
         *
         * @param x coordinate
         * @param y coordinate
         * @return false
         */
    @Override
    public boolean contains(int x, int y) {
        return false;
    }

    public void updateAnimationStatus(DefaultGraphCell cell, int frame, Icon image) {
        if (this.animatedCell != cell) {
            this.reset();
            this.animatedCell = cell;
            this.image = image;
        }
        this.frame = frame;
    }

    private void getBoundsForAnimatedState(DefaultGraphCell state) {
        if (state != null) {
            final JGraphEditor activeEditor = getActiveJGraphEditor();
            final double scale = activeEditor.getScale();
            final Rectangle2D bounds = GraphConstants.getBounds(state.getAttributes()).getBounds2D();
            final Point p = SwingUtilities.convertPoint(activeEditor, (int) (bounds.getBounds().getX() * scale), (int) ((bounds.getBounds().getY() + SystemStateView.TITLE_HEIGHT) * scale), this);
            final int x = p.x;
            final int y = p.y;
            bounds.setRect(x, y, bounds.getWidth() * scale, (bounds.getHeight() - SystemStateView.TITLE_HEIGHT) * scale);
            this.setDrawBounds(bounds);
            this.setS1(new Point(x, y));
            this.setS2(new Point(x + (int) bounds.getWidth(), y));
            this.setS3(new Point(x + (int) bounds.getWidth(), y + (int) bounds.getHeight()));
            getPositionInFrame(bounds, this.frame, getInterval((State) state.getUserObject()));
            this.setDrawBounds(bounds);
        }
    }

    private double getInterval(State s) {
        KendoModel km = s.getContainingModel().getContainingModel();
        KendoGraphCell scell = (KendoGraphCell) km.getEditorModel().getCellByUserObject(s);
        Rectangle2D bounds = GraphConstants.getBounds(scell.getAttributes());
        double maxHeight = bounds.getHeight() * 1.7;
        double newInterval = (maxHeight - bounds.getHeight()) / 45;
        return newInterval;
    }

    private void getPositionInFrame(final Rectangle2D bounds, final int frameNumber, final double interval) {
        final double xChange;
        final double yChange;
        final double widthChange;
        final double heightChange;
        if (frameNumber <= 1) {
            xChange = 0;
            yChange = 0;
            widthChange = 0;
            heightChange = 0;
        } else {
            xChange = -(interval + 1) * frameNumber / 2;
            yChange = frameNumber / 4;
            widthChange = interval * frameNumber;
            heightChange = interval * frameNumber;
        }
        bounds.setRect(bounds.getX() + xChange, bounds.getY() + yChange, bounds.getWidth() + widthChange, bounds.getHeight() + heightChange);
    }

    /**
         * Returns the currently active JGraphEditor.
         * @return Active JGraphEditor.
         */
    private JGraphEditor getActiveJGraphEditor() {
        DesignPerspective activePerspective = (DesignPerspective) MainApplicationModel.getInstance().getActivePerspective();
        return activePerspective.getActiveGraphEditor().getJGraphEditor();
    }
}
