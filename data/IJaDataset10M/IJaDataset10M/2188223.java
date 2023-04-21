package org.makagiga.plugins.mapviewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.makagiga.commons.ColorProperty;
import org.makagiga.commons.MColor;
import org.makagiga.commons.MIcon;
import org.makagiga.commons.xml.XMLBuilder;

public final class Marker implements MapMarker {

    Color color = MColor.SKY_BLUE;

    double lat;

    double lon;

    MIcon icon;

    String iconName;

    public Marker() {
    }

    public Color getColor() {
        return color;
    }

    public void setIconName(final String name) {
        if (name != null) {
            icon = MIcon.small(name);
            iconName = name;
        } else {
            icon = null;
            iconName = null;
        }
    }

    public void writeXML(final XMLBuilder xml) {
        xml.beginTag("marker");
        xml.singleTag("color", "value", ColorProperty.toString(color));
        xml.singleTag("icon", "name", iconName);
        xml.singleTag("position", "latitude", getLat(), "longitude", getLon());
        xml.endTag("marker");
    }

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLon() {
        return lon;
    }

    @Override
    public void paint(final Graphics graphics, final Point p) {
        Graphics2D g = (Graphics2D) graphics;
        int size = (icon != null) ? icon.getIconWidth() : MIcon.getSmallSize();
        int x = p.x - size / 2;
        int y = p.y - size / 2;
        if (color != null) {
            int margin = 2;
            g.setColor(color);
            g.fillRect(x - margin, y - margin, size + margin * 2, size + margin * 2);
        }
        if (icon != null) {
            icon.paintIcon(null, g, x, y);
        }
    }
}
