package org.nakedobjects.plugins.dndviewer.viewer;

import org.nakedobjects.plugins.dndviewer.viewer.drawing.Color;
import org.nakedobjects.plugins.dndviewer.viewer.util.Properties;
import org.nakedobjects.runtime.context.NakedObjectsContext;

public class AwtColor implements Color {

    public static final AwtColor DEBUG_BASELINE = new AwtColor(java.awt.Color.magenta);

    public static final AwtColor DEBUG_DRAW_BOUNDS = new AwtColor(java.awt.Color.cyan);

    public static final AwtColor DEBUG_VIEW_BOUNDS = new AwtColor(java.awt.Color.orange);

    public static final AwtColor DEBUG_REPAINT_BOUNDS = new AwtColor(java.awt.Color.green);

    public static final AwtColor DEBUG_BORDER_BOUNDS = new AwtColor(java.awt.Color.pink);

    public static final AwtColor RED = new AwtColor(java.awt.Color.red);

    public static final AwtColor GREEN = new AwtColor(java.awt.Color.green);

    public static final AwtColor BLUE = new AwtColor(java.awt.Color.blue);

    public static final AwtColor BLACK = new AwtColor(java.awt.Color.black);

    public static final AwtColor WHITE = new AwtColor(java.awt.Color.white);

    public static final AwtColor GRAY = new AwtColor(java.awt.Color.gray);

    public static final AwtColor LIGHT_GRAY = new AwtColor(java.awt.Color.lightGray);

    public static final AwtColor ORANGE = new AwtColor(java.awt.Color.orange);

    public static final AwtColor YELLOW = new AwtColor(java.awt.Color.yellow);

    public static final AwtColor NULL = new AwtColor(0);

    private static final String PROPERTY_STEM = Properties.PROPERTY_BASE + "color.";

    private final java.awt.Color color;

    private String name;

    public AwtColor(final int rgbColor) {
        this(new java.awt.Color(rgbColor));
    }

    private AwtColor(final java.awt.Color color) {
        this.color = color;
    }

    public AwtColor(final String propertyName, final String defaultColor) {
        this.name = propertyName;
        color = NakedObjectsContext.getConfiguration().getColor(PROPERTY_STEM + propertyName, java.awt.Color.decode(defaultColor));
    }

    public AwtColor(final String propertyName, final AwtColor defaultColor) {
        this.name = propertyName;
        color = NakedObjectsContext.getConfiguration().getColor(PROPERTY_STEM + propertyName, defaultColor.getAwtColor());
    }

    public Color brighter() {
        return new AwtColor(color.brighter());
    }

    public Color darker() {
        return new AwtColor(color.darker());
    }

    public java.awt.Color getAwtColor() {
        return color;
    }

    @Override
    public String toString() {
        return name + " (" + "#" + Integer.toHexString(color.getRGB()) + ")";
    }

    public String getName() {
        return name;
    }
}
