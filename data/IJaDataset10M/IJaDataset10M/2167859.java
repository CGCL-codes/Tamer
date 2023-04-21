package org.openfibs.board.vector2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class BorderFieldLabel2D extends ContextLocationSaver {

    public static String TEST_STRING_DEFAULT = "24";

    public static String FONT_NAME_DEFAULT = Utilities2D.SANS_SERIF;

    public static int FONT_ATTRIBUTES_DEFAULT = Font.BOLD;

    private Font font;

    private String label;

    private Color color;

    Field2D.Orientation orientation;

    public BorderFieldLabel2D(Rectangle2D location, int number, Color color, Field2D.Orientation orientation, Font font) {
        setLocation(location);
        this.color = color;
        this.label = Integer.toString(number);
        this.orientation = orientation;
        this.font = font;
    }

    public BorderFieldLabel2D(Rectangle2D location, int number, Color color, Field2D.Orientation orientation) {
        this(location, number, color, orientation, null);
    }

    public void paint(Graphics2D g2d) {
        push(g2d);
        if (font == null) {
            font = FontResolver.resolveFont(TEST_STRING_DEFAULT, FONT_NAME_DEFAULT, FONT_ATTRIBUTES_DEFAULT, getLocation(), g2d.getFontRenderContext());
        }
        g2d.setColor(color);
        Utilities2D.drawXYCenteredString(g2d, font, getLocation(), label);
        pop(g2d);
    }
}
