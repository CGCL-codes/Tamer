package de.unikoeln.genetik.popgen.jfms.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import de.unikoeln.genetik.popgen.jfms.event.SimulationEvent;
import de.unikoeln.genetik.popgen.jfms.event.SimulationEventListener;

public class MonitorAdapter extends CoordinateSystem implements SimulationEventListener {

    private static final long serialVersionUID = 3040758504707701673L;

    int x;

    BufferedImage bimg;

    Graphics2D g2;

    public MonitorAdapter(double ymin, double ymax) {
        super(ymin, ymax);
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() > 0 && getHeight() > 0) {
                    createGraphics2D();
                    reset();
                    FontMetrics fm = g2.getFontMetrics();
                    if (fm.getAscent() + getY(0) + 2 * ticLength + borderOffset > getHeight()) {
                        yCorrection = fm.getAscent() + getY(0) + borderOffset + 2 * ticLength - getHeight();
                    }
                }
            }
        });
    }

    public void createGraphics2D() {
        int w = getWidth();
        int h = getHeight();
        if (g2 == null || bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
            g2 = bimg.createGraphics();
        }
    }

    void draw(int x, double d, Color color) {
        g2.setColor(color);
        int y = getY(d);
        g2.drawLine(x - 1, y, x + 1, y);
        g2.drawLine(x, y - 1, x, y + 1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.drawCoordinates(g2);
        g.drawImage(bimg, 0, 0, Color.GREEN, null);
    }

    public void reset() {
        x = borderOffset + originOffset;
        g2.setColor(getBackground());
        g2.fillRect(0, 0, bimg.getWidth(), bimg.getHeight());
    }

    public void update(SimulationEvent event) {
        if (event.getType() == SimulationEvent.StartEvent) {
            reset();
            repaint();
        }
    }
}
