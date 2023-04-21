package sia.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JPanel;
import sia.domain.Planet;
import sia.domain.Universe;
import sia.domain.hab.GravHabRange;
import sia.domain.hab.RadHabRange;
import sia.domain.hab.TempHabRange;

public class MapPanel extends JPanel {

    private static final long serialVersionUID = -4351641981459921992L;

    private Universe universe;

    public MapPanel(Universe universe) {
        this.setPreferredSize(new Dimension(universe.getWidth(), universe.getHeight()));
        this.universe = universe;
    }

    @Override
    public void paint(Graphics g) {
        GravHabRange grav = new GravHabRange("0.17g", "2.96g", false);
        TempHabRange temp = new TempHabRange("-124ºC", "156ºC", false);
        RadHabRange rad = new RadHabRange("20mR", "90mR", false);
        g.setColor(Color.BLACK);
        Rectangle clip = g.getClipBounds();
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
        for (Planet p : universe.getPlanets()) {
            int px = p.x - 1000;
            int py = universe.getHeight() - (p.y - 1000);
            Integer pv = null;
            try {
                pv = p.calcValue(grav, temp, rad);
            } catch (Exception e) {
            }
            if (pv == null) {
                g.setColor(Color.DARK_GRAY);
                g.fillOval(px - 1, py - 1, 3, 3);
            } else if (pv <= 0) {
                g.setColor(Color.RED);
                g.fillOval(px - 1, py - 1, 3, 3);
            } else {
                int valR = Math.max(pv / 20, 1);
                g.setColor(new Color(0, 255, 0));
                g.fillOval(px - valR, py - valR, 2 * valR + 1, 2 * valR + 1);
                g.setColor(new Color(0, 127, 0));
                g.drawOval(px - valR, py - valR, 2 * valR, 2 * valR);
            }
            g.setColor(Color.GRAY);
            g.drawString(p.name, px + 4, py - 4);
        }
    }
}
