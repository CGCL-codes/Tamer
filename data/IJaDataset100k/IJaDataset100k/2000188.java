package ch.tarnet.library.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {

    private static final Color GRID_COLOR = Color.gray;

    protected double scaleX, scaleY, offsetX, offsetY;

    private boolean axisVisible;

    private boolean gridVisible;

    public GraphPanel() {
        scaleX = 3;
        scaleY = 3;
        offsetX = 100;
        offsetY = 100;
        axisVisible = true;
        gridVisible = true;
    }

    @Override
    protected void paintComponent(Graphics gg) {
        int height = getHeight();
        Graphics2D g = (Graphics2D) gg;
        if (gridVisible) {
            g.setColor(Color.gray);
            g.drawLine(0, height, getWidth(), 0);
        }
        if (axisVisible) {
            g.setColor(Color.black);
            g.drawLine(0, height - (int) (offsetY * scaleY), getWidth(), height - (int) (offsetY * scaleY));
            g.drawLine((int) (offsetX * scaleX), 0, (int) (offsetX * scaleX), height);
        }
    }
}
