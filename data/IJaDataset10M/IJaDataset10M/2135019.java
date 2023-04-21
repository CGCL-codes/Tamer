package calclipse.caldron.gui.theme.themes.corona.desktop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import calclipse.caldron.gui.theme.util.MapObject;
import calclipse.caldron.gui.theme.util.MapProjection;

/**
 * A rectangular mark on a map.
 * The mark can be created by specifying an absolute or logical position.
 * @author T. Sommerland
 */
public class Mark implements MapObject {

    private static final String ZERO_DEGREES_DESCRIPTION = "0 deg";

    private final Point point;

    private final double latitude;

    private final double longitude;

    private final Color color;

    private final String text;

    private final int size;

    private final String stringRepr;

    private Mark(final Point point, final double latitude, final double longitude, final Color color, final String text, final int size) {
        this.point = point;
        this.latitude = latitude;
        this.longitude = longitude;
        this.color = color;
        this.text = text;
        this.size = size;
        if (point == null) {
            stringRepr = getDescription(latitude, longitude);
        } else {
            stringRepr = "x=" + point.x + ", y=" + point.y;
        }
    }

    public Mark(final Point point, final Color color, final String text, final int size) {
        this(point, 0, 0, color, text, size);
    }

    public Mark(final double latitude, final double longitude, final Color color, final String text, final int size) {
        this(null, latitude, longitude, color, text, size);
    }

    @Override
    public void paint(final Graphics g, final MapProjection projection, final Component c) {
        final Point p;
        if (point == null) {
            p = projection.getPoint(c.getSize(), latitude, longitude);
        } else {
            p = point;
        }
        g.setColor(color);
        final int radius = size / 2;
        final int x = p.x - radius;
        final int y = p.y - radius;
        g.fillRect(x, y, size, size);
        g.drawString(text, x + size + 1, y);
    }

    public static String getDescription(final double latitude, final double longitude) {
        final String latText;
        if (latitude == 0) {
            latText = ZERO_DEGREES_DESCRIPTION;
        } else if (latitude > 0) {
            latText = latitude + " deg N";
        } else {
            latText = -latitude + " deg S";
        }
        final String lonText;
        if (longitude == 0) {
            lonText = ZERO_DEGREES_DESCRIPTION;
        } else if (longitude > 0) {
            lonText = longitude + " deg E";
        } else {
            lonText = -longitude + " deg W";
        }
        return latText + ", " + lonText;
    }

    @Override
    public String toString() {
        return stringRepr;
    }
}
