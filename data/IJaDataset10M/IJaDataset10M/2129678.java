package org.modss.facilitator.util.glyph;

import java.awt.*;
import javax.swing.*;

/** An implementation of Icon which is just a coloured disk.
 ** <strong>TODO:</strong> Rewrite to fit in with the glyph concept.
 **
 ** @author John Farrell
 **/
public class DiscIcon implements Icon {

    Color coreColour;

    Color edgeColour;

    int diam;

    public DiscIcon(Color coreColour, Color edgeColor, int diam) {
        this.coreColour = coreColour;
        this.edgeColour = edgeColour;
        this.diam = diam;
    }

    public DiscIcon(Color colour, int diam) {
        this(colour, colour.darker(), diam);
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(edgeColour);
        g.fillOval(x, y, diam, diam);
        g.setColor(coreColour);
        g.fillOval(x + 1, y + 1, diam - 2, diam - 2);
    }

    public int getIconWidth() {
        return diam;
    }

    public int getIconHeight() {
        return diam;
    }
}
