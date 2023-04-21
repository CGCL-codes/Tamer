package de.hpi.eworld.scenarios.ui;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import javax.swing.JPanel;

public class GhostGlassPane extends JPanel {

    private static final long serialVersionUID = 5198786074168836224L;

    private final AlphaComposite composite;

    private Point location = new Point(0, 0);

    private Image ghost = null;

    public GhostGlassPane() {
        setOpaque(false);
        composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        setCursor(null);
    }

    public void setImage(final Image ghost) {
        this.ghost = ghost;
    }

    public void setPoint(final Point location) {
        this.location = location;
    }

    public Point getPoint() {
        return location;
    }

    @Override
    public void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(composite);
        if (ghost != null) {
            final double x = location.getX() - (ghost.getWidth(this) / 2d);
            final double y = location.getY() - (ghost.getHeight(this) / 2d);
            g2.drawImage(ghost, (int) x, (int) y, null);
        }
    }
}
