package net.sourceforge.javacavemaps.core.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * Write a description of class Grid here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Grid {

    public int sizeX = 100;

    public int sizeY = 100;

    public int inset = 0;

    public int margin = 30;

    public int originX = sizeX + margin;

    public int originY = sizeY + inset + margin;

    public int translation = -1;

    public Graphics2D g = null;

    public void setGraphics(Graphics g) {
        this.g = (Graphics2D) g;
    }

    public void drawLine(Line line) {
        int x1 = line.x1 + originX;
        int y1 = (translation * line.y1) + originY;
        int x2 = line.x2 + originX;
        int y2 = (translation * line.y2) + originY;
        this.g.drawLine(x1, y1, x2, y2);
    }

    public void drawGrid(Graphics g) {
        this.g = (Graphics2D) g;
        this.drawGrid();
    }

    public Line getNewLine() {
        return new Line();
    }

    public void drawGrid() {
        Color gridColor = new Color(250, 250, 255);
        float gridLineWidth = 0.2f;
        int gridStep = 5;
        if (this.g == null) return;
        doDrawGrid(gridStep, gridLineWidth, gridColor);
        gridColor = new Color(200, 200, 255);
        gridLineWidth = 0.5f;
        gridStep = 10;
        doDrawGrid(gridStep, gridLineWidth, gridColor);
        gridColor = new Color(255, 150, 150);
        gridLineWidth = 1.0f;
        gridStep = 25;
        doDrawGrid(gridStep, gridLineWidth, gridColor);
        gridColor = new Color(255, 100, 100);
        gridLineWidth = 2.0f;
        gridStep = 50;
        doDrawGrid(gridStep, gridLineWidth, gridColor);
        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        this.g.setColor(Color.black);
        x1 = 0 + margin;
        y1 = 0 + margin + inset;
        x2 = sizeX * 2 + margin;
        y2 = 0 + inset + margin;
        this.g.drawLine(x1, y1, x2, y2);
        x1 = 0 + margin;
        y1 = 0 + margin + inset;
        x2 = 0 + margin;
        y2 = sizeY * 2 + inset + margin;
        this.g.drawLine(x1, y1, x2, y2);
        x1 = sizeX * 2 + margin;
        y1 = 0 + margin + inset;
        x2 = sizeX * 2 + margin;
        y2 = sizeY * 2 + inset + margin;
        this.g.drawLine(x1, y1, x2, y2);
        x1 = 0 + margin;
        y1 = sizeY * 2 + margin + inset;
        x2 = sizeX * 2 + margin;
        y2 = sizeY * 2 + inset + margin;
        this.g.drawLine(x1, y1, x2, y2);
        x1 = sizeX + margin;
        y1 = 0 + margin + inset;
        x2 = sizeX + margin;
        y2 = sizeY * 2 + inset + margin;
        this.g.drawLine(x1, y1, x2, y2);
        x1 = 0 + margin;
        y1 = sizeY + margin + inset;
        x2 = sizeX * 2 + margin;
        y2 = sizeY + inset + margin;
        this.g.drawLine(x1, y1, x2, y2);
    }

    /** 
     * Return the string describing the object state  
     */
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        final String NEW_LINE = System.getProperty("line.separator");
        strBuilder.append(super.toString());
        strBuilder.append("sizeX:" + String.valueOf(this.sizeX) + NEW_LINE);
        strBuilder.append("sizeY:" + String.valueOf(this.sizeY) + NEW_LINE);
        strBuilder.append("inset:" + String.valueOf(this.inset) + NEW_LINE);
        strBuilder.append("margin:" + String.valueOf(this.margin) + NEW_LINE);
        strBuilder.append("originX:" + String.valueOf(this.originX) + NEW_LINE);
        strBuilder.append("originY:" + String.valueOf(this.originY) + NEW_LINE);
        strBuilder.append("translation:" + String.valueOf(this.translation) + NEW_LINE);
        strBuilder.append("g:" + (g == null ? "null" : g.toString()) + NEW_LINE);
        return strBuilder.toString();
    }

    /**
     * Draw grid lines.
     * 
     * @param step Step size in feet.
     * @param lineWidth Line width.
     * @param color Line color.
     */
    private void doDrawGrid(int step, float lineWidth, Color color) {
        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        Stroke oldStroke = this.g.getStroke();
        BasicStroke stroke = new BasicStroke(lineWidth);
        this.g.setStroke(stroke);
        this.g.setColor(color);
        x1 = margin;
        x2 = sizeX * 2 + margin;
        for (int y = inset + margin; y <= sizeY * 2 + inset + margin; y += step) {
            y1 = y;
            y2 = y;
            this.g.drawLine(x1, y1, x2, y2);
        }
        y1 = margin;
        y2 = sizeX * 2 + margin;
        for (int x = margin; x <= sizeX * 2 + margin; x += step) {
            x1 = x;
            x2 = x;
            this.g.drawLine(x1, y1, x2, y2);
        }
        this.g.setStroke(oldStroke);
    }
}
