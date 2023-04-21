package com.jgoodies.animation.components;

import java.awt.*;
import javax.swing.JComponent;

/**
 * A Swing component that paints a circle with a given center, radius and color.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 */
public final class CircleComponent extends JComponent {

    private Point center;

    private int radius;

    private Color color;

    /**
     * Constructs a <code>CircleComponent</code>.
     */
    public CircleComponent() {
        center = new Point(0, 0);
        radius = 30;
        color = Color.BLACK;
    }

    public void setCenter(Point p) {
        this.center = p;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the bounds and center point.
     * 
     * @param x   the horizontal origin
     * @param y   the vertical origin
     * @param w   the width, the horizontal extent
     * @param h   the height, the vertical extent
     */
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        setCenter(new Point(x + w / 2, y + h / 2));
    }

    /**
     * Paints the circle with anti-aliasing enabled.
     * 
     * @param g    the Graphics object to render on
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int diameter = radius * 2;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(center.x - radius, center.y - radius, diameter, diameter);
    }
}
