package physiqueEngine.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import javax.swing.JPanel;

public class Graph extends JPanel {

    private static final String DEF_TITLE = "GRAPH";

    private static final Color DEF_GRID_COLOR = Color.GREEN;

    private static final Color DEF_LINE_COLOR = Color.RED;

    private static final Color DEF_BACKGROUND_COLOR = Color.BLACK;

    private String title = null;

    private Color gridColor = null;

    public Graph() {
        this.setBackground(Color.BLACK);
        this.setTitle(Graph.DEF_TITLE);
        this.setPreferredSize(new Dimension(300, 0));
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.drawString(this.title, 2, 12);
        g.setColor(Color.red);
        Graphics2D g2d = (Graphics2D) g;
        Path2D.Double p2dd = null;
        p2dd = new Path2D.Double();
        p2dd.moveTo(2, 33);
        p2dd.lineTo(11, 11);
        p2dd.lineTo(32, 22);
        p2dd.lineTo(323, 232);
        g2d.draw(p2dd);
        g2d.setColor(Color.GREEN);
        this.paintGrid(g, 50);
    }

    private void paintGrid(Graphics g, int gapSize) {
        int height = 0;
        int width = 0;
        Dimension dim = this.getSize();
        height = dim.height + gapSize;
        width = dim.width + gapSize;
        for (int i = 0; i < width; i += gapSize) {
            g.drawLine(i, 0, i, height);
        }
        for (int i = 0; i < height; i += gapSize) {
            g.drawLine(0, i, width, i);
        }
    }
}
