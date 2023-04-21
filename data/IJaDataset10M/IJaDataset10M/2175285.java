package dw.JSci.example3;

import java.awt.*;

public class LabeledLineGraph extends JSci.swing.JLineGraph {

    private Color lineColor[] = { Color.black, Color.blue, Color.darkGray, Color.green, Color.red, Color.yellow, Color.cyan, Color.lightGray, Color.magenta, Color.orange, Color.pink };

    private LabeledGraph2DModel model;

    public LabeledLineGraph(JSci.awt.Graph2DModel gm) {
        super(gm);
        model = (LabeledGraph2DModel) gm;
    }

    /**
 * Draws the graph axes.
 */
    protected final void drawLabeledAxes(Graphics g) {
        g.setColor(Color.black);
        g.drawLine(axisPad, origin.y, getWidth() - axisPad, origin.y);
        g.drawLine(origin.x, axisPad, origin.x, getHeight() - axisPad);
        String str;
        int strWidth;
        final int strHeight = g.getFontMetrics().getHeight();
        Point p;
        float x, dx;
        if (xInc == 0.0f) dx = round(40.0f / xScale); else dx = xInc;
        for (x = dx; x <= maxX; x += dx) {
            str = model.getXLabel(x);
            strWidth = g.getFontMetrics().stringWidth('+' + str);
            p = dataToScreen(x, 0.0f);
            g.drawLine(p.x, p.y, p.x, p.y + 5);
            g.drawString(str, p.x - strWidth / 2, origin.y + strHeight + 5);
        }
        for (x = -dx; x >= minX; x -= dx) {
            str = String.valueOf(round(x));
            strWidth = g.getFontMetrics().stringWidth(str);
            p = dataToScreen(x, 0.0f);
            g.drawLine(p.x, p.y, p.x, p.y + 5);
            g.drawString(str, p.x - strWidth / 2, origin.y + strHeight + 5);
        }
        float y, dy;
        if (yInc == 0.0f) dy = round(40.0f / yScale); else dy = yInc;
        for (y = dy; y <= maxY; y += dy) {
            str = String.valueOf(round(y));
            strWidth = g.getFontMetrics().stringWidth(str);
            p = dataToScreen(0.0f, y);
            g.drawLine(p.x, p.y, p.x - 5, p.y);
            g.drawString(str, origin.x - strWidth - 8, p.y + strHeight / 4);
        }
        for (y = -dy; y >= minY; y -= dy) {
            if (y > 10) {
                int i = (int) (round(y));
                str = String.valueOf(i);
            } else {
                str = String.valueOf(round(y));
            }
            strWidth = g.getFontMetrics().stringWidth(str);
            p = dataToScreen(0.0f, y);
            g.drawLine(p.x, p.y, p.x - 5, p.y);
            g.drawString(str, origin.x - strWidth - 8, p.y + strHeight / 4);
        }
    }

    /**
 * Paint the graph.
 */
    protected void offscreenPaint(Graphics g) {
        drawLabeledAxes(g);
        Point p1, p2;
        model.firstSeries();
        g.setColor(lineColor[0]);
        p1 = dataToScreen(model.getXCoord(0), model.getYCoord(0));
        int i;
        for (i = 1; i < model.seriesLength(); i++) {
            p2 = dataToScreen(model.getXCoord(i), model.getYCoord(i));
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
            p1 = p2;
        }
        for (int n = 1; model.nextSeries(); n++) {
            g.setColor(lineColor[n]);
            p1 = dataToScreen(model.getXCoord(0), model.getYCoord(0));
            for (i = 1; i < model.seriesLength(); i++) {
                p2 = dataToScreen(model.getXCoord(i), model.getYCoord(i));
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                p1 = p2;
            }
        }
    }

    /**
 * Sets the line color of the nth y-series.
 */
    public void setColor(int n, Color c) {
        lineColor[n] = c;
    }
}
