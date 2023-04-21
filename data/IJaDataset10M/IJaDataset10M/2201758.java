package voji.report;

import java.lang.*;
import java.awt.*;
import voji.utils.*;

/**
 * This report component changes the current color for
 * its sub components.
 */
public class ReportColor extends ReportComponent {

    /**
     * Constructs a new <code>ReportColor</code> instance
     *
     * @param color the color which should be used for this component's
     *              sub components
     */
    public ReportColor(Color color) {
        this.color = color;
    }

    /**
     * Constructs a new <code>ReportColor</code> instance
     * containing the given sub component
     *
     * @param color the color which should be used for this component's
     *              sub components
     * @param component a sub component to add
     */
    public ReportColor(Color color, ReportComponent component) {
        this(color);
        add(component);
    }

    /**
     * The color of the sub components
     */
    public Color color;

    /**
     * Draws this component
     *
     * @param g the graphical context where to draw to
     */
    public void draw(PageGraphics g) {
        Color o = g.getColor();
        g.setColor(color);
        super.draw(g);
        g.setColor(o);
    }
}
